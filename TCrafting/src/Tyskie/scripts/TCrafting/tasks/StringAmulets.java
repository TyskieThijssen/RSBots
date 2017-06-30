package Tyskie.scripts.TCrafting.tasks;

import Tyskie.scripts.TCrafting.resources.Actions;
import Tyskie.scripts.TCrafting.resources.Antiban;
import Tyskie.scripts.TCrafting.resources.MyConstants;
import Tyskie.scripts.TCrafting.resources.Task;
import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Component;
import org.powerbot.script.rt4.Item;

import java.util.concurrent.Callable;

/**
 * Created by Tyskie on 30-6-2017.
 */
public class StringAmulets extends Task {

    private int amuletId, parentWidget, childWidget;
    private boolean isStringing;
    private Antiban antiban;
    private Actions actions;

    public StringAmulets(ClientContext ctx, int amuletId, int parentWidget, int childWidget) {
        super(ctx);
        this.amuletId = amuletId;
        this.parentWidget = parentWidget;
        this.childWidget = childWidget;
        isStringing = false;
        antiban = new Antiban();
        actions = new Actions();
    }

    @Override
    public boolean activate() {
        return ctx.inventory.select().count() == MyConstants.INVENTORY_FULL
                && ctx.inventory.select().id(amuletId).count() >= 1
                && ctx.inventory.select().id(MyConstants.BALL_OF_WOOL).count() >= 1
                && !isStringing;
    }

    @Override
    public void execute() {
        Component inventory = ctx.widgets.component(MyConstants.INVENTORY_PARENT, MyConstants.INVENTORY_CHILD);
        final Component amuletInterface = ctx.widgets.component(parentWidget, childWidget);

        Item ballOfWool = ctx.inventory.select().id(MyConstants.BALL_OF_WOOL).poll();
        Item amuletToCraft = ctx.inventory.select().id(amuletId).poll();

        if (ballOfWool.valid() && ballOfWool.inViewport()){
            ballOfWool.hover();
            Condition.sleep(Random.nextInt(250, 500));
            ballOfWool.interact("Use", "Ball of wool");
            Condition.sleep(Random.nextInt(250, 500));

            if (amuletToCraft.valid() && amuletToCraft.inViewport()){
                amuletToCraft.hover();
                Condition.sleep(Random.nextInt(250, 500));
                amuletToCraft.interact("Use", "Ball of wool ->");
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return amuletInterface.valid();
                    }
                }, 250, 10);

                if (amuletInterface.valid()) {
                    amuletInterface.interact("Make", "All");
                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return ctx.players.local().animation() == MyConstants.ANIMATION_STRING_AMULET;
                        }
                    }, 250, 10);

                    isStringing = !isStringing;
                    while (isStringing) {
                        actions.checkForBoolean(isStringing, amuletId);
                    }
                }
            } else {
                //open inventory tab
                actions.openInventoryTab(inventory);
            }
        } else {
            //open inventory tab
            actions.openInventoryTab(inventory);
        }
    }
}
