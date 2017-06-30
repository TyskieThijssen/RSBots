package Tyskie.scripts.TCrafting.resources;

import Tyskie.scripts.TCrafting.TCrafting;
import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.Component;

import java.util.concurrent.Callable;

/**
 * Created by Tyskie on 30-6-2017.
 */
public class Actions extends TCrafting {

    private Antiban antiban = new Antiban();

    public void openInventoryTab(Component inventory) {
        if (inventory.valid()){
            inventory.hover();
            Condition.sleep(Random.nextInt(250, 500));
            inventory.click();
            Condition.sleep(Random.nextInt(250, 500));
        }
    }

    public boolean checkForBoolean(boolean booleanToCheck, int itemId) {
        if (Random.nextDouble() > 0.75){
            antiban.doAntibanAction(Random.nextInt(1,10));
        }

        if (ctx.inventory.select().id(itemId).count() == 0){
            booleanToCheck = !booleanToCheck;
        }
        Condition.sleep(Random.nextInt(1500, 3500));

        return booleanToCheck;
    }

    public void openBank() {
        if (ctx.bank.inViewport()) {
            if (ctx.bank.open()) {
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.bank.opened();
                    }
                }, 250, 10);
            }
        } else {
            ctx.camera.turnTo(ctx.bank.nearest());
        }
    }
}
