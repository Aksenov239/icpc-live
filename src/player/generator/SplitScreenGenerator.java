package player.generator;

import events.EventsLoader;
import org.json.JSONException;
import player.widgets.ClockWidget;
import player.widgets.CreepingLineWidget;
import player.widgets.TeamVideoWidget;
import player.widgets.Widget;
import player.widgets.controllers.ControllerGenerator;
import player.widgets.controllers.SplitVideoController;
import player.widgets.controllers.VideoController;

import javax.swing.*;
import java.io.IOException;

/**
 * Created by aksenov on 14.04.2015.
 */
public class SplitScreenGenerator extends ScreenGenerator {
    private static final int width = 1280 / 2;//480;
    private static final int height = (720 - 32) / 2;//360;//270;

    public SplitScreenGenerator() throws IOException, JSONException {
        super(2 * width, 2 * height + 32);

        String[] medias = new String[]{
                "video/team1.mp4",
                "video/team2.mp4",
                "video/team3.mp4",
                "video/team4.mp4",
                "video/team5.mp4",
                "video/team6.mp4",
                "video/team7.mp4",
                "http://192.168.1.88:5555/video/camera/1",
                "http://192.168.1.44:8080"};

//        new StandingsLoader().loadOnce(properties.getProperty("scoreboard"));

        new EventsLoader(properties.getProperty("events")).start();
        System.err.println("Here!");

        widgets = new Widget[6];
        for (int i = 0; i < 4; i++) {
            widgets[i] = new TeamVideoWidget((i & 1) * width, (i >> 1) * height, width, height, medias, properties.getProperty("videos"), Integer.parseInt(properties.getProperty("sleep.time")), (i + 1) + " video");
        }
        widgets[4] = new ClockWidget();
        widgets[5] = new CreepingLineWidget(properties.getProperty("creeping.line.ip"), Integer.parseInt(properties.getProperty("creeping.line.port")));
        VideoController[] videoControllers = new VideoController[4];
        for (int i = 0; i < 4; i++) {
            videoControllers[i] = (VideoController) widgets[i].getController();
//            videoControllers[i].setVideoToPlay(i);
            videoControllers[i].change(properties.getProperty("videos") + "/video/screen/" + 83, 83, -1);
            widgets[i].setVisible(true);
        }

        SplitVideoController videoController = new SplitVideoController("Split screen controller", properties, videoControllers);
        JFrame controller = ControllerGenerator.createControllerFrame("Split screen controller", videoController, widgets[5].getController());
        controller.setVisible(true);
    }
}
