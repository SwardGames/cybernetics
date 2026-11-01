package com.sward.cybernetics.config;

import kotlin.ranges.IntRange;
import me.fzzyhmstrs.fzzy_config.util.Walkable;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedString;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import net.minecraft.FieldsAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@FieldsAreNonnullByDefault
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CyberSlotType implements Walkable
{
	public ValidatedString id;

	public ValidatedInt count;

	public String icon;

	public CyberSlotType()
	{
		this("new_slot_type", 1, "");
	}

	public CyberSlotType(String defaultId)
	{
		this(defaultId, 1, "");
	}

	public CyberSlotType(String defaultId, String icon)
	{
		this(defaultId, 1, icon);
	}

	public CyberSlotType(String defaultId, int defaultCount)
	{
		this(defaultId, defaultCount, "");
	}

	public CyberSlotType(String defaultId, int defaultCount, String icon)
	{
		this.id = new ValidatedString(defaultId, "[a-z0-9_]+");
		this.count = new ValidatedInt(defaultCount, new IntRange(1, Integer.MAX_VALUE));
		this.icon = icon;
	}
}
