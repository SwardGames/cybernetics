package com.sward.cybernetics.cybernetics;

import com.mojang.serialization.Codec;
import com.sward.cybernetics.registries.CyberRegistries;
import net.minecraft.FieldsAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.util.ExtraCodecs;

import javax.annotation.ParametersAreNonnullByDefault;

@FieldsAreNonnullByDefault
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public interface ICyberneticComponent
{
	Codec<ICyberneticComponent> CODEC = ExtraCodecs.lazyInitializedCodec(
		() -> CyberRegistries.CYBERNETIC_COMPONENT_CODECS
			.getCodec()
			.dispatch("type", ICyberneticComponent::codec, codec -> codec)
	);

	Codec<? extends ICyberneticComponent> codec();
}
