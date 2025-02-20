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

    @Subscribe
    public void onGameStateChanged(GameStateChanged gameStateChanged) {
        if (gameStateChanged.getGameState() != GameState.LOGGED_IN) {
            return;
        }

        // map region has just been updated

    }

    private static long lastScriptTick = System.currentTimeMillis();

    enum ScriptState {
        AWAY_FROM_RAID, IN_LOBBY, IN_BLOAT, IN_SANG, IN_VERZIK,
    }

    ScriptState currentState = ScriptState.AWAY_FROM_RAID;
    ScriptState lastState = null;
    long runStartTime = -1;
    int runsCompleted = 0;

    @Subscribe()
    private void onClientTick(ClientTick clientTick) {
        if (client.getGameState() != GameState.LOGGED_IN || !config.active()) return;

        if (System.currentTimeMillis() - lastScriptTick < 1000) return;

        online.paescape.Client pClient = online.paescape.Client.instance;

//        System.out.print("current stats: ");
//        for (int e : pClient.currentStats) {
//            System.out.printf("%d, ", e);
//        }
//        System.out.println();
//
//        System.out.print("    max stats: ");
//        for (int e : pClient.currentMaxStats) {
//            System.out.printf("%d, ", e);
//        }
//        System.out.println();

        lastState = currentState;
        if (inLobby()) {
            currentState = ScriptState.IN_LOBBY;
            if (lastState == ScriptState.IN_VERZIK) {
                System.out.printf("Run %d took: %ds%n", ++runsCompleted, (System.currentTimeMillis() - runStartTime) / 1000);
            }
        } else if (inBloatRoom()) {
            currentState = ScriptState.IN_BLOAT;
            if (lastState == ScriptState.IN_LOBBY)
                runStartTime = System.currentTimeMillis();
        } else if (inSangRoom()) {
            currentState = ScriptState.IN_SANG;
        } else if (inVerzikRoom()) {
            currentState = ScriptState.IN_VERZIK;
        } else {
            currentState = ScriptState.AWAY_FROM_RAID;
        }

        if (currentState == ScriptState.IN_LOBBY) {
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
        } else if (currentState == ScriptState.IN_BLOAT) {
            int sangId = 21006;
            int kodaiId = 4454;
 //           int sangId = getItemIdByName("Sanguinesti staff") + 1;
            if (Arrays.stream(client.getLocalPlayer().getPlayerComposition().getEquipmentIds()).noneMatch(e -> e == sangId + 511)) {
                // if not wearing sang, equip it
                int[] invItems = RSInterface.interfaceCache[3214].inv;
                for (int i = 0; i < invItems.length; i++) {
                    if (invItems[i] == sangId) {
                        equipItem(3214, i, invItems[i]);
                    }
                }
            } else if (Arrays.stream(client.getLocalPlayer().getPlayerComposition().getEquipmentIds()).noneMatch(e -> e == kodaiId + 511)) {
                // if not wearing kodai, equip it
                int[] invItems = RSInterface.interfaceCache[3214].inv;
                for (int i = 0; i < invItems.length; i++) {
                    if (invItems[i] == kodaiId) {
                        equipItem(3214, i, invItems[i]);
                    }
                }
            } else {
                // attack bloat
                findAndAttackNpc("Bloat");
            }
        } else if (currentState == ScriptState.IN_SANG) {
//            int blowpipeId = 12927;
            int blowpipeId = getItemIdByName("Toxic blowpipe") + 1;
            if (Arrays.stream(client.getLocalPlayer().getPlayerComposition().getEquipmentIds()).noneMatch(e -> e == blowpipeId + 511)) {
                // if not wearing sang, equip it
                int[] invItems = RSInterface.interfaceCache[3214].inv;
                for (int i = 0; i < invItems.length; i++) {
                    if (invItems[i] == blowpipeId) {
                        equipItem(3214, i, invItems[i]);
                    }
                }
            } else if (findAndAttackNpc("Maiden") == -1) {
                walkTo(3170, 4444);
            }
        } else if (currentState == ScriptState.IN_VERZIK) {
            int scytheId = 21007;
            int rapierId = 21003;
//            int scytheId = getItemIdByName("Scythe of Vitur") + 1;
            if (Arrays.stream(client.getLocalPlayer().getPlayerComposition().getEquipmentIds()).noneMatch(e -> e == scytheId + 511)) {
                // if not wearing sang, equip it
                int[] invItems = RSInterface.interfaceCache[3214].inv;
                for (int i = 0; i < invItems.length; i++) {
                    if (invItems[i] == scytheId) {
                        equipItem(3214, i, invItems[i]);
                    }
                }
            } else if (Arrays.stream(client.getLocalPlayer().getPlayerComposition().getEquipmentIds()).noneMatch(e -> e == rapierId + 511)) {
                // if not wearing rapier, equip it
                int[] invItems = RSInterface.interfaceCache[3214].inv;
                for (int i = 0; i < invItems.length; i++) {
                    if (invItems[i] == rapierId) {
                        equipItem(3214, i, invItems[i]);
                    }
                }
            } else {
                findAndAttackNpc("Verzik");
            }
//            System.out.print("equipment: ");
//            for (int e : client.getLocalPlayer().getPlayerComposition().getEquipmentIds()) {
//                System.out.printf("%d, ", e);
//            }
//            System.out.println();
//            System.out.print("inventory: ");
//            for (int e : RSInterface.interfaceCache[3214].inv) {
//                System.out.printf("%d, ", e);
//            }
//            System.out.println();
        }

//        System.out.printf("ScriptState: %s%n", currentState);
//        System.out.printf("Player at %s%n", client.getLocalPlayer().getWorldLocation());
        lastScriptTick = System.currentTimeMillis();
    }

    private int getItemIdByName(String name) {
        int id = getOSRSItemIdByName(name);
        if (id < 0) {
            return get317ItemIdByName(name);
        } else {
            return id;
        }
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
            if (npc == null || npc.getName() == null) {
                continue;
            }
            if (npc.getName().contains(npcName)) {
                attackNpc(i);
                return i;
            }
        }
        return -1;
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
        stream.method433(x); //  + online.paescape.Client.baseX
        stream.writeInt(id);
        stream.method432(y); // + online.paescape.Client.baseY
    }

    void walkTo(int x, int y) {
        Stream stream = online.paescape.Client.stream;
        stream.createFrame(229);
        stream.writeByte(online.paescape.Client.instance.plane);
        stream.createFrame(98); // COMMAND_MOVEMENT_OPCODE
        stream.writeWordBigEndian(5); // maxPathSize + maxPathSize + 3
        stream.writeSignedBigEndian(x); // first step x

        // send steps

        stream.writeUnsignedWordBigEndian(y); // first step y
        stream.writeByteC(0); // super.keyArray[5] != 1 ? 0 : 1; no idea what this is
    }

    boolean inLobby() {
        if (client.getLocalPlayer() == null) return false;
        return client.getLocalPlayer().getWorldLocation().distanceTo(new WorldPoint(3670, 3219, 0)) <= 5;
    }

    boolean inBloatRoom() {
        if (client.getLocalPlayer() == null) return false;
        return client.getLocalPlayer().getWorldLocation().distanceTo(new WorldPoint(3296, 4443, 0)) <= 7;
    }

    boolean inSangRoom() {
        if (client.getLocalPlayer() == null) return false;
        return client.getLocalPlayer().getWorldLocation().distanceTo(new WorldPoint(3176, 4446, 0)) <= 14;
    }

    boolean inVerzikRoom() {
        if (client.getLocalPlayer() == null) return false;
        return client.getLocalPlayer().getWorldLocation().distanceTo(new WorldPoint(3168, 4313, 0)) <= 7;
    }

    private void click(int x, int y) {
        online.paescape.Client c = online.paescape.Client.instance;
        MouseEvent[] me = {
                new MouseEvent(c, MouseEvent.MOUSE_MOVED, System.currentTimeMillis(), 0, x, y, 0, false, 0),
                new MouseEvent(c, MouseEvent.MOUSE_PRESSED, System.currentTimeMillis() + 1, 0, x, y, 1, false, MouseEvent.BUTTON1),
                new MouseEvent(c, MouseEvent.MOUSE_RELEASED, System.currentTimeMillis() + 2, 0, x, y, 1, false, MouseEvent.BUTTON1)
        };

        System.out.printf("cleek %s%n", me[1]);

        for (MouseEvent me1 : me) {
            c.getToolkit().getSystemEventQueue().postEvent(me1);
        }
    }

    @Subscribe()
    private void onGameObjectSpawned(GameObjectSpawned gameObjectSpawned) {
//        System.out.println(gameObjectSpawned);
    }

    private boolean hasAttackOption(NPC npc) {
        return Arrays.stream(npc.getComposition().getActions()).anyMatch(a -> a != null && a.contains("Attack"));
    }
}