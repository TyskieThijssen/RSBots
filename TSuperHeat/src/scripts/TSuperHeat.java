package scripts;

import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;
import scripts.resources.Task;
import scripts.tasks.Bank;
import scripts.tasks.Superheat;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tyskie on 17-6-2017.
 */
@Script.Manifest(name="Tyskie's TSuperHeat", description = "Tyskie's TSuperHeat superheats ores into bars.", properties = "author=Tyskie; topic=999; client=4")
public class TSuperHeat extends PollingScript<ClientContext> implements PaintListener {

    private List<Task> taskList = new ArrayList<Task>();
    private int magicStartExp, smithingStartExp, magicExpGained, smithingExpGained = 0;

    @Override
    public void start(){
        taskList.add(new Superheat(ctx, 440, 2));
        taskList.add(new Bank(ctx, 440, 2));

        magicStartExp = ctx.skills.experience(Constants.SKILLS_MAGIC);
        smithingStartExp = ctx.skills.experience(Constants.SKILLS_SMITHING);
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
        System.out.println("Thanks for using Tyskie's TSuperHeat!");
        System.out.println("Runned for: " + getRunningTime());
        magicExpGained = ctx.skills.experience(Constants.SKILLS_MAGIC) - magicStartExp;
        smithingExpGained = ctx.skills.experience(Constants.SKILLS_SMITHING) - smithingStartExp;
        System.out.println("Magic Exp gained: " + magicExpGained);
        System.out.println("Smithing Exp gained: " + smithingExpGained);
    }

    @Override
    public void repaint(Graphics graphics) {
        magicExpGained = ctx.skills.experience(Constants.SKILLS_MAGIC) - magicStartExp;
        smithingExpGained = ctx.skills.experience(Constants.SKILLS_SMITHING) - smithingStartExp;

        Graphics2D g = (Graphics2D) graphics;

        g.setColor(new Color(128, 128, 128, 180));
        g.fillRect(5, 246, 175, 90);

        g.setColor(new Color(255, 255, 255));
        g.drawRect(5, 246, 175, 90);

        g.drawString("Tyskie's BarbFishNCook", 10, 266);
        g.drawString("Running For: " + getRunningTime(), 10, 286);
        g.drawString("Magic Exp Gained: " + magicExpGained, 10, 306);
        g.drawString("Smithing Exp Gained: " + smithingExpGained, 10, 326);
    }

    private String getRunningTime(){
        long milliseconds = this.getTotalRuntime();
        long seconds = (milliseconds / 1000) % 60;
        long minutes = (milliseconds / (1000 * 60) % 60);
        long hours = (milliseconds / (1000 * 60 * 60)) % 24;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
