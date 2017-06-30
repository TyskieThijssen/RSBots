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
public class CutGems extends Task {

    private int gemId, parentWidget, childWidget;
    private boolean isCutting;
    private Antiban antiban;
    private Actions actions;

    public CutGems(ClientContext ctx, int gemId, int parentWidget, int childWidget) {
        super(ctx);
        this.gemId = gemId;
        this.parentWidget = parentWidget;
        this.childWidget = childWidget;
        isCutting = false;
        antiban = new Antiban();
        actions = new Actions();
    }

    @Override
    public boolean activate() {
        return ctx.inventory.select().id(MyConstants.CHISEL).count() >= 1
                && ctx.inventory.select().count() == MyConstants.INVENTORY_FULL
                && ctx.inventory.select().id(gemId).count() >= 1
                && !isCutting;
    }

    @Override
    public void execute() {
        Component inventory = ctx.widgets.component(MyConstants.INVENTORY_PARENT, MyConstants.INVENTORY_CHILD);
        final Component gemInterface = ctx.widgets.component(parentWidget, childWidget);

        Item chisel = ctx.inventory.select().id(MyConstants.CHISEL).poll();
        Item gemToCut = ctx.inventory.select().id(gemId).poll();

        if (chisel.valid() && chisel.inViewport()){
            chisel.hover();
            Condition.sleep(Random.nextInt(250, 500));
            chisel.interact("Use", "Chisel");
            Condition.sleep(Random.nextInt(250, 500));

            if (gemToCut.valid() && gemToCut.inViewport()){
                gemToCut.hover();
                Condition.sleep(Random.nextInt(250, 500));
                gemToCut.interact("Use", "Chisel -> Uncut");
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return gemInterface.valid();
                    }
                }, 250, 10);

                if (gemInterface.valid()) {
                    gemInterface.interact("Make", "All");
                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return ctx.players.local().animation() == MyConstants.ANIMATION_CUT_GEM;
                        }
                    }, 250, 10);

                    isCutting = !isCutting;
                    while (isCutting) {
                        actions.checkForBoolean(isCutting, gemId);
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
