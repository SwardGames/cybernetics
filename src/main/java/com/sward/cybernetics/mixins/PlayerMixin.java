package com.sward.cybernetics.mixins;

import com.sward.cybernetics.capabilities.ICyberneticsInventory;
import com.sward.cybernetics.cybernetics.events.reaction.OnInflictingKnockback;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.mutable.MutableDouble;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Optional;

import static com.sward.cybernetics.capabilities.ICyberneticsInventory.CAP;

@Mixin(value = Player.class, remap = true)
public abstract class PlayerMixin extends LivingEntity
{
	@ModifyVariable(method = "attack", at = @At(value = "STORE"), name = "i")
	private float cybernetics$attack(float i)
	{
		Optional<ICyberneticsInventory> cap = this.getCapability(CAP).resolve();

		if (cap.isEmpty())
		{
			return i;
		}

		MutableDouble knockback = new MutableDouble(i);

		cap.get().forEachEvent(
			OnInflictingKnockback.EVENT,
			(event, entity, stack) -> event.inflictingKnockback(stack, entity, knockback)
		);

		return knockback.floatValue();
	}

	protected PlayerMixin(
		EntityType<? extends LivingEntity> pEntityType,
		Level pLevel
	)
	{
		super(pEntityType, pLevel);
	}
}
