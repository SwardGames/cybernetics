package com.sward.cybernetics.registries;

import com.mojang.serialization.Codec;
import com.sward.cybernetics.cybernetics.CyberneticDefinition;
import com.sward.cybernetics.cybernetics.ICyberneticComponent;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import static com.sward.cybernetics.Cybernetics.id;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CyberRegistryKeys
{
	public static final ResourceKey<Registry<Codec<? extends ICyberneticComponent>>> CYBERNETIC_COMPONENT_CODECS =
		ResourceKey.createRegistryKey(id("cybernetic_component_codecs"));

	public static final ResourceKey<Registry<CyberneticDefinition>> CYBERNETIC_DEFINITIONS =
		ResourceKey.createRegistryKey(id("cybernetic_definitions"));
}
