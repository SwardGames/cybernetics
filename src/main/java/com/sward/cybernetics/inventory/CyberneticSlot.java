package com.sward.cybernetics.inventory;

import com.sward.cybernetics.cybernetics.CyberneticDefinition;
import com.sward.cybernetics.cybernetics.events.implanting.CanImplant;
import com.sward.cybernetics.cybernetics.CyberneticDefinitions;
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
	public final Player player;
	public final String type;

	public CyberneticSlot(
		Container container,
		int slot,
		int x,
		int y,
		Player player,
		String type
	)
	{
		super(container, slot, x, y);

		this.player = player;
		this.type = type;
	}

	@Override
	public boolean mayPlace(ItemStack stack)
	{
		CyberneticDefinition definition = CyberneticDefinitions.forItem(stack.getItem());

		if (definition == null)
		{
			return false;
		}

		if (!definition.getSlot().equals(this.type))
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

			CyberneticDefinition definition = CyberneticDefinitions.forItem(stack.getItem());

			if (definition == null)
			{
				continue;
			}

			if (!definition.getEvent(CanImplant.EVENT).canImplant(stack, this.player, this.container))
			{
				return false;
			}
		}

		return true;
	}
}
