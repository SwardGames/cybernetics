package com.sward.cybernetics.attributes;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.sward.cybernetics.Cybernetics.ID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CyberAttributes
{
	public static final DeferredRegister<Attribute> REGISTER = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, ID);

	public static final RegistryObject<Attribute> CAPACITY = register("cybernetic_capacity", 100D, 1D, 1024D);

	public static final RegistryObject<Attribute> LOAD = register("cybernetic_load", 0D, 0D, 1024D);

	public static final RegistryObject<Attribute> MOTION_TRACKER_RANGE = register("motion_tracker_range", 0D, 0D, 256D);

	public static final RegistryObject<Attribute> TRACKING_DURATION = register("tracking_duration", 0D, 0D, 60D);

	public static final RegistryObject<Attribute> MAX_ZOOM = register("max_zoom", 0D, 0D, 60D);

	public static final RegistryObject<Attribute> DODGE_CHANCE = register("dodge_chance", 0D, 0D, 1D);

	public static final RegistryObject<Attribute> CATCH_ARROW_CHANCE = register("catch_arrow_chance", 0D, 0D, 1D);

	public static final RegistryObject<Attribute> POSITIVE_EFFECT_DURATION = register("positive_effect_duration", 1D, 0D, 5D);

	public static final RegistryObject<Attribute> NEGATIVE_EFFECT_DURATION = register("negative_effect_duration", 1D, 0D, 5D);

	private static RegistryObject<Attribute> register(String name, double defaultVal, double min, double max)
	{
		return REGISTER.register(
			name,
			() -> new RangedAttribute("attribute.cybernetics." + name, defaultVal, min, max).setSyncable(true)
		);
	}
}
