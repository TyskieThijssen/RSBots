package scripts.BarbFishNCook.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Npc;
import scripts.BarbFishNCook.Task;

import java.util.concurrent.Callable;

public class Fish extends Task {

    int[] fishIds;

    Tile fishingSpotLocation = Tile.NIL;

    public Fish(ClientContext ctx, int[] fishIds) {
        super(ctx);
        this.fishIds = fishIds;
    }

    @Override
    public boolean activate() {
        return ctx.inventory.count() != 28 && ctx.players.local().animation() == -1;
    }

    @Override
    public void execute() {
        Npc fishingSpotToFish = ctx.npcs.select().id(fishIds).nearest().poll();

        fishingSpotLocation = fishingSpotToFish.tile();

        if(!fishingSpotToFish.inViewport()){
            ctx.camera.turnTo(fishingSpotToFish);
        }

        fishingSpotToFish.interact("Lure");

        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.players.local().animation() != -1;
            }
        }, 250, 20);
    }
}
