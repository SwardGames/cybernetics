package com.sward.cybernetics.items;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import static com.sward.cybernetics.Cybernetics.id;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SlotTags
{
	public static final TagKey<Item> BRAIN = ItemTags.create(id("brain"));
	public static final TagKey<Item> EYES = ItemTags.create(id("eyes"));
	public static final TagKey<Item> SPINE = ItemTags.create(id("spine"));
	public static final TagKey<Item> ARMS = ItemTags.create(id("arms"));
	public static final TagKey<Item> LEGS = ItemTags.create(id("legs"));
	public static final TagKey<Item> AUGMENTS = ItemTags.create(id("augments"));
}
