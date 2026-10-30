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

	public static final RegistryObject<Attribute> CAPACITY = REGISTER.register(
		"cybernetic_capacity",
		() -> new RangedAttribute("attribute.cybernetics.cybernetic_capacity", 100D, 0D, 1024D).setSyncable(true)
	);

	public static final RegistryObject<Attribute> MOTION_SCANNER_RANGE = REGISTER.register(
		"motion_scanner_range",
		() -> new RangedAttribute("attribute.cybernetics.motion_scanner_range", 0D, 0D, 256D).setSyncable(true)
	);
}
