package net.runelite.client.plugins.autotob;

import com.google.inject.Provides;
import net.runelite.api.*;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ClientTick;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import online.paescape.cache.def.ItemDef;
import online.paescape.cache.media.interfaces.RSInterface;
import online.paescape.net.Stream;

import javax.inject.Inject;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@PluginDescriptor(
        name = "AutoTOB",
        description = "Automatically run TOB",
        tags = {},
        enabledByDefault = true
)
public class AutoTOBPlugin extends Plugin {
    @Inject
    private Client client;

    @Inject
    private AutoTOBConfig config;

    @Provides
    AutoTOBConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(AutoTOBConfig.class);
    }

    private static long lastScriptTick = System.currentTimeMillis();

    enum ScriptState {
        AWAY_FROM_RAID, IN_LOBBY, IN_BLOAT, IN_SANG, IN_VERZIK
    }

    ScriptState currentState = ScriptState.AWAY_FROM_RAID;
    ScriptState lastState = null;
    long runStartTime = -1;
    int runsCompleted = 0;

    private WorldPoint cachedPlayerLocation;
    private int[] cachedEquipmentIds;
    private int[] cachedInventoryItems;

    @Subscribe
    private void onClientTick(ClientTick clientTick) {
        if (client.getGameState() != GameState.LOGGED_IN || !config.active()) return;

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastScriptTick < 1000) return;

        online.paescape.Client pClient = online.paescape.Client.instance;

        // Cache player location and equipment/inventory
        cachedPlayerLocation = client.getLocalPlayer().getWorldLocation();
        cachedEquipmentIds = client.getLocalPlayer().getPlayerComposition().getEquipmentIds();
        cachedInventoryItems = RSInterface.interfaceCache[3214].inv;

        // Update state
        lastState = currentState;
        currentState = determineCurrentState();

        // Handle state transitions
        handleStateTransition(pClient);

        lastScriptTick = currentTime;
    }

    private ScriptState determineCurrentState() {
        if (inLobby()) {
            return ScriptState.IN_LOBBY;
        } else if (inBloatRoom()) {
            return ScriptState.IN_BLOAT;
        } else if (inSangRoom()) {
            return ScriptState.IN_SANG;
        } else if (inVerzikRoom()) {
            return ScriptState.IN_VERZIK;
        } else {
            return ScriptState.AWAY_FROM_RAID;
        }
    }

    private void handleStateTransition(online.paescape.Client pClient) {
        switch (currentState) {
            case IN_LOBBY:
                handleLobbyState(pClient);
                break;
            case IN_BLOAT:
                handleBloatState();
                break;
            case IN_SANG:
                handleSangState();
                break;
            case IN_VERZIK:
                handleVerzikState();
                break;
            default:
                break;
        }
    }

    private void handleLobbyState(online.paescape.Client pClient) {
        if (!pClient.quickPrsActive) {
            pClient.handleQuickAidsActive();
        } else if (pClient.currentStats[3] < pClient.currentMaxStats[3] || pClient.currentStats[0] <= pClient.currentMaxStats[0]) {
            firstClickObject(4005, 3670, 3214); // pool
        } else if (Objects.equals(RSInterface.interfaceCache[65002].message, "Raiding Party: @whi@0")) {
            clickButton(65003);
        } else if (Objects.equals(RSInterface.interfaceCache[39188].message, "   > @or1@Task Amount: @yel@0")) {
            clickButton(39191);
        } else {
            walkTo(3673, 3216);
            firstClickObject(332653, 3678, 3216); // entrance
        }
    }

    private void handleBloatState() {
        int sangId = 21006;
        int kodaiId = 4455;

        boolean isWearingKodai = Arrays.stream(cachedEquipmentIds).anyMatch(e -> e == kodaiId + 511);
        boolean isWearingSang = Arrays.stream(cachedEquipmentIds).anyMatch(e -> e == sangId + 511);
        boolean hasSangInInventory = Arrays.stream(cachedInventoryItems).anyMatch(id -> id == sangId);
        boolean hasKodaiInInventory = Arrays.stream(cachedInventoryItems).anyMatch(id -> id == kodaiId);

        // Prioritize Sang over Kodai
        if (hasSangInInventory && !isWearingSang) {
            equipItem(3214, findItemIndex(sangId, cachedInventoryItems), sangId);
        } else if (hasKodaiInInventory && !isWearingKodai) {
            equipItem(3214, findItemIndex(kodaiId, cachedInventoryItems), kodaiId);
        }

        // Always attack Bloat if it's present
        findAndAttackNpc("Bloat");
    }

    private void handleSangState() {
        int blowpipeId = getItemIdByName("Toxic blowpipe") + 1;
        if (Arrays.stream(cachedEquipmentIds).noneMatch(e -> e == blowpipeId + 511)) {
            int itemIndex = findItemIndex(blowpipeId, cachedInventoryItems);
            if (itemIndex != -1) {
                equipItem(3214, itemIndex, blowpipeId);
            }
        } else if (findAndAttackNpc("Maiden") == -1) {
            walkTo(3170, 4444);
        }
    }

    private void handleVerzikState() {
        int scytheId = 21007;
        int rapierId = 21004;

        boolean isWearingRapier = Arrays.stream(cachedEquipmentIds).anyMatch(e -> e == rapierId + 511);
        boolean isWearingScythe = Arrays.stream(cachedEquipmentIds).anyMatch(e -> e == scytheId + 511);
        boolean hasScytheInInventory = Arrays.stream(cachedInventoryItems).anyMatch(id -> id == scytheId);
        boolean hasRapierInInventory = Arrays.stream(cachedInventoryItems).anyMatch(id -> id == rapierId);

        // Prioritize Scythe over Rapier
        if (hasScytheInInventory && !isWearingScythe) {
            equipItem(3214, findItemIndex(scytheId, cachedInventoryItems), scytheId);
        } else if (hasRapierInInventory && !isWearingRapier) {
            equipItem(3214, findItemIndex(rapierId, cachedInventoryItems), rapierId);
        }

        // Always attack Verzik if it's present
        findAndAttackNpc("Verzik");
    }

    private int findItemIndex(int itemId, int[] items) {
        for (int i = 0; i < items.length; i++) {
            if (items[i] == itemId) {
                return i;
            }
        }
        return -1;
    }

    private int getItemIdByName(String name) {
        int id = getOSRSItemIdByName(name);
        return id < 0 ? get317ItemIdByName(name) : id;
    }

    private int get317ItemIdByName(String name) {
        return Arrays.stream(ItemDef.cache)
                .filter(Objects::nonNull)
                .filter(e -> e.name != null && e.name.equals(name))
                .findFirst()
                .map(itemDef -> itemDef.id)
                .orElse(-1);
    }

    private int getOSRSItemIdByName(String name) {
        return Arrays.stream(ItemDef.cacheOSRS)
                .filter(Objects::nonNull)
                .filter(e -> e.name != null && e.name.equals(name))
                .findFirst()
                .map(itemDef -> itemDef.id)
                .orElse(-1);
    }

    private int findAndAttackNpc(String npcName) {
        for (int i = 0; i < client.getNpcs().size(); i++) {
            NPC npc = client.getNpcs().get(i);
            if (npc != null && npc.getName() != null && npc.getName().contains(npcName)) {
                attackNpc(i);
                return i;
            }
        }
        return -1; // NPC not found
    }

    void attackNpc(int npcIdx) {
        Stream stream = online.paescape.Client.stream;
        stream.createFrame(72);
        stream.writeUnsignedWordA(npcIdx);
    }

    void equipItem(int interfaceId, int slot, int itemId) {
        Stream stream = online.paescape.Client.stream;
        stream.createFrame(41);
        stream.writeInt(itemId - 1);
        stream.writeUnsignedWordA(slot);
        stream.writeUnsignedWordA(interfaceId);
    }

    void clickButton(int id) {
        Stream stream = online.paescape.Client.stream;
        stream.createFrame(185);
        stream.writeInt(id);
    }

    void firstClickObject(int id, int x, int y) {
        Stream stream = online.paescape.Client.stream;
        stream.createFrame(132);
        stream.method433(x);
        stream.writeInt(id);
        stream.method432(y);
    }

    void walkTo(int x, int y) {
        Stream stream = online.paescape.Client.stream;
        stream.createFrame(229);
        stream.writeByte(online.paescape.Client.instance.plane);
        stream.createFrame(98);
        stream.writeWordBigEndian(5);
        stream.writeSignedBigEndian(x);
        stream.writeUnsignedWordBigEndian(y);
        stream.writeByteC(0);
    }

    boolean inLobby() {
        return cachedPlayerLocation.distanceTo(new WorldPoint(3670, 3219, 0)) <= 5;
    }

    boolean inBloatRoom() {
        return cachedPlayerLocation.distanceTo(new WorldPoint(3296, 4443, 0)) <= 7;
    }

    boolean inSangRoom() {
        return cachedPlayerLocation.distanceTo(new WorldPoint(3176, 4446, 0)) <= 14;
    }

    boolean inVerzikRoom() {
        return cachedPlayerLocation.distanceTo(new WorldPoint(3168, 4313, 0)) <= 7;
    }

    @Subscribe
    private void onGameObjectSpawned(GameObjectSpawned gameObjectSpawned) {
        // Handle game object spawns if needed
    }
}