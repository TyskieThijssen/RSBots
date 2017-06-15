package scripts.BarbFishNCook.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Item;
import scripts.BarbFishNCook.resources.MyConstants;
import scripts.BarbFishNCook.resources.Task;

import java.util.concurrent.Callable;

public class Cook extends Task {

    private int[] rawFoodIds;
    boolean isCooking = false;

    public Cook(ClientContext ctx, int[] rawFoodIds) {
        super(ctx);
        this.rawFoodIds = rawFoodIds;
    }

    @Override
    public boolean activate() {
        return ctx.players.local().animation() == MyConstants.ANIMATION_IDLE
                && ctx.inventory.select().count() == MyConstants.INVENTORY_FULL
                && ((ctx.inventory.select().id(rawFoodIds[0]).count() > 1) || (ctx.inventory.select().id(rawFoodIds[1]).count() > 1));
    }

    @Override
    public void execute() {
        cookFood(rawFoodIds);
    }

    private void cookFood(final int[] rawFoodIds){
        for (final int rawFoodId : rawFoodIds) {
            if (ctx.inventory.select().id(rawFoodId).count() > 1) {
                Item food = ctx.inventory.select().id(rawFoodId).poll();
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
                    if (Random.nextDouble() > 0.3){
                        hoverOverSkill(rawFoodId);
                    }
                }

                while(isCooking) {
                    if (ctx.widgets.component(233, 2).valid()){
                        isCooking = false;
                    }
                    if (ctx.inventory.select().id(rawFoodId).count() == 0) {
                        isCooking = !isCooking;
                    }
                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return ctx.players.local().animation() == MyConstants.ANIMATION_IDLE;
                        }
                    });
                    Condition.sleep(Random.nextInt(50, 150));
                }
            }
        }
    }

    private void hoverOverSkill(final int foodId){
        if (ctx.widgets.component(548, 48).valid()){
            ctx.widgets.component(548, 48).hover();
            Condition.sleep(Random.nextInt(250, 500));
            ctx.widgets.component(548, 48).click();
            if (ctx.widgets.component(320, 20).valid()){
                ctx.widgets.component(320, 20).hover();

                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.players.local().animation() == MyConstants.ANIMATION_IDLE
                                && ctx.inventory.select().id(foodId).count() == 0;
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
