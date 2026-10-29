package com.sward.cybernetics.datagen;

import com.sward.cybernetics.datagen.client.CyberItemModelProvider;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.sward.cybernetics.Cybernetics.ID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Mod.EventBusSubscriber(modid = ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class CyberDataGenerator
{
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event)
	{
		DataGenerator gen = event.getGenerator();
		PackOutput packOutput = gen.getPackOutput();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

		boolean includeClient = event.includeClient();

		gen.addProvider(
			includeClient,
			new CyberItemModelProvider(packOutput, existingFileHelper)
		);
	}
}
