package com.sward.cybernetics.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.sward.cybernetics.CyberConfig;
import com.sward.cybernetics.capabilities.ICyberneticsInventory;
import com.sward.cybernetics.data.CyberneticsSlotDefinition;
import com.sward.cybernetics.inventory.OperatingTableMenu;
import net.minecraft.FieldsAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

import javax.annotation.ParametersAreNonnullByDefault;

import java.util.List;

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
			guiGraphics.blit(
				INVENTORY_LOCATION,
				x + slots.get(i).x - 1,
				y + slots.get(i).y - 1,
				0,
				0,
				this.imageHeight,
				18,
				18,
				512,
				256
			);
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

		int l = 18;
		int r = 18;

		for (CyberneticsSlotDefinition definition : CyberConfig.Server.getSlotDefinitions())
		{
			int slotCount = definition.slotCount();
			boolean onRight = definition.onRight();

			guiGraphics.drawString(
				this.font,
				Component.translatable("cybernetics_group." + definition.id().location().getPath()),
				onRight ? 8 : 116,
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
			100,
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
}
