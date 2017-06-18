package scripts.TRuneCrafting.tasks;

import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import scripts.TRuneCrafting.resources.MyConstants;
import scripts.TRuneCrafting.resources.Task;
import scripts.TRuneCrafting.resources.Walker;

/**
 * Created by Tyskie on 18-6-2017.
 */
public class WalkToRuins extends Task {

    public Tile[] pathToRuins;
    private final Walker walker = new Walker(ctx);
    private int runeId;

    public WalkToRuins(ClientContext ctx, Tile[] pathToRuins, int runeId) {
        super(ctx);
        this.pathToRuins = pathToRuins;
        this.runeId = runeId;
    }

    @Override
    public boolean activate() {
        return (ctx.inventory.count() == MyConstants.INVENTORY_FULL
                && ctx.inventory.select().id(MyConstants.PURE_ESSENCE).count() == MyConstants.INVENTORY_FULL)
                || (ctx.inventory.count() == MyConstants.INVENTORY_ONLY_RUNES
                && ctx.inventory.select().id(runeId).count() == 1
                && pathToRuins[0].distanceTo(ctx.players.local()) > 5);
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
            if(ctx.inventory.select().count() == MyConstants.INVENTORY_FULL
                    && ctx.inventory.select().id(MyConstants.PURE_ESSENCE).count() == 28) {
                walker.walkPath(pathToRuins);
            } else {
                walker.walkPathReverse(pathToRuins);
            }
        }
    }
}
