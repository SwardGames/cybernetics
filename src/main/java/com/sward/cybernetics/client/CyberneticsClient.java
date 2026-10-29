package com.sward.cybernetics.client;

import com.sward.cybernetics.client.screens.OperatingTableScreen;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import static com.sward.cybernetics.Cybernetics.ID;
import static com.sward.cybernetics.inventory.CyberMenuTypes.OPERATING_TABLE;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Mod.EventBusSubscriber(modid = ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class CyberneticsClient
{
	@SubscribeEvent
	public static void clientSetup(FMLClientSetupEvent e)
	{
		e.enqueueWork(() -> MenuScreens.register(OPERATING_TABLE.get(), OperatingTableScreen::new));
	}
}
