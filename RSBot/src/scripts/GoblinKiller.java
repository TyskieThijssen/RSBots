package scripts;

import org.powerbot.script.Condition;
import org.powerbot.script.Filter;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.Item;
import org.powerbot.script.rt4.Npc;

import java.util.concurrent.Callable;

@Script.Manifest(name="Goblin Killer!", description = "Kills Goblins", properties = "author=Thijs; topic=999; client=4;")
public class GoblinKiller extends PollingScript<ClientContext>{
    final static int GOBLIN_IDS[] = {3029, 3030, 3031, 3032, 3033, 3034, 3035};
    final static int FOOD_ID = 315;

    @Override
    public void start(){

    }

    @Override
    public void stop(){

    }

    @Override
    public void poll() {
        if(hasFood()){
            // we have food
            if(needsHeal()){
                heal();
            } else if(shouldAttack()){
                attack();
            }
        }
    }

    public boolean needsHeal(){
        return ctx.combat.health() < 6;
    }

    public boolean shouldAttack(){
        return !ctx.players.local().inCombat();
    }

    public boolean hasFood(){
        return ctx.inventory.select().id(FOOD_ID).count() > 0;
    }

    public void attack(){
        final Npc goblinToAttack = ctx.npcs.select().id(GOBLIN_IDS).select(new Filter<Npc>() {
            @Override
            public boolean accept(Npc npc) {
                return !npc.inCombat();
            }
        }).nearest().poll();

        goblinToAttack.interact("Attack");

        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.players.local().inCombat();
            }
        }, 200, 20);
    }

    public void heal(){
        Item foodToEat = ctx.inventory.select().id(FOOD_ID).poll();
        foodToEat.interact("Eat");

        final int startingHealth = ctx.combat.health();

        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                final int currentHealth = ctx.combat.health();
                return currentHealth != startingHealth;
            }
        }, 150, 20);

    }
}
