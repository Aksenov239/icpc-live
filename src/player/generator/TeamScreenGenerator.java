package player.generator;

import events.EventsLoader;
import org.json.JSONException;
import player.TickPlayer;
import player.widgets.GreenScreenWidget;
import player.widgets.TeamInfoClientWidget;
import player.widgets.TeamInfoWidget;
import player.widgets.Widget;
import player.widgets.controllers.ControllerGenerator;

import javax.swing.*;
import java.io.IOException;

/**
 * Created by aksenov on 14.04.2015.
 */
public class TeamScreenGenerator extends ScreenGenerator {
    private static final int width = 1920;
    private static final int height = 1080;

    public TeamScreenGenerator() throws IOException, JSONException {
        super(width, height);

        String videosUrl = properties.getProperty("videos");

//        new StandingsLoader().updater(properties.getProperty("scoreboard"));
        new EventsLoader(properties.getProperty("events")).start();
        widgets = new Widget[2];
//        widgets[0] = new OverlayVideoWidget(0, 0, width, height, new String[]{videosUrl + "/video/camera/1"}, "team screen");
        widgets[0] = new TeamInfoWidget(videosUrl,
                width,
                height - (int) (32 * TickPlayer.scale),
                new String[]{"video/team6.mp4"},
                Integer.parseInt(properties.getProperty("sleep.time")),
                "team screen");
//        widgets[0] = new TeamInfoClientWidget(videosUrl,
//                width,
//                height - (int) (32 * TickPlayer.scale),
//                new String[]{"video/team6.mp4"},
//                Integer.parseInt(properties.getProperty("sleep.time")),
//                properties.getProperty("team.screen.server.ip"),
//                Integer.parseInt(properties.getProperty("team.screen.server.port")),
//                "team screen");
        widgets[0].setVisible(true);
        widgets[1] = new GreenScreenWidget();
        widgets[1].setVisible(false);

        JFrame controller = ControllerGenerator.createControllerFrame("Main screen controller", widgets[0].getController());
        controller.setVisible(true);
    }

}
