package com.sward.cybernetics.config;

import me.fzzyhmstrs.fzzy_config.util.Walkable;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedList;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedAny;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedString;
import net.minecraft.FieldsAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

@FieldsAreNonnullByDefault
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CyberSlotGroup implements Walkable
{
	public ValidatedString id;

	public ValidatedList<CyberSlotType> slotTypes;

	public CyberSlotGroup()
	{
		this("new_group");
	}

	public CyberSlotGroup(String defaultId, CyberSlotType... slotTypes)
	{
		this.id = new ValidatedString(defaultId, "[a-z0-9_]+");
		this.slotTypes = new ValidatedAny<>(new CyberSlotType()).toList(slotTypes);
	}
}
