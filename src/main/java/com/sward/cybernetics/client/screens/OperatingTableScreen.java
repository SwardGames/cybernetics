package com.sward.cybernetics.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.sward.cybernetics.CyberConfig;
import com.sward.cybernetics.attributes.CyberAttributes;
import com.sward.cybernetics.data.CyberneticsSlotGroup;
import com.sward.cybernetics.inventory.CyberneticSlot;
import com.sward.cybernetics.inventory.OperatingTableMenu;
import com.sward.cybernetics.items.CyberneticItem;
import net.minecraft.FieldsAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.sward.cybernetics.Cybernetics.id;
import static net.minecraft.client.gui.screens.inventory.InventoryScreen.renderEntityInInventoryFollowsMouse;

@FieldsAreNonnullByDefault
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class OperatingTableScreen extends AbstractContainerScreen<OperatingTableMenu>
{
	public static final ResourceLocation INVENTORY_LOCATION = id("textures/gui/container/operating_table.png");

	public OperatingTableScreen(OperatingTableMenu menu, Inventory playerInventory, Component title)
	{
		super(menu, playerInventory, title);
		this.imageWidth = 176;
		this.imageHeight = 224;
		this.inventoryLabelY = 131;
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY)
	{
		int x = this.leftPos;
		int y = this.topPos;

		guiGraphics.blit(
			INVENTORY_LOCATION,
			x,
			y,
			0,
			0,
			0,
			this.imageWidth,
			this.imageHeight,
			512,
			256
		);

		NonNullList<Slot> slots = this.menu.slots;

		for (int i = 0; i < slots.size() - Inventory.INVENTORY_SIZE; ++i)
		{
			if (slots.get(i) instanceof CyberneticSlot cyberSlot)
			{
				guiGraphics.blit(
					INVENTORY_LOCATION,
					x + cyberSlot.x - 1,
					y + cyberSlot.y - 1,
					0,
					0,
					this.imageHeight,
					18,
					18,
					512,
					256
				);

				if (cyberSlot.groupIndex != 0)
				{
					continue;
				}

				if (cyberSlot.hasItem())
				{
					continue;
				}

				CyberneticsSlotGroup group = CyberConfig.Server.getSlotGroup(cyberSlot.groupId);

				if (group == null || group.primarySlotIcon() == null)
				{
					continue;
				}

				guiGraphics.blit(
					id("textures/gui/primary_slot/" + group.primarySlotIcon() + ".png"),
					x + cyberSlot.x,
					y + cyberSlot.y,
					0,
					0,
					0,
					16,
					16,
					16,
					16
				);
			}
		}

		Pose pose = this.minecraft.player.getPose();
		this.minecraft.player.setPose(Pose.STANDING);

		renderEntityInInventoryFollowsMouse(
			guiGraphics,
			x + this.imageWidth / 2,
			y + 100,
			30,
			(float) (x + this.imageWidth / 2) - mouseX,
			(float) (y + 100 - 50) - mouseY,
			this.minecraft.player
		);

		this.minecraft.player.setPose(pose);
	}

	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY)
	{
		guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 0x00FF00, false);
		guiGraphics.drawString(
			this.font,
			this.playerInventoryTitle,
			this.inventoryLabelX,
			this.inventoryLabelY,
			4210752,
			false
		);

		int capacity = (int) this.minecraft.player.getAttributeValue(CyberAttributes.CAPACITY.get());
		int totalLoad = getTotalLoad();

		drawCenteredString(
			guiGraphics,
			this.font,
			Component.literal((capacity - totalLoad) + " / " + capacity),
			this.imageWidth / 2,
			116,
			0x00FF00,
			false
		);

		int l = 16;
		int r = 16;

		for (CyberneticsSlotGroup group : CyberConfig.Server.getSlotGroups())
		{
			int slotCount = group.slotCount();
			boolean onRight = group.onRight();

			FormattedCharSequence str = Component.translatable("cybernetics_group." + group.id()).getVisualOrderText();

			int width = this.font.width(str);

			guiGraphics.drawString(
				this.font,
				Component.translatable("cybernetics_group." + group.id()),
				onRight ? 116 - Math.max(width - 18 * 3, 0) : 8,
				onRight ? r : l,
				0x00FF00,
				false
			);

			if (onRight)
			{
				r += ((slotCount + 2) / 3) * 18 + 11;
			}
			else
			{
				l += ((slotCount + 2) / 3) * 18 + 11;
			}
		}

		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		guiGraphics.blit(
			INVENTORY_LOCATION,
			0,
			0,
			280,
			this.imageWidth,
			0,
			this.imageWidth,
			this.imageHeight,
			512,
			256
		);
		RenderSystem.disableBlend();
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick)
	{
		super.render(guiGraphics, mouseX, mouseY, partialTick);
		renderTooltip(guiGraphics, mouseX, mouseY);
	}

	@Override
	public void onClose()
	{
		super.onClose();

		this.minecraft.player.connection.send(
			new ServerboundPlayerCommandPacket(
				this.minecraft.player,
				ServerboundPlayerCommandPacket.Action.STOP_SLEEPING
			)
		);
	}

	private void drawCenteredString(GuiGraphics guiGraphics, Font font, Component text, int x, int y, int color, boolean dropShadow)
	{
		FormattedCharSequence charSequence = text.getVisualOrderText();
		guiGraphics.drawString(font, charSequence, x - font.width(charSequence) / 2, y, color, dropShadow);
	}

	private int getTotalLoad()
	{
		int result = 0;

		NonNullList<Slot> slots = this.menu.slots;

		for (int i = 0; i < slots.size() - Inventory.INVENTORY_SIZE; ++i)
		{
			if (slots.get(i) instanceof CyberneticSlot cyberSlot)
			{
				if (cyberSlot.getItem().getItem() instanceof CyberneticItem item)
				{
					result = result + item.capacityCost;
				}
			}
		}

		return result;
	}
}
