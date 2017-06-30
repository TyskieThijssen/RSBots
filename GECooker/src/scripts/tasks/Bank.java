package scripts.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;
import scripts.resources.MyConstants;
import scripts.resources.Task;

import java.util.concurrent.Callable;

/**
 * Created by Tyskie on 16-6-2017.
 */
public class Bank extends Task {

    private int foodId;

    public Bank(ClientContext ctx, int foodId) {
        super(ctx);
        this.foodId = foodId;
    }

    @Override
    public boolean activate() {
        System.out.println("activated bank");
        System.out.println(ctx.inventory.select().id(foodId).count());
        return ctx.inventory.select().id(foodId).count() == 0
                || ctx.inventory.count() != MyConstants.INVENTORY_FULL;
    }

    @Override
    public void execute() {
        if (ctx.bank.opened()){
            if (ctx.bank.depositInventory()){
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.inventory.count() != MyConstants.INVENTORY_FULL;
                    }
                }, 250, 10);

                ctx.bank.withdraw(foodId, Random.nextInt(28, 99));
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.inventory.count() == MyConstants.INVENTORY_FULL;
                    }
                }, 250, 10);

                ctx.bank.close();
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return !ctx.bank.open();
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
                    }, 250, 20);
                }
            } else {
                ctx.camera.turnTo(ctx.bank.nearest());
            }
        }
    }
}
