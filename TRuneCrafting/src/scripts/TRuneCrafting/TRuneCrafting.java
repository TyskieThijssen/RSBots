package scripts.TRuneCrafting;

import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.ClientContext;
import scripts.TRuneCrafting.resources.MyConstants;
import scripts.TRuneCrafting.resources.Task;
import scripts.TRuneCrafting.tasks.Bank;
import scripts.TRuneCrafting.tasks.CraftRunes;
import scripts.TRuneCrafting.tasks.WalkToRuins;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tyskie on 18-6-2017.
 */
@Script.Manifest(name="Tyskie's TRuneCrafting", description = "Tyskie's TRuneCrafting crafts your selected runes.", properties = "author=Tyskie; topic=999; client=4")
public class TRuneCrafting extends PollingScript<ClientContext> implements PaintListener {

    private List<Task> taskList = new ArrayList<Task>();

    @Override
    public void start(){
        taskList.add(new Bank(ctx, MyConstants.PURE_ESSENCE));
        taskList.add(new WalkToRuins(ctx, MyConstants.FALLY_BANK_AIR_ALTAR, MyConstants.AIR_RUNE));
        taskList.add(new CraftRunes(ctx, MyConstants.AIR_RUNE));
    }

    @Override
    public void poll() {
        for (Task task : taskList) {
            if (ctx.controller.isStopping()){
                break;
            }

            if (task.activate()){
                task.execute();
                break;
            }
        }
    }

    @Override
    public void stop(){
        System.out.println("Thanks for using Tyskie's TRuneCrafting!");
    }

    @Override
    public void repaint(Graphics graphics) {

    }
}
