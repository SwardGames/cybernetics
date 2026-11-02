package com.sward.cybernetics.entities;

import com.sward.cybernetics.capabilities.ICyberneticsInventory;
import com.sward.cybernetics.cybernetics.events.reaction.*;
import com.sward.cybernetics.cybernetics.events.reaction.effects.OnUserEffectAdded;
import com.sward.cybernetics.cybernetics.events.reaction.effects.OnUserEffectApplicable;
import com.sward.cybernetics.cybernetics.events.reaction.effects.OnUserEffectExpired;
import com.sward.cybernetics.cybernetics.events.reaction.effects.OnUserEffectRemoved;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Optional;

import static com.sward.cybernetics.Cybernetics.ID;
import static com.sward.cybernetics.capabilities.ICyberneticsInventory.CAP;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Mod.EventBusSubscriber(modid = ID)
public final class EntityEvents
{
	@SubscribeEvent
	public static void tickPlayer(TickEvent.PlayerTickEvent e)
	{
		Optional<ICyberneticsInventory> cap = e.player.getCapability(CAP).resolve();

		if (cap.isEmpty())
		{
			return;
		}

		cap.get().tick();
	}

	@SubscribeEvent
	public static void livingHeal(LivingHealEvent e)
	{
		Optional<ICyberneticsInventory> cap = e.getEntity().getCapability(CAP).resolve();

		if (cap.isEmpty())
		{
			return;
		}

		cap.get().handleEvent(OnUserHealed.EVENT, e, OnUserHealed::userHealed);
	}

	@SubscribeEvent
	public static void livingHurt(LivingHurtEvent e)
	{
		Optional<ICyberneticsInventory> cap = e.getEntity().getCapability(CAP).resolve();

		if (cap.isPresent())
		{
			cap.get().handleEvent(OnUserHurt.EVENT, e, OnUserHurt::userHurt);
		}

		if (e.isCanceled())
		{
			return;
		}

		if (e.getSource().getEntity() instanceof LivingEntity other)
		{
			Optional<ICyberneticsInventory> otherCap = other.getCapability(CAP).resolve();

			if (otherCap.isPresent())
			{
				otherCap.get().handleEvent(OnOtherHurt.EVENT, e, OnOtherHurt::otherHurt);
			}
		}
	}

	@SubscribeEvent
	public static void livingDeath(LivingDeathEvent e)
	{
		Optional<ICyberneticsInventory> cap = e.getEntity().getCapability(CAP).resolve();

		if (cap.isPresent())
		{
			cap.get().handleEvent(OnUserKilled.EVENT, e, OnUserKilled::userKilled);
		}

		if (e.isCanceled())
		{
			return;
		}

		if (e.getSource().getEntity() instanceof LivingEntity other)
		{
			Optional<ICyberneticsInventory> otherCap = other.getCapability(CAP).resolve();

			if (otherCap.isPresent())
			{
				otherCap.get().handleEvent(OnOtherKilled.EVENT, e, OnOtherKilled::otherKilled);
			}
		}
	}

	@SubscribeEvent
	public static void livingKnockedBack(LivingKnockBackEvent e)
	{
		Optional<ICyberneticsInventory> cap = e.getEntity().getCapability(CAP).resolve();

		if (cap.isEmpty())
		{
			return;
		}

		cap.get().handleEvent(OnUserKnockedBack.EVENT, e, OnUserKnockedBack::userKnockedBack);
	}

	@SubscribeEvent
	public static void effectAdded(MobEffectEvent.Added e)
	{
		e.getEffectInstance().duration = EffectHelper.getScaledEffectDuration(e.getEntity(), e.getEffectInstance());

		Optional<ICyberneticsInventory> cap = e.getEntity().getCapability(CAP).resolve();

		if (cap.isEmpty())
		{
			return;
		}

		cap.get().handleEvent(OnUserEffectAdded.EVENT, e, OnUserEffectAdded::userEffectAdded);
	}

	@SubscribeEvent
	public static void effectRemoved(MobEffectEvent.Remove e)
	{
		Optional<ICyberneticsInventory> cap = e.getEntity().getCapability(CAP).resolve();

		if (cap.isEmpty())
		{
			return;
		}

		cap.get().handleEvent(OnUserEffectRemoved.EVENT, e, OnUserEffectRemoved::userEffectRemoved);
	}

	@SubscribeEvent
	public static void effectExpired(MobEffectEvent.Expired e)
	{
		Optional<ICyberneticsInventory> cap = e.getEntity().getCapability(CAP).resolve();

		if (cap.isEmpty())
		{
			return;
		}

		cap.get().handleEvent(OnUserEffectExpired.EVENT, e, OnUserEffectExpired::userEffectExpired);
	}

	@SubscribeEvent
	public static void effectApplicable(MobEffectEvent.Applicable e)
	{
		int newDuration = EffectHelper.getScaledEffectDuration(e.getEntity(), e.getEffectInstance());

		if (newDuration == 0)
		{
			e.setResult(Event.Result.DENY);

			return;
		}

		Optional<ICyberneticsInventory> cap = e.getEntity().getCapability(CAP).resolve();

		if (cap.isEmpty())
		{
			return;
		}

		cap.get().handleEvent(OnUserEffectApplicable.EVENT, e, OnUserEffectApplicable::userEffectApplicable);
	}
}
