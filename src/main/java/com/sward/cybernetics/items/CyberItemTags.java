package com.sward.cybernetics.items;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import static com.sward.cybernetics.Cybernetics.id;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CyberItemTags
{
	public static TagKey<Item> SILENCES_FOOTSTEPS = ItemTags.create(id("silences_footsteps"));
}
