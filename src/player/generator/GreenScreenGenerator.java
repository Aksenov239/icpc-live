package player.generator;

import net.NetworkPreparation;
import player.widgets.*;
import player.widgets.controllers.ControllerGenerator;

import javax.swing.*;
import java.io.File;
import java.net.MalformedURLException;

/**
 * @author: pashka
 */
public class GreenScreenGenerator extends ScreenGenerator {

    private static final int width = 1280;
    private static final int height = 720;

    public GreenScreenGenerator() {
        super(width, height);

        widgets = new Widget[5];
        widgets[0] = new GreenScreenWidget();
        widgets[0].setVisible(true);
        widgets[1] = new ClockWidget();
        widgets[2] = new CreepingLineWidget(properties.getProperty("creeping.line.ip"), Integer.parseInt(properties.getProperty("creeping.line.port")));

        NetworkPreparation.prepare("live", "l1ve");

        widgets[3] = new StandingsWidget();
        String[] medias = new String[]{"team1.mp4", "team2.mp4", "video-short.mp4"};
        String url = properties.getProperty("videos");
        widgets[4] = new DoubleVideoWidget(20, 230, 450, 230, 200, 100, medias, url, Integer.parseInt(properties.getProperty("sleep.time")), "Double video widget");

        JFrame controller = ControllerGenerator.createControllerFrame("Main screen controller", widgets[2].getController(), widgets[3].getController(), widgets[4].getController());
        controller.setVisible(true);
    }
}
