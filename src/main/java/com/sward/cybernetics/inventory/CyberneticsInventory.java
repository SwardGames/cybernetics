package com.sward.cybernetics.inventory;

import com.sward.cybernetics.CyberConfig;
import com.sward.cybernetics.capabilities.ICyberneticsInventory;
import com.sward.cybernetics.data.CyberneticsSlotDefinition;
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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

import static com.sward.cybernetics.Cybernetics.id;

@FieldsAreNonnullByDefault
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CyberneticsInventory implements ICyberneticsInventory
{
	public final List<NonNullList<ItemStack>> compartments = new ObjectArrayList<>();
	public final Object2IntMap<TagKey<Item>> groupIndices = new Object2IntOpenHashMap<>();
	public final int containerSize;
	public final Player player;

	@Getter
	private int timesChanged;

	public CyberneticsInventory(Player player)
	{
		int containerSize = 0;

		List<CyberneticsSlotDefinition> slotDefinitions = CyberConfig.Server.getSlotDefinitions();

		for (int i = 0; i < slotDefinitions.size(); i++)
		{
			CyberneticsSlotDefinition definition = slotDefinitions.get(i);

			this.compartments.add(NonNullList.withSize(definition.slotCount(), ItemStack.EMPTY));

			this.groupIndices.put(definition.id(), i);

			containerSize += definition.slotCount();
		}

		this.containerSize = containerSize;
		this.player = player;
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
			for (ItemStack itemstack : items)
			{
				if (!itemstack.isEmpty() && ItemStack.isSameItemSameTags(itemstack, pStack))
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
			for (ItemStack itemstack : items)
			{
				if (!itemstack.isEmpty() && itemstack.is(pTag))
				{
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public CompoundTag serializeNBT()
	{
		CompoundTag result = new CompoundTag();

		for (Object2IntMap.Entry<TagKey<Item>> group : this.groupIndices.object2IntEntrySet())
		{
			ListTag list = new ListTag();

			NonNullList<ItemStack> items = this.compartments.get(group.getIntValue());

			for (ItemStack stack : items)
			{
				list.add(ItemStack.CODEC.encodeStart(NbtOps.INSTANCE, stack).get().orThrow());
			}

			result.put(group.getKey().location().getPath(), list);
		}

		return result;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt)
	{
		for (String key : nbt.getAllKeys())
		{
			TagKey<Item> id = TagKey.create(ForgeRegistries.ITEMS.getRegistryKey(), id(key));

			if (!this.groupIndices.containsKey(id))
			{
				continue;
			}

			NonNullList<ItemStack> items = this.compartments.get(this.groupIndices.getInt(id));

			ListTag listTag = nbt.getList(key, ListTag.TAG_COMPOUND);

			int count = Math.min(listTag.size(), items.size());

			for (int i = 0; i < count; ++i)
			{
				items.set(i, ItemStack.CODEC.parse(NbtOps.INSTANCE, listTag.get(i)).get().orThrow());
			}
		}
	}
}
