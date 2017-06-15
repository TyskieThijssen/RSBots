package scripts.BarbFishNCook.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Npc;
import scripts.BarbFishNCook.resources.MyConstants;
import scripts.BarbFishNCook.resources.Task;

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
        return ctx.inventory.count() != MyConstants.INVENTORY_FULL
                && ctx.players.local().animation() == MyConstants.ANIMATION_IDLE;
    }

    @Override
    public void execute() {
        Npc fishingSpotToFish = ctx.npcs.select().id(fishIds).nearest().poll();

        fishingSpotLocation = fishingSpotToFish.tile();

        if(!fishingSpotToFish.inViewport()){
            ctx.camera.turnTo(fishingSpotToFish);
        }

        fishingSpotToFish.doSetBounds(MyConstants.FISHING_BOUNDS);
        fishingSpotToFish.hover();
        Condition.sleep(Random.nextInt(50, 150));
        fishingSpotToFish.interact("Lure", "Fishing spot");

        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.players.local().animation() == MyConstants.ANIMATION_FISHING;
            }
        }, 250, 20);

        if (Random.nextDouble() > 0.3) {
            System.out.println("hover skill");
            hoverOverSkill();
        } else {
            System.out.println("not hover skill");
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.players.local().animation() == MyConstants.ANIMATION_IDLE;
                }
            }, 250, 20);
        }
    }

    private void hoverOverSkill(){
        if (ctx.widgets.component(548, 48).valid()){
            ctx.widgets.component(548, 48).hover();
            Condition.sleep(Random.nextInt(250, 500));
            ctx.widgets.component(548, 48).click();
            if (ctx.widgets.component(320, 19).valid()){
                ctx.widgets.component(320, 19).hover();

                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.players.local().animation() != MyConstants.ANIMATION_FISHING;
                    }
                }, 250, 20);

                if (ctx.widgets.component(548, 50).valid()){
                    ctx.widgets.component(548, 50).hover();
                    Condition.sleep(Random.nextInt(250, 500));
                    ctx.widgets.component(548, 50).click();
                }
            }
        }
    }
}
