package com.sward.cybernetics.capabilities;

import com.sward.cybernetics.cybernetics.Event;
import com.sward.cybernetics.util.Consumer3;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.*;

import static com.sward.cybernetics.Cybernetics.id;

@AutoRegisterCapability
public interface ICyberneticsInventory extends Container
{
	ResourceLocation KEY = id("cybernetics_inventory");
	Capability<ICyberneticsInventory> CAP = CapabilityManager.get(new CapabilityToken<>() {});

	boolean contains(ItemStack stack);
	boolean contains(TagKey<Item> tag);

	void tick();

	<T> void forEachEvent(Event<T> event, Consumer3<T, LivingEntity, ItemStack> consumer);

	<T, E> void handleEvent(Event<T> event, E forgeEvent, Consumer3<T, ItemStack, E> consumer);

	CompoundTag serializeNBT();

	void deserializeNBT(CompoundTag nbt);
}
