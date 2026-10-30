package com.sward.cybernetics.mixins;

import com.sward.cybernetics.entities.IMotionTrackable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = LivingEntity.class, remap = true)
public abstract class LivingEntityMixin extends Entity implements IMotionTrackable
{
	@Unique
	private double cybernetics$motionTrackerSpeed;

	@Inject(method = "tick", at = @At("TAIL"), remap = true)
	private void cybernetics$tick(CallbackInfo ci)
	{
		this.cybernetics$motionTrackerSpeed -= 0.2D / 20D;

		double speed = 0D;

		if (!getSharedFlag(1 << 1))
		{
			speed = getDeltaMovement().length();
		}

		this.cybernetics$motionTrackerSpeed = Math.max(this.cybernetics$motionTrackerSpeed, speed);
	}

	@Override
	public double cybernetics$getMotionTrackerSpeed()
	{
		return this.cybernetics$motionTrackerSpeed;
	}

	private LivingEntityMixin(EntityType<?> pEntityType, Level pLevel)
	{
		super(pEntityType, pLevel);
	}
}
