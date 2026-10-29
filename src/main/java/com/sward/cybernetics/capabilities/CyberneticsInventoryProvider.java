package com.sward.cybernetics.capabilities;

import com.sward.cybernetics.inventory.CyberneticsInventory;
import net.minecraft.FieldsAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@FieldsAreNonnullByDefault
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CyberneticsInventoryProvider implements ICapabilitySerializable<CompoundTag>
{
	private final LazyOptional<ICyberneticsInventory> instanceLazyOptional;

	public CyberneticsInventoryProvider(Player player)
	{
		this.instanceLazyOptional = LazyOptional.of(() -> new CyberneticsInventory(player));
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side)
	{
		if (cap == ICyberneticsInventory.CAP)
		{
			return this.instanceLazyOptional.cast();
		}

		return LazyOptional.empty();
	}

	@Override
	public CompoundTag serializeNBT()
	{
		return this.instanceLazyOptional.resolve().orElseThrow().serializeNBT();
	}

	@Override
	public void deserializeNBT(CompoundTag nbt)
	{
		this.instanceLazyOptional.resolve().orElseThrow().deserializeNBT(nbt);
	}
}
