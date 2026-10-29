package com.sward.cybernetics;

import com.mojang.logging.LogUtils;
import com.sward.cybernetics.attributes.CyberAttributes;
import com.sward.cybernetics.blocks.CyberBlocks;
import com.sward.cybernetics.capabilities.CyberneticsInventoryProvider;
import com.sward.cybernetics.capabilities.ICyberneticsInventory;
import com.sward.cybernetics.items.CyberCreativeTabs;
import com.sward.cybernetics.items.CyberItems;
import com.sward.cybernetics.inventory.CyberMenuTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Cybernetics.ID)
public final class Cybernetics
{
	public static final String ID = "cybernetics";
	public static final Logger LOGGER = LogUtils.getLogger();

	public Cybernetics(FMLJavaModLoadingContext ctx)
	{
		IEventBus modEventBus = ctx.getModEventBus();

		CyberAttributes.REGISTER.register(modEventBus);
		CyberBlocks.REGISTER.register(modEventBus);
		CyberItems.REGISTER.register(modEventBus);
		CyberCreativeTabs.REGISTER.register(modEventBus);
		CyberMenuTypes.REGISTER.register(modEventBus);

		ctx.registerConfig(ModConfig.Type.SERVER, CyberConfig.Server.SPEC);

		modEventBus.addListener(Cybernetics::attributeModification);

		MinecraftForge.EVENT_BUS.addGenericListener(Entity.class, Cybernetics::attachEntityCapabilities);
	}

	public static ResourceLocation id(String path)
	{
		return ResourceLocation.fromNamespaceAndPath(ID, path);
	}

	private static void attributeModification(EntityAttributeModificationEvent e)
	{
		e.add(EntityType.PLAYER, CyberAttributes.CAPACITY.get());
	}

	private static void attachEntityCapabilities(AttachCapabilitiesEvent<Entity> e)
	{
		if (e.getObject() instanceof Player player)
		{
			e.addCapability(ICyberneticsInventory.KEY, new CyberneticsInventoryProvider(player));
		}
	}
}
