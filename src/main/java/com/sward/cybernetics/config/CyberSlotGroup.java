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

	public int x;
	public int y;

	public CyberSlotGroup()
	{
		this("new_group", 0, 0);
	}

	public CyberSlotGroup(String defaultId, int x, int y, CyberSlotType... slotTypes)
	{
		this.id = new ValidatedString(defaultId, "[a-z0-9_]+");
		this.x = x;
		this.y = y;
		this.slotTypes = new ValidatedAny<>(new CyberSlotType()).toList(slotTypes);
	}
}
