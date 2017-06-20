package scripts.TPowerMine.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import scripts.TPowerMine.resources.Antiban;
import scripts.TPowerMine.resources.MyConstants;
import scripts.TPowerMine.resources.Task;

import java.util.concurrent.Callable;

/**
 * Created by Tyskie on 20-6-2017.
 */
public class Mine extends Task {

    private int[] rockIds;
    private Tile rockLocation = Tile.NIL;
    private Antiban antiban;

    public Mine(ClientContext ctx, int[] rockIds) {
        super(ctx);
        this.rockIds = rockIds;
        antiban = new Antiban();
    }

    @Override
    public boolean activate() {
        return ctx.objects.select().at(rockLocation).id(rockIds).poll() == ctx.objects.nil()
                || ctx.players.local().animation() == MyConstants.ANIMATION_IDLE;
    }

    @Override
    public void execute() {
        if (Random.nextDouble() > 0.75){
            antiban.doAntibanAction(Random.nextInt(1, 10));
        }

        GameObject rockToMine = ctx.objects.select().id(rockIds).nearest().poll();

        rockLocation = rockToMine.tile();
        rockToMine.interact("Mine", "Rocks");

        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.players.local().animation() != MyConstants.ANIMATION_IDLE;
            }
        }, 200, 10);
    }

}
