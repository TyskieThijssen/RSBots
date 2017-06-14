package scripts.BarbFishNCook.tasks;

import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import scripts.BarbFishNCook.resources.Task;
import scripts.BarbFishNCook.resources.Walker;

public class Walk extends Task {

    public Tile[] pathToBank;
    private final Walker walker = new Walker(ctx);
    private int[] cookedFoodIds;
    private int[] foodIds;

    public Walk(ClientContext ctx, Tile[] pathToBank, int[] cookedFoodIds, int[] foodIds) {
        super(ctx);
        this.pathToBank = pathToBank;
        this.cookedFoodIds = cookedFoodIds;
        this.foodIds = foodIds;
    }

    @Override
    public boolean activate() {
        return (ctx.inventory.select().count() > 27 && (ctx.inventory.select().id(foodIds[0]).count() == 0) && (ctx.inventory.select().id(cookedFoodIds[0]).count() > 1) && (ctx.inventory.select().id(foodIds[1]).count() == 0) && (ctx.inventory.select().id(cookedFoodIds[1]).count() > 1)) || (ctx.inventory.select().count() < 28 && pathToBank[0].distanceTo(ctx.players.local()) > 10);
    }

    @Override
    public void execute() {
        if(!ctx.movement.running() && ctx.movement.energyLevel() > Random.nextInt(20, 35)){
            ctx.movement.running(true);
        }
        if(!ctx.players.local().inMotion() || ctx.movement.destination().equals(Tile.NIL) || ctx.movement.destination().distanceTo(ctx.players.local()) < 5){
            if(ctx.inventory.select().count() > 27 && (ctx.inventory.select().id(foodIds[0]).count() == 0) && (ctx.inventory.select().id(cookedFoodIds[0]).count() > 1) && (ctx.inventory.select().id(foodIds[1]).count() == 0) && (ctx.inventory.select().id(cookedFoodIds[1]).count() > 1)) {
                walker.walkPath(pathToBank);
            } else {
                walker.walkPathReverse(pathToBank);
            }
        }
    }
}
