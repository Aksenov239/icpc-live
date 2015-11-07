package player.generator;

import events.EventsLoader;
import org.json.JSONException;
import player.TickPlayer;
import player.widgets.*;
import player.widgets.controllers.ControllerGenerator;

import javax.swing.*;
import java.io.IOException;

/**
 * Created by aksenov on 14.04.2015.
 */
public class BreakingNewsGenerator extends ScreenGenerator {
    private static final int width = 1920;
    private static final int height = 1080;

    public BreakingNewsGenerator() throws IOException, JSONException {
        super(width, height);

        try {
            new EventsLoader(properties.getProperty("events")).start();
        } catch (Throwable e) {
            e.printStackTrace();
        }

        String videoUrl = properties.getProperty("videos");

        widgets = new Widget[5];
        widgets[0] = new GreenScreenWidget();
        widgets[0].setVisible(true);
        widgets[1] = new StandingsWidget();
        widgets[2] = new BreakingNewsRecordWidget("Breaking news widget", videoUrl, properties, widgets[1]);
        widgets[3] = new BreakingNewsLiveWidget("Breaking news live widget", videoUrl, properties, widgets[1], widgets[2]);
        widgets[4] = new TeamInfoServerWidget(videoUrl,
                width,
                height - (int) (32 * TickPlayer.scale),
                new String[]{"video/team6.mp4"},
                Integer.parseInt(properties.getProperty("sleep.time")),
                Integer.parseInt(properties.getProperty("team.screen.server.port")),
                "team screen"
        );
        JFrame controller = ControllerGenerator.createControllerFrame("Main screen controller",
                widgets[1].getController(),
                widgets[2].getController(),
                widgets[3].getController(),
                widgets[4].getController()
        );
        controller.setVisible(true);
    }

}
