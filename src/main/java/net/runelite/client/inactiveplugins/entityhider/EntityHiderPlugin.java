/*
 * Copyright (c) 2018, Lotto <https://github.com/devLotto>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.client.inactiveplugins.entityhider;

import com.google.inject.Provides;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@PluginDescriptor(
	name = "Entity Hider",
	description = "Hide players, NPCs, and/or projectiles",
	tags = {"npcs", "players", "projectiles"},
	enabledByDefault = false
)
public class EntityHiderPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private EntityHiderConfig config;

	@Provides
	EntityHiderConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(EntityHiderConfig.class);
	}

	@Override
	protected void startUp()
	{
		updateConfig();
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged e)
	{
		if (e.getGroup().equals(EntityHiderConfig.GROUP))
		{
			updateConfig();
		}
	}

	private void updateConfig()
	{
		client.setIsHidingEntities(true);

		client.setOthersHidden(config.hideOthers());
		client.setOthersHidden2D(config.hideOthers2D());

		client.setFriendsHidden(config.hideFriends());
		client.setFriendsChatMembersHidden(config.hideFriendsChatMembers());
		client.setIgnoresHidden(config.hideIgnores());

		client.setLocalPlayerHidden(config.hideLocalPlayer());
		client.setLocalPlayerHidden2D(config.hideLocalPlayer2D());

		client.setNPCsHidden(config.hideNPCs());
		client.setNPCsHidden2D(config.hideNPCs2D());

		client.setPetsHidden(config.hidePets());

		client.setAttackersHidden(config.hideAttackers());

		client.setProjectilesHidden(config.hideProjectiles());
	}

	@Override
	protected void shutDown() throws Exception
	{
		client.setIsHidingEntities(false);

		client.setOthersHidden(false);
		client.setOthersHidden2D(false);

		client.setFriendsHidden(false);
		client.setFriendsChatMembersHidden(false);
		client.setIgnoresHidden(false);

		client.setLocalPlayerHidden(false);
		client.setLocalPlayerHidden2D(false);

		client.setNPCsHidden(false);
		client.setNPCsHidden2D(false);

		client.setPetsHidden(false);

		client.setAttackersHidden(false);

		client.setProjectilesHidden(false);
	}
}
