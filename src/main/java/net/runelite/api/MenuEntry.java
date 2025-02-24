/*
 * Copyright (c) 2016-2017, Adam <Adam@sigterm.info>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.api;

import net.runelite.api.widgets.Widget;

import javax.annotation.Nullable;
import java.util.function.Consumer;

/**
 * A menu entry in a right-click menu.
 */
public interface MenuEntry
{
	/**
	 * The option text added to the menu. (ie. "Walk here", "Use")
	 */
	String getOption();
	MenuEntry setOption(String option);

	/**
	 * The target of the action. (ie. Item or Actor name)
	 * <p>
	 * If the option does not apply to any target, this field
	 * will be set to empty string.
	 */
	String getTarget();
	MenuEntry setTarget(String target);

	/**
	 * An identifier value for the target of the action.
	 */
	long getIdentifier();
	MenuEntry setIdentifier(long identifier);

	/**
	 * The action the entry will trigger.
	 */
	MenuAction getType();
	MenuEntry setType(MenuAction type);

	/**
	 * An additional parameter for the action.
	 */
	int getParam0();
	MenuEntry setParam0(int param0);

	/**
	 * A second additional parameter for the action.
	 */
	int getParam1();
	MenuEntry setParam1(int param1);

	/**
	 * If this is true and you have single mouse button on and this entry is
	 * the top entry the right click menu will not be opened when you left click
	 *
	 * This is used  for shift click
	 */
	boolean isForceLeftClick();
	MenuEntry setForceLeftClick(boolean forceLeftClick);

	/**
	 * Deprioritized menus are sorted in the menu to be below the other menu entries.
	 * @return
	 */
	boolean isDeprioritized();
	MenuEntry setDeprioritized(boolean deprioritized);

	/**
	 * Set a callback to be called when this menu option is clicked
	 * @param callback
	 * @return
	 */
	MenuEntry onClick(Consumer<MenuEntry> callback);
}