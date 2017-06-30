package Tyskie.scripts.TCrafting;

import Tyskie.scripts.TCrafting.resources.Task;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.ClientContext;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by Tyskie on 30-6-2017.
 */
@Script.Manifest(name = "Tyskie's AIO TCrafting", description = "Tyskie's AIO TCrafting crafts all the F2P jewelry and armour.", properties = "author=Tyskie; topic=1334821; client=4")
public class TCrafting extends PollingScript<ClientContext> implements PaintListener {

    private List<Task> taskList = new ArrayList<Task>();

    @Override
    public void start(){

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

    }
}
