package com.sward.cybernetics.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.sward.cybernetics.client.screens.util.Label;
import com.sward.cybernetics.config.CyberConfig;
import com.sward.cybernetics.attributes.CyberAttributes;
import com.sward.cybernetics.config.CyberSlotType;
import com.sward.cybernetics.config.CyberSlotGroup;
import com.sward.cybernetics.inventory.CyberneticSlot;
import com.sward.cybernetics.inventory.OperatingTableMenu;
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
import net.minecraft.world.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;

import java.util.ArrayList;
import java.util.List;

import static com.sward.cybernetics.Cybernetics.id;
import static net.minecraft.client.gui.screens.inventory.InventoryScreen.renderEntityInInventoryFollowsMouse;

@FieldsAreNonnullByDefault
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class OperatingTableScreen extends AbstractContainerScreen<OperatingTableMenu>
{
	public static final ResourceLocation INVENTORY_LOCATION = id("textures/gui/container/operating_table.png");

	private final List<Label> groupLabels = new ArrayList<>();

	public OperatingTableScreen(OperatingTableMenu menu, Inventory playerInventory, Component title)
	{
		super(menu, playerInventory, title);
		this.imageWidth = 176;
		this.imageHeight = 224;
		this.inventoryLabelY = 131;
	}

	@Override
	protected void init()
	{
		super.init();

		int l = 16;
		int r = 16;

		boolean onRight = false;

		for (CyberSlotGroup group : CyberConfig.SLOTS.groups)
		{
			FormattedCharSequence text = Component
				.translatable("cybernetics_group." + group.id.get())
				.getVisualOrderText();

			int width = this.font.width(text);

			this.groupLabels.add(
				Label.at(
					text,
					this.font,
					onRight ? 116 - Math.max(width - 18 * 3, 0) : 8,
					onRight ? r : l,
					0x00FF00,
					false
				)
			);

			int n = 0;

			for (CyberSlotType type : group.slotTypes)
			{
				n += type.count.get();
			}

			if (onRight)
			{
				r += ((n + 2) / 3) * 18 + 11;
			}
			else
			{
				l += ((n + 2) / 3) * 18 + 11;
			}

			onRight = !onRight;
		}
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

				if (cyberSlot.hasItem())
				{
					continue;
				}

				CyberSlotType slotTypes = CyberConfig.SLOTS.findSlotType(cyberSlot.type);

				if (slotTypes == null || slotTypes.icon.isEmpty())
				{
					continue;
				}

				guiGraphics.blit(
					id("textures/gui/slot_icons/" + slotTypes.icon + ".png"),
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
		int totalLoad = (int) this.minecraft.player.getAttributeValue(CyberAttributes.LOAD.get());

		drawCenteredString(
			guiGraphics,
			this.font,
			Component.literal((capacity - totalLoad) + " / " + capacity),
			this.imageWidth / 2,
			116,
			0x00FF00,
			false
		);

		for (Label label : this.groupLabels)
		{
			label.draw(guiGraphics);
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

		if (this.menu.getCarried().isEmpty() && this.hoveredSlot != null)
		{
			if (this.hoveredSlot.hasItem())
			{
				ItemStack itemstack = this.hoveredSlot.getItem();

				guiGraphics.renderTooltip(
					this.font,
					this.getTooltipFromContainerItem(itemstack),
					itemstack.getTooltipImage(),
					itemstack,
					mouseX,
					mouseY
				);
			}
			else if (this.hoveredSlot instanceof CyberneticSlot cyberSlot)
			{
				guiGraphics.renderTooltip(
					this.font,
					Component.translatable("cybernetics_slot_type." + cyberSlot.type),
					mouseX,
					mouseY
				);
			}
		}
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

	private void drawCenteredString(
		GuiGraphics guiGraphics,
		Font font,
		Component text,
		int x,
		int y,
		int color,
		boolean dropShadow
	)
	{
		FormattedCharSequence charSequence = text.getVisualOrderText();
		guiGraphics.drawString(font, charSequence, x - font.width(charSequence) / 2, y, color, dropShadow);
	}
}
