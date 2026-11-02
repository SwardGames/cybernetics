package com.sward.cybernetics.mixins;

import com.sward.cybernetics.attributes.CyberAttributes;
import com.sward.cybernetics.capabilities.ICyberneticsInventory;
import com.sward.cybernetics.entities.IMotionTrackable;
import com.sward.cybernetics.items.CyberItemTags;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(value = LivingEntity.class, remap = true)
public abstract class LivingEntityMixin extends Entity implements IMotionTrackable
{
	@Unique
	private int cybernetics$trackingTime;

	@Inject(method = "tick", at = @At("TAIL"), remap = true)
	private void cybernetics$tick(CallbackInfo ci)
	{
		// This is entirely client side
		if (!this.level().isClientSide)
		{
			return;
		}

		--this.cybernetics$trackingTime;

		LocalPlayer player = Minecraft.getInstance().player;

		// Require the player to exist, is not the entity, and is alive
		if (player == null || player == (Entity)this || !player.isAlive())
		{
			return;
		}

		// Require the mob to not be sneaking
		if (getSharedFlag(1 << 1))
		{
			return;
		}

		double trackingRange = player.getAttributeValue(CyberAttributes.MOTION_TRACKER_RANGE.get());

		// Require the player to have a motion tracking range
		if (trackingRange <= 0D)
		{
			return;
		}

		int trackingDuration = (int)(20 * player.getAttributeValue(CyberAttributes.TRACKING_DURATION.get()));

		// Require the player to have a tracking duration
		if (trackingDuration <= 0)
		{
			return;
		}

		// Require the mob to be within the tracking range
		if (player.distanceToSqr(this) > trackingRange * trackingRange)
		{
			return;
		}

		double speed = getDeltaMovement().lengthSqr();

		// Require the mob to be moving above a small threshold
		if (speed > 0.1D * 0.1D)
		{
			this.cybernetics$trackingTime = trackingDuration;
		}
	}

	@Override
	public boolean cybernetics$isTracked()
	{
		return this.cybernetics$trackingTime > 0;
	}

	@Override
	public boolean isSteppingCarefully()
	{
		Optional<ICyberneticsInventory> inv = getCapability(ICyberneticsInventory.CAP).resolve();

		if (inv.isPresent() && inv.get().contains(CyberItemTags.SILENCES_FOOTSTEPS))
		{
			return !isSprinting();
		}

		return super.isSteppingCarefully();
	}

	private LivingEntityMixin(EntityType<?> pEntityType, Level pLevel)
	{
		super(pEntityType, pLevel);
	}
}
