package com.sward.cybernetics.mixins.client;

import com.sward.cybernetics.entities.IMotionTrackable;
import net.minecraft.client.Minecraft;
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

		if (motionTrackable.cybernetics$isTracked())
		{
			callback.setReturnValue(true);
		}
	}
}
