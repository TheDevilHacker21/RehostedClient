package net.runelite.client.plugins.autotob;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("autotob")
public interface AutoTOBConfig extends Config {
    @ConfigItem(
            position = 0,
            keyName = "active",
            name = "active",
            description = "Configures whether or not the plugin is running"
    )
    default boolean active()
    {
        return false;
    }
}
