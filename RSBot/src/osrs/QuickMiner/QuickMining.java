package osrs.QuickMiner;

import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;
import osrs.QuickMiner.tasks.Bank;
import osrs.QuickMiner.tasks.Drop;
import osrs.QuickMiner.tasks.Mine;
import osrs.QuickMiner.tasks.Walk;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Script.Manifest(name="QuickMining", description = "Tutorial", properties = "author=Thijs; topic=999; client=4;")
public class QuickMining extends PollingScript<ClientContext> implements PaintListener {

    List<Task> taskList = new ArrayList<Task>();
    int startExp = 0;

    @Override
    public void start(){
        String userOptions[] = {"Bank", "Powermine"};
        String userChoice = "" + (String) JOptionPane.showInputDialog(null, "Bank or Powermine?", "QuickMining", JOptionPane.PLAIN_MESSAGE, null, userOptions, userOptions[1]);

        String oreOptions[] = {"Copper", "Clay"};
        String oreChoice = "" + (String) JOptionPane.showInputDialog(null, "What do you want to mine?", "QuickMining", JOptionPane.PLAIN_MESSAGE, null, oreOptions, oreOptions[0]);

        if(userChoice.equals("Bank")){
            taskList.add(new Bank(ctx));

            String locationOptions[] = {"Lumbridge Swamp", "Falador Dwarven"};
            String locationChoice = "" + (String) JOptionPane.showInputDialog(null, "Where do you want to mine?", "QuickMining", JOptionPane.PLAIN_MESSAGE, null, locationOptions, locationOptions[0]);

            if(locationChoice.equals("Lumbridge Swamp")){
                taskList.add(new Walk(ctx, MConstants.LUMBRIDGE_SWAMP));
            } else if (userChoice.equals("Falador Dwarven")){
                List<Tile> finalPath = new ArrayList<Tile>();

                if (oreChoice.equals("Copper")){
                    finalPath.addAll(Arrays.asList(MConstants.DWARVEN_COPPER));
                } else if (oreChoice.equals("Clay")){
                    finalPath.addAll(Arrays.asList(MConstants.DWARVEN_CLAY));
                } else {
                    ctx.controller.stop();
                }

                finalPath.addAll(Arrays.asList(MConstants.DWARVEN_STAIRCASE));
                taskList.add(new Walk(ctx, finalPath.toArray(new Tile[] {})));
            } else {
                ctx.controller.stop();
            }

        } else if (userChoice.equals("Powermine")){
            taskList.add(new Drop(ctx));
        } else {
            ctx.controller.stop();
        }

        if (oreChoice.equals("Copper")){
            taskList.add(new Mine(ctx, MConstants.COPPER_ROCK_IDS));
        } else if (oreChoice.equals("Clay")){
            taskList.add(new Mine(ctx, MConstants.CLAY_ROCK_IDS));
        } else {
            ctx.controller.stop();
        }

        startExp = ctx.skills.experience(Constants.SKILLS_MINING);
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
    public void repaint(Graphics graphics) {
        long milliseconds = this.getTotalRuntime();
        long seconds = (milliseconds / 1000) % 60;
        long minutes = (milliseconds / (1000 * 60) % 60);
        long hours = (milliseconds / (1000 * 60 * 60)) % 24;

        int expGained = ctx.skills.experience(Constants.SKILLS_MINING) - startExp;

        Graphics2D g = (Graphics2D) graphics;

        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, 150, 100);

        g.setColor(new Color(255, 255, 255));
        g.drawRect(0, 0, 150, 100);

        g.drawString("QuickMiner", 20, 20);
        g.drawString("Running for: " + String.format("%02d:%02d:%02d", hours, minutes, seconds), 20, 40);
        g.drawString("Exp/Hour: " + (int) (expGained * (3600000 / milliseconds)), 20, 60);
    }
}
