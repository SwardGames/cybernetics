package com.sward.cybernetics.data;

import javax.annotation.Nonnull;

public record CyberneticsSlotGroup(
	@Nonnull String id,
	int slotCount,
	boolean onRight,
	String primarySlotIcon
)
{

}
