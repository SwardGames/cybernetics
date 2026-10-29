package com.sward.cybernetics;

import com.google.common.collect.ImmutableList;
import com.sward.cybernetics.data.CyberneticsSlotGroup;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.sward.cybernetics.Cybernetics.ID;

@Mod.EventBusSubscriber(modid = ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CyberConfig
{
	public static class Server
	{
		private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

		private static final ForgeConfigSpec.ConfigValue<List<? extends String>> SLOT_GROUPS_SPEC = BUILDER
			.comment(
				"The cybernetic slot groups available to the player.",
				"The format of a slot is 'id slot_count side has_primary_slot'.",
				"'id' is a valid resource location path and determines the tag needed for a cybernetic component to be placed into that slot.",
				"'slot_count' is the number of slots and must be a positive integer.",
				"'side' is the side of the operating table the slots appear on and can be 'left' or 'right'.",
				"'primary_slot_icon' is a valid resource location path and determines the icon used for the primary (first) slot."
			)
			.worldRestart()
			.defineList(
				"cybernetic_slot_groups",
				List.of(
					toConfig(new CyberneticsSlotGroup("brain", 3, true, "brain")),
					toConfig(new CyberneticsSlotGroup("eyes", 3, true, "eyes")),
					toConfig(new CyberneticsSlotGroup("spine", 3, false, "spine")),
					toConfig(new CyberneticsSlotGroup("arms", 3, false, "arms")),
					toConfig(new CyberneticsSlotGroup("legs", 3, false, "legs")),
					toConfig(new CyberneticsSlotGroup("augments", 5, true, null))
				),
				e -> e instanceof String s && fromConfig(s) != null
			);

		static final ForgeConfigSpec SPEC = BUILDER.build();

		private static ImmutableList<CyberneticsSlotGroup> SLOT_GROUPS;
		private static Map<String, CyberneticsSlotGroup> SLOT_GROUPS_MAP;

		public static ImmutableList<CyberneticsSlotGroup> getSlotGroups()
		{
			return SLOT_GROUPS;
		}

		public static CyberneticsSlotGroup getSlotGroup(String name)
		{
			return SLOT_GROUPS_MAP.get(name);
		}
	}

	@SubscribeEvent
	static void onLoad(final ModConfigEvent event)
	{
		Server.SLOT_GROUPS = Server.SLOT_GROUPS_SPEC.get()
			.stream()
			.map(CyberConfig::fromConfig)
			.collect(ImmutableList.toImmutableList());

		Server.SLOT_GROUPS_MAP = Server.SLOT_GROUPS.stream()
			.collect(Collectors.toMap(CyberneticsSlotGroup::id, Function.identity()));
	}

	private static CyberneticsSlotGroup fromConfig(String config)
	{
		String[] split = config.split(" ");

		if (split.length != 3 && split.length != 4)
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

		String primarySlotIcon = split.length == 4 ? split[3] : null;

		return new CyberneticsSlotGroup(id, slotCount, onRight, primarySlotIcon);
	}

	private static String toConfig(CyberneticsSlotGroup group)
	{
		String config = group.id() + " " + group.slotCount() + " " + (group.onRight() ? "right" : "left");

		if (group.primarySlotIcon() != null)
		{
			config += " " + group.primarySlotIcon();
		}

		return config;
	}
}
