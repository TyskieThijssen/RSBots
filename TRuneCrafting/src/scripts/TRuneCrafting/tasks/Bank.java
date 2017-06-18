package scripts.TRuneCrafting.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;
import scripts.TRuneCrafting.resources.MyConstants;
import scripts.TRuneCrafting.resources.Task;

import java.util.concurrent.Callable;

/**
 * Created by Tyskie on 18-6-2017.
 */
public class Bank extends Task {

    private int essenceId;

    public Bank(ClientContext ctx, int essenceId) {
        super(ctx);
        this.essenceId = essenceId;
    }

    @Override
    public boolean activate() {
        return (ctx.players.local().animation() == MyConstants.ANIMATION_IDLE
                && ctx.inventory.select().id(MyConstants.AIR_RUNE).count() == 1
                && ctx.bank.nearest().tile().distanceTo(ctx.players.local().tile()) < 5)
                ||
                (ctx.players.local().animation() == MyConstants.ANIMATION_IDLE
                && ctx.inventory.count() == MyConstants.INVENTORY_EMPTY
                && ctx.bank.nearest().tile().distanceTo(ctx.players.local().tile()) < 5);
    }

    @Override
    public void execute() {
        if (ctx.bank.opened()){
            if (ctx.bank.currentTab() != 0){
                ctx.bank.currentTab(0);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.bank.currentTab(0);
                    }
                }, 250, 10);
            }
            if (ctx.bank.depositInventory()){
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.inventory.count() == MyConstants.INVENTORY_EMPTY;
                    }
                }, 250, 10);

                if (ctx.bank.contains(ctx.bank.select().id(essenceId).peek())) {
                    ctx.bank.withdraw(essenceId, Random.nextInt(28, 99));
                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return ctx.inventory.select().id(essenceId).count() == MyConstants.INVENTORY_FULL;
                        }
                    }, 250, 10);
                } else {
                    ctx.controller.stop();
                }

                ctx.bank.close();
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return !ctx.bank.opened();
                    }
                }, 250, 10);
            }
        } else {
            if (ctx.bank.inViewport()){
                if (ctx.bank.open()){
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
}
