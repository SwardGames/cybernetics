package com.sward.cybernetics.cybernetics;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.FieldsAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@FieldsAreNonnullByDefault
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class Event<T>
{
	static final Set<Event<?>> REGISTERED = new ObjectOpenHashSet<>();

	@Getter
	private final Class<T> interfaceClass;

	private final T defaultValue;

	private final Function<List<T>, T> merger;

	public T apply(List<ICyberneticComponent> components)
	{
		List<T> list = new ArrayList<>();

		for (ICyberneticComponent component : components)
		{
			if (this.interfaceClass.isInstance(component))
			{
				list.add(this.interfaceClass.cast(component));
			}
		}

		if (list.isEmpty())
		{
			return this.defaultValue;
		}
		else if (list.size() == 1)
		{
			return list.get(0);
		}
		else
		{
			return this.merger.apply(list);
		}
	}

	public static <T> Event<T> create(Class<T> interfaceClass, T defaultValue, Function<List<T>, T> merger)
	{
		return new Event<>(interfaceClass, defaultValue, merger);
	}
}
