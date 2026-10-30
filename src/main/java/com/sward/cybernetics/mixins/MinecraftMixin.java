package com.sward.cybernetics.mixins;

import com.sward.cybernetics.attributes.CyberAttributes;
import com.sward.cybernetics.entities.IMotionTrackable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Minecraft.class, remap = true)
public abstract class MinecraftMixin
{
	@Inject(method = "shouldEntityAppearGlowing", at = @At("RETURN"), cancellable = true)
	private void cybernetics$shouldEntityAppearGlowing(Entity entity, CallbackInfoReturnable<Boolean> callback)
	{
		if (callback.getReturnValueZ())
		{
			return;
		}

		if (!(entity instanceof IMotionTrackable motionTrackable))
		{
			return;
		}

		if (motionTrackable.cybernetics$getMotionTrackerSpeed() <= 0.2D)
		{
			return;
		}

		LocalPlayer player = Minecraft.getInstance().player;

		if (player == null || !player.isAlive())
		{
			return;
		}

		double scannerRange = player.getAttributeValue(CyberAttributes.MOTION_SCANNER_RANGE.get());

		if (scannerRange <= 0D)
		{
			return;
		}

		if (player.distanceToSqr(entity) <= scannerRange * scannerRange)
		{
			callback.setReturnValue(true);
		}
	}
}
