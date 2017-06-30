package scripts.TChickenKBC.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GroundItem;
import scripts.TChickenKBC.resources.MyConstants;
import scripts.TChickenKBC.resources.Task;

import java.util.concurrent.Callable;

/**
 * Created by Tyskie on 27-6-2017.
 */
public class Loot extends Task {

    public Loot(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return ctx.groundItems.select().id(MyConstants.FEATHERS).nearest().size() >= 1;
    }

    @Override
    public void execute() {
        GroundItem featherToLoot = ctx.groundItems.select().id(MyConstants.FEATHERS).poll();

        if (!featherToLoot.inViewport()){
            ctx.camera.turnTo(featherToLoot);
        }

        featherToLoot.interact("Take", "Feather");

        final int startAmt = ctx.inventory.select().id(MyConstants.FEATHERS).size();

        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.inventory.select().id(MyConstants.FEATHERS).size() != startAmt;
            }
        }, 250, 10);
    }
}
