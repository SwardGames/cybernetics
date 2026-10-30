package com.sward.cybernetics.items;

import com.google.common.collect.ImmutableMultimap;
import com.sward.cybernetics.data.CyberneticSlotType;
import net.minecraft.FieldsAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

@FieldsAreNonnullByDefault
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CyberneticItemBuilder implements Supplier<CyberneticItem>
{
	private final ICyberneticItemFactory factory;

	private final ResourceLocation id;

	private final UUID uuid;

	private final String slotGroup;
	private final CyberneticSlotType slotType;

	@Nullable
	private TagKey<Item> requiredTag;

	@Nullable
	private TagKey<Item> forbiddenTag;

	private int capacityCost = 5;

	private Item.Properties properties = new Item.Properties().stacksTo(1);

	private final List<Pair<Supplier<Attribute>, AttributeModifier>> attributes = new ArrayList<>();

	private final ImmutableMultimap.Builder<String, AttributeModifier> curiosSlots = ImmutableMultimap.builder();

	public CyberneticItemBuilder(ResourceLocation id, String slotGroup, CyberneticSlotType slotType)
	{
		this(CyberneticItem::new, id, slotGroup, slotType);
	}

	public CyberneticItemBuilder(ICyberneticItemFactory factory, ResourceLocation id, String slotGroup, CyberneticSlotType slotType)
	{
		this.factory = factory;
		this.id = id;
		this.uuid = UUID.nameUUIDFromBytes(id.toString().getBytes(StandardCharsets.UTF_8));
		this.slotGroup = slotGroup;
		this.slotType = slotType;
	}

	public CyberneticItemBuilder requiredTag(TagKey<Item> requiredTag)
	{
		this.requiredTag = requiredTag;

		return this;
	}

	public CyberneticItemBuilder forbiddenTag(TagKey<Item> forbiddenTag)
	{
		this.forbiddenTag = forbiddenTag;

		return this;
	}

	public CyberneticItemBuilder capacityCost(int capacityCost)
	{
		this.capacityCost = capacityCost;

		return this;
	}

	public CyberneticItemBuilder attribute(String name, Attribute attribute, double amount)
	{
		return attribute(name, () -> attribute, amount, AttributeModifier.Operation.ADDITION);
	}

	public CyberneticItemBuilder attribute(
		String name,
		Attribute attribute,
		double amount,
		AttributeModifier.Operation operation
	)
	{
		return attribute(name, () -> attribute, amount, operation);
	}

	public CyberneticItemBuilder attribute(String name, Supplier<Attribute> attribute, double amount)
	{
		return attribute(name, attribute, amount, AttributeModifier.Operation.ADDITION);
	}

	public CyberneticItemBuilder attribute(
		String name,
		Supplier<Attribute> attribute,
		double amount,
		AttributeModifier.Operation operation
	)
	{
		this.attributes.add(Pair.of(attribute, new AttributeModifier(this.uuid, name, amount, operation)));

		return this;
	}

	public CyberneticItemBuilder curiosSlot(
		String name,
		String curiosSlot,
		int amount
	)
	{
		return curiosSlot(name, curiosSlot, amount, AttributeModifier.Operation.ADDITION);
	}

	public CyberneticItemBuilder curiosSlot(
		String name,
		String curiosSlot,
		double amount,
		AttributeModifier.Operation operation
	)
	{
		this.curiosSlots.put(curiosSlot, new AttributeModifier(this.uuid, name, amount, operation));

		return this;
	}

	public CyberneticItemBuilder itemProperties(Item.Properties properties)
	{
		this.properties = properties;

		return this;
	}

	@Override
	public CyberneticItem get()
	{
		ImmutableMultimap.Builder<Attribute, AttributeModifier> attributesMapBuilder = ImmutableMultimap.builder();

		for (Pair<Supplier<Attribute>, AttributeModifier> attribute : this.attributes)
		{
			attributesMapBuilder.put(attribute.getKey().get(), attribute.getValue());
		}

		return factory.apply(
			this.slotGroup,
			this.slotType,
			this.requiredTag,
			this.forbiddenTag,
			this.capacityCost,
			attributesMapBuilder.build(),
			this.curiosSlots.build(),
			this.properties
		);
	}

	public RegistryObject<CyberneticItem> register(DeferredRegister<Item> register)
	{
		return register.register(this.id.getPath(), this);
	}
}
