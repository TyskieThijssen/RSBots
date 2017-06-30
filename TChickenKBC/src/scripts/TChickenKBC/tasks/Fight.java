package scripts.TChickenKBC.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.Filter;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Npc;
import scripts.TChickenKBC.resources.MyConstants;
import scripts.TChickenKBC.resources.Task;

import java.util.concurrent.Callable;

/**
 * Created by Tyskie on 27-6-2017.
 */
public class Fight extends Task {

    public Fight(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return !ctx.players.local().inCombat();
    }

    @Override
    public void execute() {
        Npc chickenToAttack = ctx.npcs.select().id(MyConstants.CHICKEN_IDS).select(new Filter<Npc>() {
            @Override
            public boolean accept(Npc npc) {
                    return !npc.inCombat();
            }
        }).nearest().poll();

        if (!chickenToAttack.inViewport()){
            ctx.camera.turnTo(chickenToAttack);
        }

        chickenToAttack.interact("Attack", "Chicken");

        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.players.local().inCombat();
            }
        }, 250, 10);
    }
}
