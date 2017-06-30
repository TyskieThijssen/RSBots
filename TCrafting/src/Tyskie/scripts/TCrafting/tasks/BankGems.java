package Tyskie.scripts.TCrafting.tasks;

import Tyskie.scripts.TCrafting.resources.Actions;
import Tyskie.scripts.TCrafting.resources.MyConstants;
import Tyskie.scripts.TCrafting.resources.Task;
import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;

import java.util.concurrent.Callable;

/**
 * Created by Tyskie on 30-6-2017.
 */
public class BankGems extends Task {

    private int gemId;
    private Actions actions;

    public BankGems(ClientContext ctx, int gemId) {
        super(ctx);
        this.gemId = gemId;
        actions = new Actions();
    }

    @Override
    public boolean activate() {
        return (ctx.inventory.select().count() == MyConstants.INVENTORY_FULL
                && ctx.inventory.select().id(gemId).count() == 0
                && ctx.inventory.select().id(MyConstants.CHISEL).count() >= 1
                && ctx.bank.nearest().tile().distanceTo(ctx.players.local().tile()) < 5)
                || (ctx.inventory.select().count() != MyConstants.INVENTORY_FULL
                    && ctx.bank.nearest().tile().distanceTo(ctx.players.local().tile()) < 5);
    }

    @Override
    public void execute() {
        if (ctx.bank.opened()){
            //withdraw gems
            withdrawGems(gemId);
        } else {
            //open bank
            actions.openBank();
        }
    }

    private void withdrawGems(final int gemId){
        if (ctx.bank.currentTab() != 0){
            ctx.bank.currentTab(0);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.bank.currentTab(0);
                }
            }, 250, 10);
        }

        if (ctx.bank.depositAllExcept(MyConstants.CHISEL)){
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.inventory.select().count() == 1
                            && ctx.inventory.select().id(MyConstants.CHISEL).count() >= 1;
                }
            }, 250, 10);

            if (ctx.inventory.select().id(MyConstants.CHISEL).count() < 1){
                if (ctx.bank.contains(ctx.bank.select().id(MyConstants.CHISEL).peek())){
                    ctx.bank.withdraw(MyConstants.CHISEL, 1);
                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return ctx.inventory.select().id(MyConstants.CHISEL).count() >= 1;
                        }
                    }, 250, 10);
                } else {
                    //stop script
                    ctx.controller.stop();
                }
            }

            if (ctx.bank.contains(ctx.bank.select().id(gemId).peek())){
                ctx.bank.withdraw(gemId, Random.nextInt(27, 99));
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.inventory.select().id(gemId).count() >= 1;
                    }
                },250, 10);
            } else {
                //stop script
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
    }
}
