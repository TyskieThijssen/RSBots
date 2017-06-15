package scripts.BarbFishNCook.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import scripts.BarbFishNCook.resources.MyConstants;
import scripts.BarbFishNCook.resources.Task;
import scripts.BarbFishNCook.resources.Walker;

public class WalkToFire extends Task {

    public Tile[] pathToFire;
    private final Walker walker = new Walker(ctx);
    private int[] cookedFoodIds;
    private int[] rawFoodIds;
    private boolean walkedToFire = false;

    public WalkToFire(ClientContext ctx, Tile[] pathToFire, int[] cookedFoodIds, int[] rawFoodIds) {
        super(ctx);
        this.pathToFire = pathToFire;
        this.cookedFoodIds = cookedFoodIds;
        this.rawFoodIds = rawFoodIds;
    }

    @Override
    public boolean activate() {
        return (ctx.inventory.select().count() == MyConstants.INVENTORY_FULL
                && (ctx.inventory.select().id(rawFoodIds[0]).count() > 1)
                && (ctx.inventory.select().id(cookedFoodIds[0]).count() == 0)
                && (ctx.inventory.select().id(rawFoodIds[1]).count() > 1)
                && (ctx.inventory.select().id(cookedFoodIds[1]).count() == 0))
                && !walkedToFire;
    }

    @Override
    public void execute() {
        if(!ctx.movement.running()
                && ctx.movement.energyLevel() > Random.nextInt(20, 35)){
            ctx.movement.running(true);
        }
        if(!ctx.players.local().inMotion()
                || ctx.movement.destination().equals(Tile.NIL)
                || ctx.movement.destination().distanceTo(ctx.players.local()) < 5){
            walker.walkPath(pathToFire);
            walkedToFire = !walkedToFire;
            Condition.sleep(Random.nextInt(500, 1000));
        }
    }
}
