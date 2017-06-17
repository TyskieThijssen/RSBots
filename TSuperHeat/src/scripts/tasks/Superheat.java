package scripts.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Component;
import org.powerbot.script.rt4.Item;
import scripts.resources.MyConstants;
import scripts.resources.Task;

import java.util.concurrent.Callable;

/**
 * Created by Tyskie on 17-6-2017.
 */
public class Superheat extends Task {

    private int oreId;
    private int coalNeeded;

    public Superheat(ClientContext ctx, int oreId, int coalNeeded) {
        super(ctx);
        this.oreId = oreId;
        this.coalNeeded = coalNeeded;
    }

    @Override
    public boolean activate() {
        System.out.println("activated superheat");
        return ctx.players.local().animation() == MyConstants.ANIMATION_IDLE
                && ctx.inventory.select().id(MyConstants.NATURE_ID).count() == 1
                && ctx.inventory.select().id(MyConstants.COAL_ID).count() >= coalNeeded
                && ctx.inventory.select().id(oreId).count() >= 1;
    }

    @Override
    public void execute() {
        if (ctx.inventory.select().id(MyConstants.NATURE_ID).count() == 1){
            if ((ctx.inventory.select().id(oreId).count() > 1)
                    && ctx.inventory.select().id(MyConstants.COAL_ID).count() >= coalNeeded){
                final Component superHeat = ctx.widgets.component(218, 26);
                if (!superHeat.valid()){
                    Component magicBook = ctx.widgets.component(548, 60);
                    if (magicBook.valid()){
                        magicBook.hover();
                        Condition.sleep(Random.nextInt(250, 500));
                        magicBook.click();
                        Condition.sleep(Random.nextInt(250, 500));
                    }
                }
                if (superHeat.valid()){
                    superHeat.hover();
                    Condition.sleep(Random.nextInt(50, 150));
                    superHeat.click();
                    Condition.sleep(Random.nextInt(50, 150));
                    Item oreToHeat = ctx.inventory.select().id(oreId).poll();
                    oreToHeat.hover();
                    Condition.sleep(Random.nextInt(50, 150));
                    oreToHeat.click();
                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return superHeat.valid();
                        }
                    }, 250, 10);
                }
            }
        } else {
            ctx.controller.stop();
        }
    }
}
