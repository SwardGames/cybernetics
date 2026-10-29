package com.sward.cybernetics.blocks;

import com.sward.cybernetics.capabilities.ICyberneticsInventory;
import com.sward.cybernetics.inventory.OperatingTableMenu;
import net.minecraft.FieldsAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.ParametersAreNonnullByDefault;

@FieldsAreNonnullByDefault
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class OperatingTableBlock extends BedBlock
{
	public OperatingTableBlock(DyeColor pColor)
	{
		super(pColor, BlockBehaviour.Properties.of());
	}

	@Override
	public InteractionResult use(
		BlockState pState,
		Level pLevel,
		BlockPos pPos,
		Player pPlayer,
		InteractionHand pHand,
		BlockHitResult pHit
	)
	{
		if (pLevel.isClientSide)
		{
			return InteractionResult.CONSUME;
		}
		else
		{
			if (pState.getValue(PART) != BedPart.HEAD)
			{
				pPos = pPos.relative(pState.getValue(FACING));
				pState = pLevel.getBlockState(pPos);

				if (!pState.is(this))
				{
					return InteractionResult.CONSUME;
				}
			}

			ServerPlayer player = (ServerPlayer) pPlayer;

			if (pState.getValue(OCCUPIED))
			{
				player.displayClientMessage(Component.translatable("block.minecraft.bed.occupied"), true);

				return InteractionResult.SUCCESS;
			}
			else
			{
				player.startSleeping(pPos);

				NetworkHooks.openScreen(
					player,
					new SimpleMenuProvider(
						(containerId, playerInventory, p) -> new OperatingTableMenu(
							containerId,
							playerInventory,
							player.getCapability(ICyberneticsInventory.CAP)
								.orElseThrow(() -> new IllegalStateException("Missing cybernetics inventory"))
						),
						Component.translatable("menu.cybernetics.operating_table")
					),
					pPos
				);

				return InteractionResult.SUCCESS;
			}
		}
	}
}
