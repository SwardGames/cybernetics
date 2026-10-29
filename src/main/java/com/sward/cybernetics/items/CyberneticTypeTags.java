package com.sward.cybernetics.items;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import static com.sward.cybernetics.Cybernetics.id;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CyberneticTypeTags
{
	public static final TagKey<Item> COPROCESSOR = ItemTags.create(id("cybernetic_type/coprocessor"));
	public static final TagKey<Item> EYE = ItemTags.create(id("cybernetic_type/eye"));
	public static final TagKey<Item> BONE = ItemTags.create(id("cybernetic_type/bone"));
}
