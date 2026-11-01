package com.sward.cybernetics.config;

import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.api.FileType;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedList;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedAny;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

import static com.sward.cybernetics.Cybernetics.*;

@Mod.EventBusSubscriber(modid = ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class CyberConfig
{
	public static final Slots SLOTS = ConfigApiJava.registerAndLoadConfig(Slots::new);

	public static class Slots extends Config
	{
		public ValidatedList<CyberSlotGroup> groups = new ValidatedAny<>(new CyberSlotGroup()).toList(
			new CyberSlotGroup(
				"nervous_system",
				new CyberSlotType("brain", 1, "brain"),
				new CyberSlotType("spine", 1, "spine"),
				new CyberSlotType("nervous_system_augments", 3)
			),
			new CyberSlotGroup(
				"eyes",
				new CyberSlotType("eyes", 1, "eyes"),
				new CyberSlotType("eye_augments", 2)
			),
			new CyberSlotGroup(
				"arms",
				new CyberSlotType("arms", 1, "arms"),
				new CyberSlotType("hands", 1, "hands"),
				new CyberSlotType("arm_augments", 1)
			),
			new CyberSlotGroup(
				"circulatory_system",
				new CyberSlotType("heart", 1, "heart"),
				new CyberSlotType("circulatory_system_augments", 2)
			),
			new CyberSlotGroup(
				"legs",
				new CyberSlotType("legs", 1, "legs"),
				new CyberSlotType("feet", 1, "feet"),
				new CyberSlotType("leg_augments", 1)
			),
			new CyberSlotGroup(
				"augments",
				new CyberSlotType("augments", 5)
			)
		);

		public Slots()
		{
			super(id("cybernetic_slots"));
		}

		public @Nullable CyberSlotType findSlotType(String id)
		{
			for (CyberSlotGroup groups : this.groups)
			{
				for (CyberSlotType slotTypes : groups.slotTypes)
				{
					if (slotTypes.id.get().equals(id))
					{
						return slotTypes;
					}
				}
			}

			return null;
		}

		@Override
		public @NotNull FileType fileType()
		{
			return FileType.JSON;
		}
	}

	public static void init()
	{

	}
}
