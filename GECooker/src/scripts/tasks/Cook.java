package scripts.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Item;
import scripts.resources.Antiban;
import scripts.resources.MyConstants;
import scripts.resources.Task;

import java.util.concurrent.Callable;

/**
 * Created by Tyskie on 16-6-2017.
 */
public class Cook extends Task {

    private int foodId;
    private boolean isCooking = false;
    private Antiban antiban;

    public Cook(ClientContext ctx, int foodId) {
        super(ctx);
        this.foodId = foodId;
        antiban = new Antiban();
    }

    @Override
    public boolean activate() {
        System.out.println("activated cook");
        GameObject fire = ctx.objects.select().id(MyConstants.FIRE_ID).poll();
        return ctx.players.local().animation() == MyConstants.ANIMATION_IDLE
                && ctx.inventory.count() == MyConstants.INVENTORY_FULL
                && ctx.inventory.select().id(foodId).count() > 1
                && !isCooking
                && ctx.movement.distance(fire.tile()) > 1;
    }

    @Override
    public void execute() {
        cookFood(foodId);
    }

    private void cookFood(int foodId) {
        if (ctx.inventory.select().id(foodId).count() > 1) {
            Item food = ctx.inventory.select().id(foodId).poll();
            food.interact("Use");

            GameObject fire = ctx.objects.select().id(MyConstants.FIRE_ID).nearest().poll();

            if (!fire.inViewport()) {
                while(!fire.inViewport()) {
                    ctx.camera.turnTo(fire);
                }
            }

            fire.doSetBounds(MyConstants.FIRE_BOUNDS);
            fire.hover();
            Condition.sleep(Random.nextInt(100, 250));
            fire.interact(false, "Use", "-> fire");

            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.widgets.component(307, 2).valid();
                }
            });

            if (ctx.widgets.component(307, 2).valid()) {
                ctx.widgets.component(307, 2).hover();
                Condition.sleep(Random.nextInt(100, 250));
                ctx.widgets.component(307, 2).interact("Cook All");
            }

            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.players.local().animation() == MyConstants.ANIMATION_COOKING;
                }
            });

            if (ctx.players.local().animation() == MyConstants.ANIMATION_COOKING) {
                isCooking = true;
                if (Random.nextDouble() > 0.75){
                    antiban.doAntibanAction(Random.nextInt(1, 10));
                }
            }

            while(isCooking) {
                if (Random.nextDouble() > 0.75){
                    antiban.doAntibanAction(Random.nextInt(1, 10));
                }

                if (ctx.widgets.component(233, 2).valid()){
                    isCooking = !isCooking;
                }

                fire = ctx.objects.select().id(MyConstants.FIRE_ID).poll();
                System.out.println(ctx.movement.distance(fire.tile()));
                System.out.println(isCooking);
                if ((ctx.players.local().animation() == MyConstants.ANIMATION_IDLE
                        && ctx.inventory.select().id(foodId).count() == 0)
                        ||
                        (ctx.players.local().animation() == MyConstants.ANIMATION_IDLE
                                && ctx.movement.distance(fire.tile()) >= 1)) {
                    isCooking = !isCooking;
                    System.out.println(isCooking);
                }
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.players.local().animation() == MyConstants.ANIMATION_IDLE;
                    }
                });
                Condition.sleep(Random.nextInt(750, 1000));
            }
        }
    }
}
