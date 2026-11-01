package com.sward.cybernetics.client.screens.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Label
{
	public final FormattedCharSequence text;
	public final Font font;

	public int x;
	public int y;

	public int color;
	public boolean shadow;

	public void draw(GuiGraphics graphics)
	{
		graphics.drawString(this.font, this.text, this.x, this.y, this.color, this.shadow);
	}

	public static Label at(Component text, Font font, int x, int y)
	{
		return at(text.getVisualOrderText(), font, x, y, -1, true);
	}

	public static Label atCentered(Component text, Font font, int x, int y)
	{
		return atCentered(text.getVisualOrderText(), font, x, y, -1, true);
	}

	public static Label atRightAligned(Component text, Font font, int x, int y)
	{
		return atRightAligned(text.getVisualOrderText(), font, x, y, -1, true);
	}

	public static Label at(Component text, Font font, int x, int y, int color)
	{
		return at(text.getVisualOrderText(), font, x, y, color, true);
	}

	public static Label atCentered(Component text, Font font, int x, int y, int color)
	{
		return atCentered(text.getVisualOrderText(), font, x, y, color, true);
	}

	public static Label atRightAligned(Component text, Font font, int x, int y, int color)
	{
		return atRightAligned(text.getVisualOrderText(), font, x, y, color, true);
	}

	public static Label at(Component text, Font font, int x, int y, int color, boolean shadow)
	{
		return at(text.getVisualOrderText(), font, x, y, color, shadow);
	}

	public static Label atCentered(Component text, Font font, int x, int y, int color, boolean shadow)
	{
		return atCentered(text.getVisualOrderText(), font, x, y, color, shadow);
	}

	public static Label atRightAligned(Component text, Font font, int x, int y, int color, boolean shadow)
	{
		return atRightAligned(text.getVisualOrderText(), font, x, y, color, shadow);
	}

	public static Label at(FormattedCharSequence text, Font font, int x, int y)
	{
		return new Label(text, font, x, y, -1, true);
	}

	public static Label atCentered(FormattedCharSequence text, Font font, int x, int y)
	{
		return new Label(text, font, x - font.width(text) / 2, y, -1, true);
	}

	public static Label atRightAligned(FormattedCharSequence text, Font font, int x, int y)
	{
		return new Label(text, font, x - font.width(text), y, -1, true);
	}

	public static Label at(FormattedCharSequence text, Font font, int x, int y, int color)
	{
		return new Label(text, font, x, y, color, true);
	}

	public static Label atCentered(FormattedCharSequence text, Font font, int x, int y, int color)
	{
		return new Label(text, font, x - font.width(text) / 2, y, color, true);
	}

	public static Label atRightAligned(FormattedCharSequence text, Font font, int x, int y, int color)
	{
		return new Label(text, font, x - font.width(text), y, color, true);
	}

	public static Label at(FormattedCharSequence text, Font font, int x, int y, int color, boolean shadow)
	{
		return new Label(text, font, x, y, color, shadow);
	}

	public static Label atCentered(FormattedCharSequence text, Font font, int x, int y, int color, boolean shadow)
	{
		return new Label(text, font, x - font.width(text) / 2, y, color, shadow);
	}

	public static Label atRightAligned(FormattedCharSequence text, Font font, int x, int y, int color, boolean shadow)
	{
		return new Label(text, font, x - font.width(text), y, color, shadow);
	}
}
