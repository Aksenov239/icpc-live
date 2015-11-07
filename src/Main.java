import com.sun.jna.NativeLibrary;
import org.json.JSONException;
import player.TickPlayer;
import player.generator.MainScreenGenerator;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * @author: pashka
 */
public class Main {

    public static final String FILENAME = "video-short.mp4";

    public static void main(String[] args) throws InterruptedException, InvocationTargetException, IOException, JSONException {
        new Main().run();
    }

    private void run() throws InterruptedException, InvocationTargetException, IOException, JSONException {
        String dir = new File(".").getCanonicalPath();
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            if (System.getProperty("sun.arch.data.model").equals("32")) {
                NativeLibrary.addSearchPath("libvlc", dir + "/libvlc/x86");
            } else {
                NativeLibrary.addSearchPath("libvlc", dir + "/libvlc/x64");
            }
        } else {
            NativeLibrary.addSearchPath("vlc", "/Applications/VLC.app/Contents/MacOS/lib");
        }

//        String media = new File(FILENAME).toString();//.toURI().toString();
//
//        CreepingLineWidget creepingLinePainter = new CreepingLineWidget();
//        creepingLinePainter.addMessage("It works!");u
//
//        StandingsWidget standingsWidget = new StandingsWidget(Standings.loadFromFile("standings.json"));
////        StandingsWidget standingsWidget = new StandingsWidget(Standings.loadFromFile("http://192.168.1.88:8000/standings"));
//
//        String[] medias = new String[]{"team1.mp4", "team2.mp4", FILENAME};
//
//        new Player(media, medias, new String[0], new Widget[]{
//                new ClockWidget(),
//                creepingLinePainter,
//                standingsWidget,
//                new DoubleVideoWidget(20, 230, 450, 230, 200, 100, medias, "Double video widget")
//        });
//
//        Thread.sleep(5000);
//        creepingLinePainter.addMessage("Tsinghua Univ is the first team solved problem D. Congratulations!");
//        Thread.sleep(10000);
//        creepingLinePainter.addMessage("OMG, It really works!");
//        new TickPlayer("Main screen", new GreenScreenGenerator());
        TickPlayer.scale = 1. * 1080 / 720;
        new TickPlayer("Main screen", new MainScreenGenerator()).frame.setLocation(1600, 0);
    }
}
