package net.runelite.client.plugins.microbot.woodcutting;

import net.runelite.api.coords.WorldPoint;
import net.runelite.client.plugins.microbot.Microbot;
import net.runelite.client.plugins.microbot.Script;
import net.runelite.client.plugins.microbot.util.combat.Rs2Combat;
import net.runelite.client.plugins.microbot.util.equipment.Rs2Equipment;
import net.runelite.client.plugins.microbot.util.gameobject.Rs2GameObject;
import net.runelite.client.plugins.microbot.util.inventory.Inventory;

import java.util.concurrent.TimeUnit;

public class AutoWoodcuttingScript extends Script {

    public static double version = 1.2;

    public boolean run(AutoWoodcuttingConfig config) {
        WorldPoint startPoint = Microbot.getClient().getLocalPlayer().getWorldLocation();
        mainScheduledFuture = scheduledExecutorService.scheduleWithFixedDelay(() -> {
            if (!super.run()) return;
            try {
                if (Rs2Equipment.hasEquipped("Dragon axe"))
                    Rs2Combat.setSpecState(true, 1000);
                if (Microbot.isMoving() || Microbot.isAnimating() || Microbot.pauseAllScripts) return;
                if (Inventory.isFull()) {
                    if (config.hasAxeInventory()) {
                        Inventory.dropAllStartingFrom(1);
                    } else {
                        Inventory.dropAll();
                    }
                    return;
                }
                var tree = Rs2GameObject.findObject(config.TREE().getName(), true, config.distanceToStray(), startPoint);

                if (tree != null){
                    Rs2GameObject.interact(tree, config.TREE().getAction());
                }else {
                    System.out.println("No trees in zone");
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }, 0, 1000, TimeUnit.MILLISECONDS);
        return true;
    }
}
