import com.sun.jna.NativeLibrary;
import org.json.JSONException;
import player.*;
import player.widgets.*;
import player.widgets.controllers.ControllerGenerator;
import player.widgets.controllers.CreepingLineController;

import javax.swing.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * @author: pashka
 */
public class StandingsTest {

    public static final String FILENAME = "video-short.mp4";
    public static final String VLC_PATH = "/Applications/VLC.app/Contents/MacOS/lib";

    public static void main(String[] args) throws InterruptedException, InvocationTargetException, IOException, JSONException {
        new StandingsTest().run();
    }

    private void run() throws InterruptedException, InvocationTargetException, IOException, JSONException {
        NativeLibrary.addSearchPath("vlc", VLC_PATH);

        CreepingLineWidget creepingLineWidget = new CreepingLineWidget("localhost", 1);
        creepingLineWidget.addMessage("It works!");
        StandingsWidget standingsWidget = new StandingsWidget();

//        InternalVideoWidget internalVideoWidget = new InternalVideoWidget(500, 250, 200, 112, "team1.mp4");
//        InternalVideoWidget internalVideoWidget2 = new InternalVideoWidget(20, 250, 200, 112, "team2.mp4");

        new Player(FILENAME, new String[]{FILENAME}, new String[0], new Widget[]{
                new ClockWidget(),
                creepingLineWidget,
                standingsWidget,
//                 internalVideoWidget,
//                internalVideoWidget2
        });
//        JFrame controller = ControllerGenerator.createControllerFrame("Main frame controller", creepingLineWidget.getController(),
//                standingsWidget.getController());
//        controller.setVisible(true);


        Thread.sleep(5000);
        creepingLineWidget.addMessage("Tsinghua Univ is the first team solved problem D. Congratulations!");
        Thread.sleep(10000);
        creepingLineWidget.addMessage("OMG, It really works!");
    }
}
