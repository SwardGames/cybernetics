package com.sward.cybernetics.items;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static com.sward.cybernetics.Cybernetics.ID;
import static net.minecraft.core.registries.Registries.CREATIVE_MODE_TAB;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CyberCreativeTabs
{
	public static final DeferredRegister<CreativeModeTab> REGISTER = DeferredRegister.create(CREATIVE_MODE_TAB, ID);

	public static final RegistryObject<CreativeModeTab> MAIN = REGISTER.register(
		"main", () -> CreativeModeTab.builder()
			.title(Component.translatable("item_group.cybernetics"))
			.icon(() -> new ItemStack(CyberItems.OPERATING_TABLES.apply(DyeColor.WHITE)))
			.displayItems(
				(parameters, output) -> CyberItems.REGISTER.getEntries().forEach(item -> output.accept(item.get()))
			)
			.build()
	);
}
