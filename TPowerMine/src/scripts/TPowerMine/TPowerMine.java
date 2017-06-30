package scripts.TPowerMine;

import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;
import scripts.TPowerMine.resources.MyConstants;
import scripts.TPowerMine.resources.Task;
import scripts.TPowerMine.tasks.Drop;
import scripts.TPowerMine.tasks.Mine;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tyskie on 20-6-2017.
 */
@Script.Manifest(name = "Tyskie's TPowerMine", description = "Tyskie's TPowerMine powermines ores and drops them.", properties = "author=Tyskie; topic=999; client=4")
public class TPowerMine extends PollingScript<ClientContext> implements PaintListener {

    private List<Task> taskList = new ArrayList<Task>();
    private int startExp, expGained;
    private double expEachOre = 1;
    private int[] rockIds = {};

    @Override
    public void start(){
        rockIds = MyConstants.IRON_ROCK_IDS;
        expEachOre = MyConstants.IRON_EXP;

        taskList.add(new Drop(ctx));
        taskList.add(new Mine(ctx, rockIds));

        startExp = ctx.skills.experience(Constants.SKILLS_MINING);
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
        System.out.println("Thanks for using Tyskie's TPowerMine!");
        System.out.println("Runned For: " + getRunningTime());
        expGained = ctx.skills.experience(Constants.SKILLS_MINING) - startExp;
        System.out.println("Mining Exp Gained: " + expGained);
        System.out.println("Ores Mined: " + (int) Math.round(expGained / expEachOre));
    }

    @Override
    public void repaint(Graphics graphics) {
        expGained = ctx.skills.experience(Constants.SKILLS_MINING) - startExp;

        Graphics2D g = (Graphics2D) graphics;

        g.setColor(new Color(128, 128, 128, 180));
        g.fillRect(5, 246, 200, 90);

        g.setColor(new Color(255, 255, 255));
        g.drawRect(5, 246, 200, 90);

        g.drawString("Tyskie's TPowerMine", 10, 266);
        g.drawString("Running For: " + getRunningTime(), 10, 286);
        g.drawString("Mining Exp Gained: " + expGained, 10, 306);
        g.drawString("Ores Mined: " + (int) Math.round(expGained / expEachOre), 10, 326);
    }

    private String getRunningTime(){
        long milliseconds = this.getTotalRuntime();
        long seconds = (milliseconds / 1000) % 60;
        long minutes = (milliseconds / (1000 * 60) % 60);
        long hours = (milliseconds / (1000 * 60 * 60)) % 24;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
