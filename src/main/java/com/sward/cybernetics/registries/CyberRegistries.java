package com.sward.cybernetics.registries;

import com.mojang.serialization.Codec;
import com.sward.cybernetics.cybernetics.ICyberneticComponent;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraftforge.registries.IForgeRegistry;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CyberRegistries
{
	public static IForgeRegistry<Codec<? extends ICyberneticComponent>> CYBERNETIC_COMPONENT_CODECS;
}
