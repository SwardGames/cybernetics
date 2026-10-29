package com.sward.cybernetics.inventory;

import com.sward.cybernetics.CyberConfig;
import com.sward.cybernetics.data.CyberneticsSlotGroup;
import net.minecraft.FieldsAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;

@FieldsAreNonnullByDefault
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class OperatingTableMenu extends AbstractContainerMenu
{
	public OperatingTableMenu(int containerId, Inventory playerInv)
	{
		this(
			containerId,
			playerInv,
			new SimpleContainer(
				CyberConfig.Server.getSlotGroups()
					.stream()
					.mapToInt(CyberneticsSlotGroup::slotCount)
					.sum()
			)
		);
	}

	public OperatingTableMenu(int containerId, Inventory playerInv, Container cyberInv)
	{
		super(CyberMenuTypes.OPERATING_TABLE.get(), containerId);

		int z = 0;
		int l = 15 + 11;
		int r = 15 + 11;

		for (CyberneticsSlotGroup group : CyberConfig.Server.getSlotGroups())
		{
			int slotCount = group.slotCount();
			boolean onRight = group.onRight();

			int x0 = onRight ? 116 : 8;
			int y0 = onRight ? r : l;

			for (int j = 0; j < slotCount; ++j)
			{
				this.addSlot(new CyberneticSlot(cyberInv, z, x0 + (j % 3) * 18, y0 + (j / 3) * 18, group.id(), j));

				++z;
			}

			if (onRight)
			{
				r += ((slotCount + 2) / 3) * 18 + 11;
			}
			else
			{
				l += ((slotCount + 2) / 3) * 18 + 11;
			}
		}

		for (int j = 0; j < 3; ++j)
		{
			for (int i = 0; i < 9; ++i)
			{
				this.addSlot(new Slot(playerInv, i + j * 9 + 9, 8 + i * 18, 142 + j * 18));
			}
		}

		for (int i = 0; i < 9; ++i)
		{
			this.addSlot(new Slot(playerInv, i, 8 + i * 18, 200));
		}
	}

	@Override
	public ItemStack quickMoveStack(Player pPlayer, int pIndex)
	{
		return ItemStack.EMPTY;
	}

	@Override
	public boolean stillValid(Player pPlayer)
	{
		return true;
	}
}
