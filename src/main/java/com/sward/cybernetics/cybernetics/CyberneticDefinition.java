package com.sward.cybernetics.cybernetics;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.ListCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.Getter;
import net.minecraft.FieldsAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@FieldsAreNonnullByDefault
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class CyberneticDefinition
{
	public static final Codec<CyberneticDefinition> CODEC = RecordCodecBuilder.create(
		instance -> instance.group(
			Codec.STRING.fieldOf("slot").forGetter(CyberneticDefinition::getSlot),
			new ListCodec<>(ICyberneticComponent.CODEC).fieldOf("components")
				.forGetter(CyberneticDefinition::getComponents)
		).apply(instance, CyberneticDefinition::new)
	);

	@Getter
	private final String slot;

	@Getter
	private final List<ICyberneticComponent> components;

	private final Map<Event<?>, Object> events = new Object2ObjectOpenHashMap<>();

	public CyberneticDefinition(String slot, List<ICyberneticComponent> components)
	{
		this.slot = slot;
		this.components = components;
	}

	public <T> T getEvent(Event<T> event)
	{
		return (T) this.events.computeIfAbsent(event, e -> e.apply(this.components));
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == this)
		{
			return true;
		}

		if (obj == null || obj.getClass() != this.getClass())
		{
			return false;
		}

		CyberneticDefinition that = (CyberneticDefinition) obj;

		return Objects.equals(this.slot, that.slot) &&
			Objects.equals(this.components, that.components);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(this.slot, this.components);
	}

}
