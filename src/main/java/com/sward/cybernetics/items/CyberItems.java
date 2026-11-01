package com.sward.cybernetics.items;

import com.sward.cybernetics.blocks.CyberBlocks;
import com.sward.cybernetics.registries.DyedRegistryObjects;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.world.item.BedItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.sward.cybernetics.Cybernetics.ID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CyberItems
{
	public static final DeferredRegister<Item> REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, ID);

	public static final DyedRegistryObjects<Item> OPERATING_TABLES = new DyedRegistryObjects<>(
		REGISTER, "operating_table", c -> new BedItem(
		CyberBlocks.OPERATING_TABLES.apply(c), new Item.Properties().stacksTo(1))
	);

	// Brain
	public static final RegistryObject<Item> NEUTRAL_COPROCESSOR = cybernetic("neural_coprocessor");

	// Eyes
	public static final RegistryObject<Item> BASIC_CYBER_EYES = cybernetic("basic_cyber_eyes");
	public static final RegistryObject<Item> STALKER_CYBER_EYES = cybernetic("stalker_cyber_eyes");
	public static final RegistryObject<Item> FARSIGHT_CYBER_EYES = cybernetic("farsight_cyber_eyes");

	// Nervous System
	public static final RegistryObject<Item> REFLEX_BYPASS = cybernetic("reflex_bypass");
	public static final RegistryObject<Item> SHINOBI_MODULE = cybernetic("shinobi_module");
	public static final RegistryObject<Item> MNEMONIC_TRACKER = cybernetic("mnemonic_tracker");

	// Arms

	// Hands
	public static final RegistryObject<Item> EXTRA_FINGER = cybernetic("extra_finger");

	// Legs

	// Feet
	public static final RegistryObject<Item> PADDED_SOLES = cybernetic("padded_soles");

	// Heart
	public static final RegistryObject<Item> RESUSCITATOR = cybernetic("resuscitator");

	// Circulatory
	public static final RegistryObject<Item> BLOOD_FILTER = cybernetic("blood_filter");

	// Spine

	// Skeleton
	public static final RegistryObject<Item> HOLLOW_BONES = cybernetic("hollow_bones");
	public static final RegistryObject<Item> TITANIUM_BONES = cybernetic("titanium_bones");
	public static final RegistryObject<Item> TORTOISESHELL = cybernetic("tortoiseshell");

	// Muscle
	public static final RegistryObject<Item> SUBDERMAL_ARMOR = cybernetic("subdermal_armor");

	// Visceral

	private static RegistryObject<Item> cybernetic(String name)
	{
		return REGISTER.register(name, () -> new Item(new Item.Properties().stacksTo(1)));
	}
}
