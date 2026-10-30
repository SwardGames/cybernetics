package com.sward.cybernetics.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.*;

import static com.sward.cybernetics.Cybernetics.id;

@AutoRegisterCapability
public interface ICyberneticsInventory extends Container
{
	ResourceLocation KEY = id("cybernetics_inventory");
	Capability<ICyberneticsInventory> CAP = CapabilityManager.get(new CapabilityToken<>() {});

	void tick();

	CompoundTag serializeNBT();

	void deserializeNBT(CompoundTag nbt);
}
