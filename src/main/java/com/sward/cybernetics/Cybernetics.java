package com.sward.cybernetics;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.sward.cybernetics.blocks.OperatingTableBlock;
import com.sward.cybernetics.compat.curios.components.CuriosSlotsComponent;
import com.sward.cybernetics.config.CyberConfig;
import com.sward.cybernetics.cybernetics.CyberneticDefinition;
import com.sward.cybernetics.cybernetics.ICyberneticComponent;
import com.sward.cybernetics.cybernetics.components.*;
import com.sward.cybernetics.registries.CyberRegistries;
import com.sward.cybernetics.attributes.CyberAttributes;
import com.sward.cybernetics.blocks.CyberBlocks;
import com.sward.cybernetics.capabilities.CyberneticsInventoryProvider;
import com.sward.cybernetics.capabilities.ICyberneticsInventory;
import com.sward.cybernetics.items.CyberCreativeTabs;
import com.sward.cybernetics.items.CyberItems;
import com.sward.cybernetics.inventory.CyberMenuTypes;
import com.sward.cybernetics.registries.CyberRegistryKeys;
import com.sward.cybernetics.tooltips.CyberneticTooltipCategory;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.event.entity.player.SleepingTimeCheckEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.*;
import org.slf4j.Logger;

import java.util.Optional;

@Mod(Cybernetics.ID)
public final class Cybernetics
{
	public static final String ID = "cybernetics";
	public static final Logger LOGGER = LogUtils.getLogger();

	public Cybernetics(FMLJavaModLoadingContext ctx)
	{
		IEventBus modEventBus = ctx.getModEventBus();

		CyberConfig.init();
		CyberneticTooltipCategory.register();

		CyberAttributes.REGISTER.register(modEventBus);
		CyberBlocks.REGISTER.register(modEventBus);
		CyberItems.REGISTER.register(modEventBus);
		CyberCreativeTabs.REGISTER.register(modEventBus);
		CyberMenuTypes.REGISTER.register(modEventBus);

		modEventBus.addListener(Cybernetics::attributeModification);
		modEventBus.addListener(Cybernetics::newRegistries);
		modEventBus.addListener(Cybernetics::newDataPackRegistries);
		modEventBus.addListener(Cybernetics::registerEvent);

		MinecraftForge.EVENT_BUS.addListener(Cybernetics::sleepingTimeCheck);
		MinecraftForge.EVENT_BUS.addGenericListener(Entity.class, Cybernetics::attachEntityCapabilities);
	}

	public static ResourceLocation id(String path)
	{
		return ResourceLocation.fromNamespaceAndPath(ID, path);
	}

	private static void attributeModification(EntityAttributeModificationEvent e)
	{
		for (RegistryObject<Attribute> attribute : CyberAttributes.REGISTER.getEntries())
		{
			e.add(EntityType.PLAYER, attribute.get());
		}
	}

	private static void newRegistries(NewRegistryEvent e)
	{
		e.<Codec<? extends ICyberneticComponent>>create(
			RegistryBuilder.of(CyberRegistryKeys.CYBERNETIC_COMPONENT_CODECS.location()),
			newRegistry -> CyberRegistries.CYBERNETIC_COMPONENT_CODECS = newRegistry
		);
	}

	private static void newDataPackRegistries(DataPackRegistryEvent.NewRegistry e)
	{
		e.dataPackRegistry(CyberRegistryKeys.CYBERNETIC_DEFINITIONS, CyberneticDefinition.CODEC);
	}

	private static void registerEvent(RegisterEvent e)
	{
		e.register(
			CyberRegistryKeys.CYBERNETIC_COMPONENT_CODECS,
			id("attribute_modifiers"),
			() -> AttributeModifiersComponent.CODEC
		);

		e.register(
			CyberRegistryKeys.CYBERNETIC_COMPONENT_CODECS,
			id("description"),
			() -> DescriptionComponent.CODEC
		);

		e.register(
			CyberRegistryKeys.CYBERNETIC_COMPONENT_CODECS,
			id("when_implanted_tooltip"),
			() -> WhenImplantedTooltipComponent.CODEC
		);

		e.register(
			CyberRegistryKeys.CYBERNETIC_COMPONENT_CODECS,
			id("incompatible_implant_tag"),
			() -> IncompatibleImplantTagComponent.CODEC
		);

		e.register(
			CyberRegistryKeys.CYBERNETIC_COMPONENT_CODECS,
			id("required_implant_tag"),
			() -> RequiredImplantTagComponent.CODEC
		);

		e.register(
			CyberRegistryKeys.CYBERNETIC_COMPONENT_CODECS,
			id("damage_modifier"),
			() -> DamageModifierComponent.CODEC
		);

		e.register(
			CyberRegistryKeys.CYBERNETIC_COMPONENT_CODECS,
			id("rear_damage_reduction"),
			() -> RearDamageReductionComponent.CODEC
		);

		e.register(
			CyberRegistryKeys.CYBERNETIC_COMPONENT_CODECS,
			id("unarmed_knockback"),
			() -> UnarmedKnockbackComponent.CODEC
		);

		e.register(
			CyberRegistryKeys.CYBERNETIC_COMPONENT_CODECS,
			id("resuscitator"),
			() -> ResuscitatorComponent.CODEC
		);

		e.register(
			CyberRegistryKeys.CYBERNETIC_COMPONENT_CODECS,
			id("apply_effect_on_hurt"),
			() -> ApplyEffectOnHurtComponent.CODEC
		);

		if (AddonsPresent.CURIOS)
		{
			e.register(
				CyberRegistryKeys.CYBERNETIC_COMPONENT_CODECS,
				id("curios_slots"),
				() -> CuriosSlotsComponent.CODEC
			);
		}
	}

	private static void sleepingTimeCheck(SleepingTimeCheckEvent e)
	{
		Optional<BlockPos> maybePos = e.getSleepingLocation();

		if (maybePos.isEmpty())
		{
			return;
		}

		if (e.getEntity().level().getBlockState(maybePos.get()).getBlock() instanceof OperatingTableBlock)
		{
			e.setResult(Event.Result.ALLOW);
		}
	}

	private static void attachEntityCapabilities(AttachCapabilitiesEvent<Entity> e)
	{
		if (e.getObject() instanceof Player player)
		{
			e.addCapability(ICyberneticsInventory.KEY, new CyberneticsInventoryProvider(player));
		}
	}
}
