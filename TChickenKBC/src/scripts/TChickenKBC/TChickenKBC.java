package scripts.TChickenKBC;

import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;
import scripts.TChickenKBC.resources.Task;
import scripts.TChickenKBC.tasks.Fight;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tyskie on 27-6-2017.
 */
@Script.Manifest(name = "Tyskie's TChickenKBC", description = "Kills chickens then, if selected, buries the bones and, if selected, cooks the chickens.", properties = "author=Tyskie; topic=999; client=4")
public class TChickenKBC extends PollingScript<ClientContext> implements PaintListener {

    private List<Task> taskList = new ArrayList<Task>();
    private int startExp, expGained;

    @Override
    public void start(){
        taskList.add(new Fight(ctx));
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

    }

    @Override
    public void repaint(Graphics graphics) {
        expGained = ctx.skills.experience(Constants.SKILLS_WOODCUTTING) - startExp;

        Graphics2D g = (Graphics2D) graphics;

        g.setColor(new Color(128, 128, 128, 180));
        g.fillRect(5, 246, 200, 90);

        g.setColor(new Color(255, 255, 255));
        g.drawRect(5, 246, 200, 90);

        g.drawString("Tyskie's TChickenKBC", 10, 266);
        g.drawString("Running For: " + getRunningTime(), 10, 286);
        g.drawString("Exp Gained: " + expGained, 10, 306);
        g.drawString("Chickens : " + expGained, 10, 326);
    }

    private String getRunningTime(){
        long milliseconds = this.getTotalRuntime();
        long seconds = (milliseconds / 1000) % 60;
        long minutes = (milliseconds / (1000 * 60) % 60);
        long hours = (milliseconds / (1000 * 60 * 60)) % 24;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
