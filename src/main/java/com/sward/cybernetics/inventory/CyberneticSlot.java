package com.sward.cybernetics.inventory;

import com.sward.cybernetics.items.CyberneticItem;
import net.minecraft.FieldsAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;

@FieldsAreNonnullByDefault
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CyberneticSlot extends Slot
{
	public TagKey<Item> groupId;
	public int groupIndex;

	public CyberneticSlot(
		Container pContainer,
		int pSlot,
		int pX,
		int pY,
		TagKey<Item> groupId,
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

		if (item.slotTag != this.groupId)
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
