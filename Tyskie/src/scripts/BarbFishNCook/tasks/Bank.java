package scripts.BarbFishNCook.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import scripts.BarbFishNCook.Task;

import java.util.concurrent.Callable;

public class Bank extends Task {

    private int[] fishingSupplies;

    public Bank(ClientContext ctx, int[] fishingSupplies) {
        super(ctx);
        this.fishingSupplies = fishingSupplies;
    }

    @Override
    public boolean activate() {
        return ctx.inventory.select().count() > 27 && ctx.bank.nearest().tile().distanceTo(ctx.players.local()) < 6;
    }

    @Override
    public void execute() {
        if(ctx.bank.opened()){
            if(ctx.bank.depositAllExcept(fishingSupplies)){
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.inventory.select().count() == 2 && ctx.inventory.contains(ctx.inventory.select().id(fishingSupplies[0]).poll()) && ctx.inventory.contains(ctx.inventory.select().id(fishingSupplies[1]).poll());
                    }
                }, 250, 20);
            }
        } else {
            if(ctx.bank.inViewport()) {
                if(ctx.bank.open()){
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
