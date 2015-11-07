package player.generator;

import events.EventsLoader;
import org.json.JSONException;
import player.widgets.*;
import player.widgets.controllers.ControllerGenerator;

import javax.swing.*;
import java.io.IOException;

/**
 * Created by aksenov on 14.04.2015.
 */
public class MainScreenGenerator extends ScreenGenerator {
    private static final int width = 1920;
    private static final int height = 1080;

    public MainScreenGenerator() throws IOException, JSONException {
        super(width, height);

        String mainVideoUrl = properties.getProperty("main.video");

        int sleepTime = Integer.parseInt(properties.getProperty("sleep.time"));

        String videoUrl = properties.getProperty("videos");

        widgets = new Widget[4];
        widgets[0] = new GreenScreenWidget();//OverlayVideoWidget(0, 0, width, height, new String[]{mainVideoUrl}, videoUrl, sleepTime, "main screen");
        widgets[0].setVisible(true);
        widgets[1] = new ClockWidget();
        widgets[2] = new CreepingLineWidget(properties.getProperty("creeping.line.ip"), Integer.parseInt(properties.getProperty("creeping.line.port")));

//        new StandingsLoader().updater(properties.getProperty("scoreboard"));
        try {
            new EventsLoader(properties.getProperty("events")).start();
        } catch (Throwable e) {
            e.printStackTrace();
        }

        widgets[3] = new DoublePersonWidget("Double person widget", properties);
        String[] medias = new String[]{"team1.mp4", "team2.mp4", "video-short.mp4"};
//        widgets[5] = new DoubleVideoWidget(20, 230, 450, 230, 200, 100, medias, videoUrl, sleepTime, "Double video widget");
//        widgets[5] = new BreakingNewsRecordWidget("Breaking news widget", videoUrl);

        JFrame controller = ControllerGenerator.createControllerFrame("Main screen controller",
                widgets[1].getController(),
                widgets[2].getController(),
                widgets[3].getController()
//                widgets[4].getController()
//                widgets[5].getController()
        );
        controller.setVisible(true);
    }

}
