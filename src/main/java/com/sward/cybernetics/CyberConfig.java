package com.sward.cybernetics;

import com.electronwill.nightconfig.core.Config;
import com.google.common.base.Predicates;
import com.sward.cybernetics.data.CyberneticsSlotDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import java.util.List;

import static com.sward.cybernetics.Cybernetics.ID;
import static com.sward.cybernetics.Cybernetics.id;

@Mod.EventBusSubscriber(modid = ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CyberConfig
{
	public static class Server
	{
		private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

		private static final ForgeConfigSpec.ConfigValue<List<? extends String>> SLOT_DEFINITIONS = BUILDER
			.comment(
				"The cybernetic slots available to the player.",
				"The format of a slot is 'id slot_count side'.",
				"'id' must be a valid resource location path and will determine the tag needed for a cybernetic component to be placed into that slot.",
				"'slot_count' is the number of slots and must be a positive integer.",
				"'side' is the side of the operating table the slots appear on and can be 'left' or 'right'."
			)
			.worldRestart()
			.defineList(
				"cybernetic_slots",
				List.of(
					toConfig(new CyberneticsSlotDefinition(ItemTags.create(id("brain")), 3, true)),
					toConfig(new CyberneticsSlotDefinition(ItemTags.create(id("eyes")), 3, true)),
					toConfig(new CyberneticsSlotDefinition(ItemTags.create(id("spine")), 3, false)),
					toConfig(new CyberneticsSlotDefinition(ItemTags.create(id("arms")), 3, false)),
					toConfig(new CyberneticsSlotDefinition(ItemTags.create(id("legs")), 3, false)),
					toConfig(new CyberneticsSlotDefinition(ItemTags.create(id("augments")), 5, true))
				),
				e -> e instanceof String s && fromConfig(s) != null
			);

		private static List<CyberneticsSlotDefinition> SLOT_DEFINITIONS_VALUE;

		static final ForgeConfigSpec SPEC = BUILDER.build();

		public static List<CyberneticsSlotDefinition> getSlotDefinitions()
		{
			return SLOT_DEFINITIONS_VALUE;
		}
	}

	@SubscribeEvent
	static void onLoad(final ModConfigEvent event)
	{
		Server.SLOT_DEFINITIONS_VALUE = Server.SLOT_DEFINITIONS.get().stream().map(CyberConfig::fromConfig).toList();
	}

	private static CyberneticsSlotDefinition fromConfig(String config)
	{
		String[] split = config.split(" ");

		if (split.length != 3)
		{
			return null;
		}

		String id = split[0];

		if (id.isEmpty() || !ResourceLocation.isValidPath(id))
		{
			return null;
		}

		int slotCount;

		try
		{
			slotCount = Integer.parseInt(split[1]);
		}
		catch (Exception e)
		{
			return null;
		}

		boolean onRight;

		switch (split[2])
		{
			case "left":
			{
				onRight = false;
				break;
			}
			case "right":
			{
				onRight = true;
				break;
			}
			default:
			{
				return null;
			}
		}

		return new CyberneticsSlotDefinition(ItemTags.create(id(id)), slotCount, onRight);
	}

	private static String toConfig(CyberneticsSlotDefinition slot)
	{
		return slot.id().location().getPath() + " " + slot.slotCount() + " " + (slot.onRight() ? "right" : "left");
	}
}
