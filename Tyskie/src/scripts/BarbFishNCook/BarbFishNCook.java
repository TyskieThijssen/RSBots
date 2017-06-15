package scripts.BarbFishNCook;

import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.ClientContext;
import scripts.BarbFishNCook.resources.MyConstants;
import scripts.BarbFishNCook.resources.Task;
import scripts.BarbFishNCook.tasks.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Script.Manifest(name="BarbFishNCook", description = "Tyskie's BarbFishNCook fishes trout/salmon at Barbarian Village and cooks them on the infinite fire.", properties = "author=Tyskie; topic=999; client=4;")
public class BarbFishNCook extends PollingScript<ClientContext> implements PaintListener {

    List<Task> taskList = new ArrayList<Task>();

    @Override
    public void start(){
        taskList.add(new Bank(ctx, MyConstants.FISHING_SUPPLIES_IDS));
        taskList.add(new WalkToBank(ctx, MyConstants.FISHING_TO_BANK, MyConstants.COOKED_FISH_IDS, MyConstants.RAW_FISH_IDS));
        taskList.add(new Fish(ctx, MyConstants.FISHING_SPOT_IDS));
        taskList.add(new WalkToFire(ctx, MyConstants.FISHING_TO_FIRE, MyConstants.COOKED_FISH_IDS, MyConstants.RAW_FISH_IDS));
        taskList.add(new Cook(ctx, MyConstants.RAW_FISH_IDS));
//        taskList.add(new Drop(ctx));
    }

    @Override
    public void poll() {
        for (Task task : taskList) {
            if(ctx.controller.isStopping()){
                break;
            }

            if(task.activate()){
                task.execute();
                break;
            }
        }
    }

    @Override
    public void stop(){
        System.out.println("Thanks for using BarbFishNCook!");
    }

    @Override
    public void repaint(Graphics graphics) {

    }
}
