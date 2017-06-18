package scripts.TRuneCrafting.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import scripts.TRuneCrafting.resources.MyConstants;
import scripts.TRuneCrafting.resources.Task;

import java.util.concurrent.Callable;

/**
 * Created by Tyskie on 18-6-2017.
 */
public class CraftRunes extends Task {

    private int runeId;

    public CraftRunes(ClientContext ctx, int runeId) {
        super(ctx);
        this.runeId = runeId;
    }

    @Override
    public boolean activate() {
        return ctx.players.local().animation() == MyConstants.ANIMATION_IDLE
                && ctx.inventory.count() == MyConstants.INVENTORY_FULL
                && ctx.inventory.select().id(MyConstants.PURE_ESSENCE).count() == MyConstants.INVENTORY_FULL
                && ctx.objects.select().id(MyConstants.MYSTERIOUS_RUNES_ID).nearest().poll().valid();
    }

    @Override
    public void execute() {
        enterRuins();
        craftRunes();
        leaveRuins();
    }

    private void enterRuins(){
        final GameObject ruins = ctx.objects.select().id(MyConstants.MYSTERIOUS_RUNES_ID).nearest().poll();
        if (ruins.inViewport() && ruins.valid()){
            ruins.hover();
            Condition.sleep(Random.nextInt(250, 500));
            ruins.interact("Enter", "Mysterious ruins");
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.players.local().animation() == MyConstants.ANIMATION_IDLE
                            && !ruins.valid();
                }
            }, 250, 20);
        } else {
            ctx.camera.turnTo(ruins);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ruins.inViewport();
                }
            }, 250, 10);
        }
    }

    private void craftRunes(){
        final GameObject altar = ctx.objects.select().id(MyConstants.ALTAR_ID).nearest().poll();
        if (altar.inViewport() && altar.valid()){
            altar.hover();
            Condition.sleep(Random.nextInt(250, 500));
            altar.interact("Craft-rune", "Altar");
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.inventory.count() == MyConstants.INVENTORY_ONLY_RUNES
                            && ctx.inventory.select().id(runeId).count() == 1;
                }
            }, 250, 20);
        } else {
            ctx.camera.turnTo(altar);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return altar.inViewport();
                }
            }, 250, 10);
        }
    }

    private void leaveRuins(){
        final GameObject portal = ctx.objects.select().id(MyConstants.PORTAL_ID).nearest().poll();
        if (portal.inViewport() && portal.valid()){
            portal.hover();
            Condition.sleep(Random.nextInt(250, 500));
            portal.interact("Use", "Portal");
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return !portal.inViewport();
                }
            }, 250, 20);
        } else {
            ctx.camera.turnTo(portal);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return portal.inViewport();
                }
            }, 250, 10);
        }
    }
}
