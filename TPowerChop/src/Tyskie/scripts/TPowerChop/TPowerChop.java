package Tyskie.scripts.TPowerChop;

import Tyskie.scripts.TPowerChop.tasks.Chop;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;
import Tyskie.scripts.TPowerChop.resources.MyConstants;
import Tyskie.scripts.TPowerChop.resources.Task;
import Tyskie.scripts.TPowerChop.tasks.Drop;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tyskie on 20-6-2017.
 */
@Script.Manifest(name = "Tyskie's TPowerChop", description = "Tyskie's TPowerChop cuts any tree and drops the logs.", properties = "author=Tyskie; topic=1334458; client=4")
public class TPowerChop extends PollingScript<ClientContext> implements PaintListener {

    private List<Task> taskList = new ArrayList<Task>();
    private int startExp, expGained;
    int[] treeIds;
    private double expEachLog = 1;
    private String interact;

    @Override
    public void start(){
        String userOptions[] = {"Logs", "Oak logs", "Willow logs"};
        String userChoice = "" + JOptionPane.showInputDialog(null, "Select the logs you want to cut.", "TPowerChop", JOptionPane.PLAIN_MESSAGE, null, userOptions, userOptions[0]);

        if (userChoice.equals("Logs")){
            treeIds = MyConstants.TREE_IDS;
            expEachLog = MyConstants.TREE_EXP;
            interact = "Tree";
        } else if (userChoice.equals("Oak logs")){
            treeIds = MyConstants.OAK_IDS;
            expEachLog = MyConstants.OAK_EXP;
            interact = "Oak";
        } else if (userChoice.equals("Willow logs")){
            treeIds = MyConstants.WILLOW_IDS;
            expEachLog = MyConstants.WILLOW_EXP;
            interact = "Willow";
        } else {
            ctx.controller.stop();
        }

        taskList.add(new Drop(ctx));
        taskList.add(new Chop(ctx, treeIds, interact));

        startExp = ctx.skills.experience(Constants.SKILLS_WOODCUTTING);
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
        System.out.println("Thanks for using Tyskie's TPowerChop!");
        System.out.println("Runned For: " + getRunningTime());
        expGained = ctx.skills.experience(Constants.SKILLS_WOODCUTTING) - startExp;
        System.out.println("Woodcutting Exp Gained: " + expGained);
        System.out.println("Logs Chopped: " + (int) Math.round(expGained / expEachLog));
    }

    @Override
    public void repaint(Graphics graphics) {
        expGained = ctx.skills.experience(Constants.SKILLS_WOODCUTTING) - startExp;

        Graphics2D g = (Graphics2D) graphics;

        g.setColor(new Color(128, 128, 128, 180));
        g.fillRect(5, 246, 200, 90);

        g.setColor(new Color(255, 255, 255));
        g.drawRect(5, 246, 200, 90);

        g.drawString("Tyskie's TPowerChop", 10, 266);
        g.drawString("Running For: " + getRunningTime(), 10, 286);
        g.drawString("Woodcutting Exp Gained: " + expGained, 10, 306);
        g.drawString("Logs Chopped: " + (int) Math.round(expGained / expEachLog), 10, 326);
    }

    private String getRunningTime(){
        long milliseconds = this.getTotalRuntime();
        long seconds = (milliseconds / 1000) % 60;
        long minutes = (milliseconds / (1000 * 60) % 60);
        long hours = (milliseconds / (1000 * 60 * 60)) % 24;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
