package com.sward.cybernetics.inventory;

import com.mojang.datafixers.util.Function3;
import com.sward.cybernetics.config.CyberConfig;
import com.sward.cybernetics.config.CyberSlotType;
import com.sward.cybernetics.config.CyberSlotGroup;
import com.sward.cybernetics.cybernetics.CyberneticDefinition;
import com.sward.cybernetics.cybernetics.Event;
import com.sward.cybernetics.cybernetics.events.behaviour.OnTick;
import com.sward.cybernetics.cybernetics.events.implanting.OnImplanted;
import com.sward.cybernetics.cybernetics.events.implanting.OnUnimplanted;
import com.sward.cybernetics.capabilities.ICyberneticsInventory;
import com.sward.cybernetics.cybernetics.CyberneticDefinitions;
import com.sward.cybernetics.util.Consumer3;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.Getter;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedList;
import net.minecraft.FieldsAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.tags.TagKey;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.function.Consumer;

@FieldsAreNonnullByDefault
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CyberneticsInventory implements ICyberneticsInventory
{
	public final List<NonNullList<ItemStack>> compartments = new ObjectArrayList<>();
	public final Object2IntMap<String> slotCompartmentIndices = new Object2IntOpenHashMap<>();
	public final int containerSize;
	public final Player player;

	@Getter
	private int timesChanged;

	private final NonNullList<ItemStack> previousStacks;

	public CyberneticsInventory(Player player)
	{
		int containerSize = 0;
		int slotType = 0;

		for (CyberSlotGroup group : CyberConfig.SLOTS.groups)
		{
			for (CyberSlotType type : group.slotTypes)
			{
				this.compartments.add(NonNullList.withSize(type.count.get(), ItemStack.EMPTY));
				this.slotCompartmentIndices.put(type.id.get(), slotType);

				containerSize += type.count.get();
				++slotType;
			}
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
						CyberneticDefinition prevItemDefinition = CyberneticDefinitions.forItem(prevStack.getItem());

						if (prevItemDefinition != null)
						{
							prevItemDefinition.getEvent(OnUnimplanted.EVENT).unimplanted(stack, this.player);
						}

						CyberneticDefinition newItemDefinition = CyberneticDefinitions.forItem(stack.getItem());

						if (newItemDefinition != null)
						{
							newItemDefinition.getEvent(OnImplanted.EVENT).implanted(stack, this.player);
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

	@Override
	public boolean contains(ItemStack stack)
	{
		for (List<ItemStack> items : this.compartments)
		{
			for (ItemStack stack1 : items)
			{
				if (!stack1.isEmpty() && ItemStack.isSameItemSameTags(stack1, stack))
				{
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public boolean contains(TagKey<Item> tag)
	{
		for (List<ItemStack> items : this.compartments)
		{
			for (ItemStack stack : items)
			{
				if (!stack.isEmpty() && stack.is(tag))
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
				CyberneticDefinition definition = CyberneticDefinitions.forItem(stack.getItem());

				if (definition != null)
				{
					definition.getEvent(OnTick.EVENT).tick(stack, this.player);
				}
			}
		}
	}

	@Override
	public <T> void forEachEvent(Event<T> event, Consumer3<T, LivingEntity, ItemStack> consumer)
	{
		for (List<ItemStack> items : this.compartments)
		{
			for (ItemStack stack : items)
			{
				CyberneticDefinition definition = CyberneticDefinitions.forItem(stack.getItem());

				if (definition != null)
				{
					consumer.accept(definition.getEvent(event), this.player, stack);
				}
			}
		}
	}

	@Override
	public <T, E> void handleEvent(Event<T> event, E forgeEvent, Consumer3<T, ItemStack, E> consumer)
	{
		for (List<ItemStack> items : this.compartments)
		{
			for (ItemStack stack : items)
			{
				CyberneticDefinition definition = CyberneticDefinitions.forItem(stack.getItem());

				if (definition != null)
				{
					consumer.accept(definition.getEvent(event), stack, forgeEvent);
				}
			}
		}
	}

	@Override
	public CompoundTag serializeNBT()
	{
		CompoundTag result = new CompoundTag();

		for (Object2IntMap.Entry<String> group : this.slotCompartmentIndices.object2IntEntrySet())
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
			if (!this.slotCompartmentIndices.containsKey(key))
			{
				continue;
			}

			NonNullList<ItemStack> items = this.compartments.get(this.slotCompartmentIndices.getInt(key));

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
