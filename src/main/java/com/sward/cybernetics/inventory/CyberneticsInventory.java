package com.sward.cybernetics.inventory;

import com.sward.cybernetics.CyberConfig;
import com.sward.cybernetics.capabilities.ICyberneticsInventory;
import com.sward.cybernetics.data.CyberneticsSlotGroup;
import com.sward.cybernetics.items.CyberneticItem;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.Getter;
import net.minecraft.FieldsAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.tags.TagKey;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Map;

@FieldsAreNonnullByDefault
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CyberneticsInventory implements ICyberneticsInventory
{
	public final List<NonNullList<ItemStack>> compartments = new ObjectArrayList<>();
	public final Object2IntMap<String> groupIndices = new Object2IntOpenHashMap<>();
	public final int containerSize;
	public final Player player;

	@Getter
	private int timesChanged;

	private final NonNullList<ItemStack> previousStacks;

	public CyberneticsInventory(Player player)
	{
		int containerSize = 0;

		List<CyberneticsSlotGroup> groups = CyberConfig.Server.getSlotGroups();

		for (int i = 0; i < groups.size(); i++)
		{
			CyberneticsSlotGroup group = groups.get(i);

			this.compartments.add(NonNullList.withSize(group.slotCount(), ItemStack.EMPTY));

			this.groupIndices.put(group.id(), i);

			containerSize += group.slotCount();
		}

		this.containerSize = containerSize;
		this.player = player;

		this.previousStacks = NonNullList.withSize(containerSize, ItemStack.EMPTY);
	}

	@Override
	public int getContainerSize()
	{
		return this.containerSize;
	}

	@Override
	public boolean isEmpty()
	{
		for (NonNullList<ItemStack> items : this.compartments)
		{
			for (ItemStack itemstack : items)
			{
				if (!itemstack.isEmpty())
				{
					return false;
				}
			}
		}

		return true;
	}

	@Override
	public ItemStack getItem(int slot)
	{
		for (NonNullList<ItemStack> items : this.compartments)
		{
			if (slot < items.size())
			{
				return items.get(slot);
			}

			slot -= items.size();
		}

		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack removeItem(int slot, int amount)
	{
		for (NonNullList<ItemStack> items : this.compartments)
		{
			if (slot < items.size())
			{
				return items.get(slot).isEmpty() ? ItemStack.EMPTY : ContainerHelper.removeItem(items, slot, amount);
			}

			slot -= items.size();
		}

		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack removeItemNoUpdate(int slot)
	{
		for (NonNullList<ItemStack> items : this.compartments)
		{
			if (slot < items.size())
			{
				ItemStack itemstack = items.get(slot);
				items.set(slot, ItemStack.EMPTY);
				return itemstack;
			}

			slot -= items.size();
		}

		return ItemStack.EMPTY;
	}

	@Override
	public void setItem(int slot, ItemStack stack)
	{
		for (NonNullList<ItemStack> items : this.compartments)
		{
			if (slot < items.size())
			{
				items.set(slot, stack);

				return;
			}

			slot -= items.size();
		}
	}

	@Override
	public void setChanged()
	{
		++this.timesChanged;

		if (!this.player.level().isClientSide)
		{
			int i = 0;

			for (NonNullList<ItemStack> items : this.compartments)
			{
				for (ItemStack stack : items)
				{
					ItemStack prevStack = this.previousStacks.get(i);

					if (!ItemStack.isSameItem(prevStack, stack))
					{
						if (prevStack.getItem() instanceof CyberneticItem prevItem)
						{
							prevItem.onUnequipped(this.player);
						}

						if (stack.getItem() instanceof CyberneticItem newItem)
						{
							newItem.onEquipped(this.player);
						}

						this.previousStacks.set(i, stack.copy());
					}

					++i;
				}
			}
		}
	}

	@Override
	public boolean stillValid(Player player)
	{
		return !this.player.isRemoved();
	}

	@Override
	public void clearContent()
	{
		for (List<ItemStack> items : this.compartments)
		{
			items.clear();
		}
	}

	public boolean contains(ItemStack pStack)
	{
		for (List<ItemStack> items : this.compartments)
		{
			for (ItemStack stack : items)
			{
				if (!stack.isEmpty() && ItemStack.isSameItemSameTags(stack, pStack))
				{
					return true;
				}
			}
		}

		return false;
	}

	public boolean contains(TagKey<Item> pTag)
	{
		for (List<ItemStack> items : this.compartments)
		{
			for (ItemStack stack : items)
			{
				if (!stack.isEmpty() && stack.is(pTag))
				{
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public void tick()
	{
		for (List<ItemStack> items : this.compartments)
		{
			for (ItemStack stack : items)
			{
				if (stack.getItem() instanceof CyberneticItem cyberneticItem)
				{
					cyberneticItem.onEquippedTick(this.player.level(), this.player);
				}
			}
		}
	}

	@Override
	public CompoundTag serializeNBT()
	{
		CompoundTag result = new CompoundTag();

		for (Object2IntMap.Entry<String> group : this.groupIndices.object2IntEntrySet())
		{
			ListTag list = new ListTag();

			NonNullList<ItemStack> items = this.compartments.get(group.getIntValue());

			for (ItemStack stack : items)
			{
				list.add(ItemStack.CODEC.encodeStart(NbtOps.INSTANCE, stack).get().orThrow());
			}

			result.put(group.getKey(), list);
		}

		return result;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt)
	{
		for (String key : nbt.getAllKeys())
		{
			if (!this.groupIndices.containsKey(key))
			{
				continue;
			}

			NonNullList<ItemStack> items = this.compartments.get(this.groupIndices.getInt(key));

			ListTag listTag = nbt.getList(key, ListTag.TAG_COMPOUND);

			int count = Math.min(listTag.size(), items.size());

			for (int i = 0; i < count; ++i)
			{
				items.set(i, ItemStack.CODEC.parse(NbtOps.INSTANCE, listTag.get(i)).get().orThrow());
			}
		}

		this.setChanged();
	}
}
