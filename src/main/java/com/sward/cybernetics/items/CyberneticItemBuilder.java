package com.sward.cybernetics.items;

import com.google.common.collect.ImmutableMultimap;
import net.minecraft.FieldsAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.nio.charset.StandardCharsets;
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

	private final TagKey<Item> slotTag;

	@Nullable
	private TagKey<Item> requiredTag;

	@Nullable
	private TagKey<Item> forbiddenTag;

	private int capacityCost = 5;

	private Item.Properties properties = new Item.Properties().stacksTo(1);

	private final ImmutableMultimap.Builder<Attribute, AttributeModifier> attributesMapBuilder = ImmutableMultimap.builder();

	public CyberneticItemBuilder(ResourceLocation id, TagKey<Item> slotTag)
	{
		this(CyberneticItem::new, id, slotTag);
	}

	public CyberneticItemBuilder(ICyberneticItemFactory factory, ResourceLocation id, TagKey<Item> slotTag)
	{
		this.factory = factory;
		this.id = id;
		this.uuid = UUID.nameUUIDFromBytes(id.toString().getBytes(StandardCharsets.UTF_8));
		this.slotTag = slotTag;
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
		return attribute(name, attribute, amount, AttributeModifier.Operation.ADDITION);
	}

	public CyberneticItemBuilder attribute(
		String name, Attribute attribute,
		double amount,
		AttributeModifier.Operation operation
	)
	{
		this.attributesMapBuilder.put(attribute, new AttributeModifier(this.uuid, name, amount, operation));

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
		return factory.apply(
			this.uuid,
			this.slotTag,
			this.requiredTag,
			this.forbiddenTag,
			this.capacityCost,
			this.attributesMapBuilder.build(),
			this.properties
		);
	}

	public RegistryObject<CyberneticItem> register(DeferredRegister<Item> register)
	{
		return register.register(this.id.getPath(), this);
	}
}
