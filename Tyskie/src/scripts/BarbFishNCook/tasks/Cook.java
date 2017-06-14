package scripts.BarbFishNCook.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Item;
import scripts.BarbFishNCook.MyConstants;
import scripts.BarbFishNCook.Task;

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
        return ctx.players.local().animation() == -1 && ctx.inventory.select().count() > 27 && ((ctx.inventory.select().id(rawFoodIds[0]).count() > 1) || (ctx.inventory.select().id(rawFoodIds[1]).count() > 1));
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
                    ctx.camera.turnTo(fire);
                }

                fire.hover();
                Condition.sleep(Random.nextInt(100, 250));
                fire.interact(false, "Use");

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
                        return ctx.players.local().animation() == 897;
                    }
                });

                if (ctx.players.local().animation() == 897) {
                    isCooking = true;
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
                            return ctx.players.local().animation() == -1;
                        }
                    });
                    Condition.sleep(Random.nextInt(50, 150));
                }
            }
        }
    }

}
