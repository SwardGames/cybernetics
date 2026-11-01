package com.sward.cybernetics.cybernetics;

import com.sward.cybernetics.Cybernetics;
import com.sward.cybernetics.registries.CyberRegistryKeys;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

import static com.sward.cybernetics.Cybernetics.ID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Mod.EventBusSubscriber(modid = ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class CyberneticDefinitions
{
	private static final Map<Item, CyberneticDefinition> MAP = new HashMap<>();

	public static CyberneticDefinition forItem(@Nullable Item item)
	{
		return MAP.get(item);
	}

	@SubscribeEvent
	public static void serverAboutToStart(ServerAboutToStartEvent e)
	{
		MAP.clear();

		e.getServer()
			.registryAccess()
			.registryOrThrow(CyberRegistryKeys.CYBERNETIC_DEFINITIONS)
			.entrySet()
			.forEach(
				entry -> {
					ResourceLocation itemId = entry.getKey().location();

					if (!ForgeRegistries.ITEMS.containsKey(itemId))
					{
						Cybernetics.LOGGER.error("No item found with id '{}'", itemId);

						return;
					}

					MAP.put(
						ForgeRegistries.ITEMS.getValue(itemId),
						entry.getValue()
					);
				}
			);
	}
}
