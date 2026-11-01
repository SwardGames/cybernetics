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
	public static final RegistryObject<Item> CYBER_EYES = cybernetic("cyber_eyes");

	public static final RegistryObject<Item> TARGET_SCANNER = cybernetic("target_scanner");

	// Arms
	public static final RegistryObject<Item> EXTRA_FINGER = cybernetic("extra_finger");

	// Augments
	public static final RegistryObject<Item> SUBDERMAL_ARMOR = cybernetic("subdermal_armor");

	// Augments / Bones
	public static final RegistryObject<Item> HOLLOW_BONES = cybernetic("hollow_bones");
	public static final RegistryObject<Item> TITANIUM_BONES = cybernetic("titanium_bones");

	private static RegistryObject<Item> cybernetic(String name)
	{
		return REGISTER.register(name, () -> new Item(new Item.Properties().stacksTo(1)));
	}
}
