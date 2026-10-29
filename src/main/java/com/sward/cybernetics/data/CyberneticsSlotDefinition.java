package com.sward.cybernetics.data;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public record CyberneticsSlotDefinition(TagKey<Item> id, int slotCount, boolean onRight)
{

}
