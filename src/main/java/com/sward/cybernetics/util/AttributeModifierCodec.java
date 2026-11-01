package com.sward.cybernetics.util;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AttributeModifierCodec
{
	private static final BiMap<String, AttributeModifier.Operation> NAME2OP = ImmutableBiMap.of(
		"addition", AttributeModifier.Operation.ADDITION,
		"multiply_base", AttributeModifier.Operation.MULTIPLY_BASE,
		"multiply", AttributeModifier.Operation.MULTIPLY_TOTAL
	);

	private static final BiMap<AttributeModifier.Operation, String> OP2NAME = NAME2OP.inverse();

	public static final Codec<AttributeModifier.Operation> OPERATION = Codec.STRING.comapFlatMap(
		name ->
		{
			AttributeModifier.Operation operation = NAME2OP.get(name);

			return operation == null
				? DataResult.error(() -> "Unknown attribute modifier operation: " + name)
				: DataResult.success(operation);
		},
		OP2NAME::get
	);

	public static final Codec<AttributeModifier> INSTANCE = RecordCodecBuilder.create(
		instance -> instance.group(
			Codec.STRING.optionalFieldOf("name", "cybernetics.codec").forGetter(AttributeModifier::getName),
			Codec.DOUBLE.fieldOf("amount").forGetter(AttributeModifier::getAmount),
			OPERATION.fieldOf("operation").forGetter(AttributeModifier::getOperation)
		).apply(instance, AttributeModifier::new)
	);
}
