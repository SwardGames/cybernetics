package com.sward.cybernetics.registries;

import net.minecraft.world.item.DyeColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Function;

public class DyedRegistryObjects<T> implements Function<DyeColor, T>
{
	private final RegistryObject[] registryObjects;

	public DyedRegistryObjects(DeferredRegister<? super T> register, String name, Function<DyeColor, T> func)
	{
		DyeColor[] colors = DyeColor.values();

		this.registryObjects = new RegistryObject[colors.length];

		for (int i = 0; i < colors.length; ++i)
		{
			final DyeColor color = colors[i];

			this.registryObjects[i] = register.register(color.getSerializedName() + '_' + name, () -> func.apply(color));
		}
	}

	@Override
	public T apply(DyeColor dyeColor)
	{
		//noinspection unchecked
		return (T) this.registryObjects[dyeColor.ordinal()].get();
	}
}
