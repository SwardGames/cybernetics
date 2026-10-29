package com.sward.cybernetics.items;

import com.sward.cybernetics.blocks.CyberBlocks;
import com.sward.cybernetics.data.CyberneticSlotType;
import com.sward.cybernetics.registries.DyedRegistryObjects;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.BedItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.sward.cybernetics.Cybernetics.ID;
import static com.sward.cybernetics.Cybernetics.id;
import static com.sward.cybernetics.data.CyberneticSlotType.*;
import static com.sward.cybernetics.items.CyberneticTypeTags.*;
import static com.sward.cybernetics.data.SlotGroups.*;
import static net.minecraft.world.entity.ai.attributes.Attributes.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CyberItems
{
	public static final DeferredRegister<Item> REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, ID);

	public static final DyedRegistryObjects<Item> OPERATING_TABLES = new DyedRegistryObjects<>(
		REGISTER, "operating_table", c -> new BedItem(
		CyberBlocks.OPERATING_TABLES.apply(c), new Item.Properties().stacksTo(1))
	);

	// Brain
	public static final RegistryObject<CyberneticItem> NEUTRAL_COPROCESSOR = cybernetic("neural_coprocessor", BRAIN, PRIMARY)
		.capacityCost(-15)
		.register(REGISTER);

	// Eyes
	public static final RegistryObject<CyberneticItem> CYBER_EYES = cybernetic("cyber_eyes", EYES, PRIMARY)
		.capacityCost(4)
		.register(REGISTER);

	public static final RegistryObject<CyberneticItem> TARGET_SCANNER = cybernetic("target_scanner", EYES, SECONDARY)
		.capacityCost(9)
		.requiredTag(EYE)
		.register(REGISTER);

	// Augments
	public static final RegistryObject<CyberneticItem> SUBDERMAL_ARMOR = augment("subdermal_armor")
		.capacityCost(10)
		.attribute("Armor modifier", ARMOR, 4)
		.register(REGISTER);

	// Augments / Bones
	public static final RegistryObject<CyberneticItem> HOLLOW_BONES = augment("hollow_bones")
		.capacityCost(14)
		.forbiddenTag(BONE)
		.attribute("Movement speed modifier", MOVEMENT_SPEED, 0.15D, AttributeModifier.Operation.MULTIPLY_TOTAL)
		.attribute("Armor modifier", ARMOR, -0.1D, AttributeModifier.Operation.MULTIPLY_TOTAL)
		.attribute("Knockback modifier", KNOCKBACK_RESISTANCE, -0.1, AttributeModifier.Operation.ADDITION)
		.register(REGISTER);

	public static final RegistryObject<CyberneticItem> TITANIUM_BONES = augment("titanium_bones")
		.capacityCost(18)
		.forbiddenTag(BONE)
		.attribute("Max health modifier", MAX_HEALTH, 6, AttributeModifier.Operation.ADDITION)
		.attribute("Knockback modifier", KNOCKBACK_RESISTANCE, 0.1, AttributeModifier.Operation.ADDITION)
		.register(REGISTER);

	private static CyberneticItemBuilder cybernetic(String name, String slotGroup, CyberneticSlotType slotType)
	{
		return new CyberneticItemBuilder(id(name), slotGroup, slotType);
	}

	private static CyberneticItemBuilder augment(String name)
	{
		return new CyberneticItemBuilder(id(name), AUGMENTS, ANY);
	}
}
