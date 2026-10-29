package com.sward.cybernetics.blocks;

import com.sward.cybernetics.registries.DyedRegistryObjects;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.sward.cybernetics.Cybernetics.ID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CyberBlocks
{
	public static final DeferredRegister<Block> REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS, ID);

	public static final DyedRegistryObjects<OperatingTableBlock> OPERATING_TABLES = new DyedRegistryObjects<>(
		REGISTER,
		"operating_table",
		OperatingTableBlock::new
	);
}
