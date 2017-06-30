package scripts;

import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;
import scripts.resources.MyConstants;
import scripts.resources.Task;
import scripts.tasks.Bank;
import scripts.tasks.Cook;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Thijs on 16-6-2017.
 */
@Script.Manifest(name="Tyskie's GECooker", description = "Tyskie's GECooker cooks food on random fires made by other players.", properties = "author=Tyskie; topic=999; client=4;")
public class GECooker extends PollingScript<ClientContext> implements PaintListener {

    private List<Task> taskList = new ArrayList<Task>();
    private int startExp = 0;
    private int expEachCook = 1;
    private int foodId;

    @Override
    public void start(){
        String userOptions[] = MyConstants.FOOD_IDS;
        String userChoice = "" + JOptionPane.showInputDialog(null, "What food do you want to cook?", "GECooker", JOptionPane.PLAIN_MESSAGE, null, userOptions, userOptions[0]);

        if (userChoice.equals("Shrimps")){
            foodId = MyConstants.SHRIMPS;
            expEachCook = MyConstants.EXP_SHRIMPS;
        } else if (userChoice.equals("Anchovies")){
            foodId = MyConstants.ANCHOVIES;
            expEachCook = MyConstants.EXP_ANCHOVIES;
        } else if (userChoice.equals("Sardine")){
            foodId = MyConstants.SARDINE;
            expEachCook = MyConstants.EXP_SARDINE;
        } else if (userChoice.equals("Salmon")){
            foodId = MyConstants.SALMON;
            expEachCook = MyConstants.EXP_SALMON;
        } else if (userChoice.equals("Trout")){
            foodId = MyConstants.TROUT;
            expEachCook = MyConstants.EXP_TROUT;
        } else if (userChoice.equals("Cod")){
            foodId = MyConstants.COD;
            expEachCook = MyConstants.EXP_COD;
        } else if (userChoice.equals("Herring")){
            foodId = MyConstants.HERRING;
            expEachCook = MyConstants.EXP_HERRING;
        } else if (userChoice.equals("Pike")){
            foodId = MyConstants.PIKE;
            expEachCook = MyConstants.EXP_PIKE;
        } else if (userChoice.equals("Mackerel")){
            foodId = MyConstants.MACKEREL;
            expEachCook = MyConstants.EXP_MACKEREL;
        } else if (userChoice.equals("Tuna")){
            foodId = MyConstants.TUNA;
            expEachCook = MyConstants.EXP_TUNA;
        } else if (userChoice.equals("Bass")){
            foodId = MyConstants.BASS;
            expEachCook = MyConstants.EXP_BASS;
        } else if (userChoice.equals("Swordfish")){
            foodId = MyConstants.SWORDFISH;
            expEachCook = MyConstants.EXP_SWORDFISH;
        } else if (userChoice.equals("Lobster")){
            foodId = MyConstants.LOBSTER;
            expEachCook = MyConstants.EXP_LOBSTER;
        } else if (userChoice.equals("Shark")){
            foodId = MyConstants.SHARK;
            expEachCook = MyConstants.EXP_SHARK;
        } else if (userChoice.equals("Manta Ray")){
            foodId = MyConstants.MANTA_RAY;
            expEachCook = MyConstants.EXP_MANTA_RAY;
        } else if (userChoice.equals("Sea Turtle")){
            foodId = MyConstants.SEA_TURTLE;
            expEachCook = MyConstants.EXP_SEA_TURTLE;
        } else if (userChoice.equals("Monkfish")){
            foodId = MyConstants.MONKFISH;
            expEachCook = MyConstants.EXP_MONKFISH;
        } else {
            ctx.controller.stop();
        }

        taskList.add(new Bank(ctx, foodId));
        taskList.add(new Cook(ctx, foodId));

        startExp = ctx.skills.experience(Constants.SKILLS_COOKING);
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
        System.out.println("Thanks for using Tyskie's GECooker!");
    }

    @Override
    public void repaint(Graphics graphics) {
        long milliseconds = this.getTotalRuntime();
        long seconds = (milliseconds / 1000) % 60;
        long minutes = (milliseconds / (1000 * 60) % 60);
        long hours = (milliseconds / (1000 * 60 * 60)) % 24;

        int expGained = ctx.skills.experience(Constants.SKILLS_COOKING) - startExp;

        Graphics2D g = (Graphics2D) graphics;

        g.setColor(new Color(128, 128, 128, 180));
        g.fillRect(5, 246, 175, 90);

        g.setColor(new Color(255, 255, 255));
        g.drawRect(5, 246, 175, 90);

        g.drawString("Tyskie's GECooker", 10, 266);
        g.drawString("Running For: " + String.format("%02d:%02d:%02d", hours, minutes, seconds), 10, 286);
        g.drawString("Cooking Exp Gained: " + expGained, 10, 306);
        g.drawString("Food cooked: " + expGained / expEachCook, 10, 326);
    }
}
