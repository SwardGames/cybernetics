package com.sward.cybernetics.datagen.client;

import com.sward.cybernetics.items.CyberItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.BlockItem;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import static com.sward.cybernetics.Cybernetics.ID;

public class CyberItemModelProvider extends ItemModelProvider
{
	public CyberItemModelProvider(
		PackOutput output,
		ExistingFileHelper existingFileHelper
	)
	{
		super(output, ID, existingFileHelper);
	}

	@Override
	protected void registerModels()
	{
		CyberItems.REGISTER.getEntries().forEach(
			item ->
			{
				if (item.get() instanceof BlockItem)
				{
					return;
				}

				basicItem(item.get());
			}
		);
	}
}
