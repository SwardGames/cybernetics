package com.sward.cybernetics.inventory;

import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static com.sward.cybernetics.Cybernetics.ID;
import static net.minecraftforge.registries.ForgeRegistries.MENU_TYPES;

public class CyberMenuTypes
{
	public static final DeferredRegister<MenuType<?>> REGISTER = DeferredRegister.create(MENU_TYPES, ID);

	public static final RegistryObject<MenuType<OperatingTableMenu>> OPERATING_TABLE = REGISTER.register(
		"operating_table", () -> new MenuType<>(
			OperatingTableMenu::new,
			FeatureFlags.DEFAULT_FLAGS
		)
	);
}
