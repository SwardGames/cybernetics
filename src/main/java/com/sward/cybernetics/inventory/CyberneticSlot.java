package com.sward.cybernetics.inventory;

import com.sward.cybernetics.data.CyberneticSlotType;
import com.sward.cybernetics.items.CyberneticItem;
import net.minecraft.FieldsAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;

@FieldsAreNonnullByDefault
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CyberneticSlot extends Slot
{
	public String groupId;
	public int groupIndex;

	public CyberneticSlot(
		Container pContainer,
		int pSlot,
		int pX,
		int pY,
		String groupId,
		int groupIndex
	)
	{
		super(pContainer, pSlot, pX, pY);

		this.groupId = groupId;
		this.groupIndex = groupIndex;
	}

	@Override
	public boolean mayPlace(ItemStack stack)
	{
		if (!(stack.getItem() instanceof CyberneticItem item))
		{
			return false;
		}

		if (!item.slotGroup.equals(this.groupId))
		{
			return false;
		}

		if (item.slotType != CyberneticSlotType.ANY && ((item.slotType == CyberneticSlotType.PRIMARY) != (this.groupIndex == 0)))
		{
			return false;
		}

		ItemStack prevStack = this.getItem();
		this.container.setItem(this.getContainerSlot(), stack);
		boolean isValid = validateContents();
		this.container.setItem(this.getContainerSlot(), prevStack);

		return isValid;
	}

	@Override
	public boolean mayPickup(Player player)
	{
		if (!validateContents())
		{
			return true;
		}

		ItemStack prevStack = this.getItem();
		this.container.setItem(this.getContainerSlot(), ItemStack.EMPTY);
		boolean isValid = validateContents();
		this.container.setItem(this.getContainerSlot(), prevStack);

		return isValid;
	}

	private boolean validateContents()
	{
		for (int i = 0; i < this.container.getContainerSize(); ++i)
		{
			ItemStack stack = this.container.getItem(i);

			if (!(stack.getItem() instanceof CyberneticItem item))
			{
				continue;
			}

			if (!item.validate(stack, this.container))
			{
				return false;
			}
		}

		return true;
	}
}
