import com.sun.jna.NativeLibrary;
import org.json.JSONException;
import player.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import player.generator.*;

/**
 * User: Aksenov Vitaly
 * Date: 07.04.2015
 * Time: 18:49
 */
public class VideoSplit {
    public static void main(String[] args) throws InterruptedException, InvocationTargetException, IOException, JSONException {
//        if (args.length == 0) {
//            System.err.println("java VideoSplit <site/ip>");
//            System.exit(1);
//        }
        new VideoSplit().run();
    }

    //    private int width = 720;
//    private int height = 405;
    private int width = 480;
    private int height = 270;

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

//        String[] medias = new String[]{"team1.mp4", "team2.mp4", "video-short.mp4", "http://" + ip + ":5555/video/camera/1/"};
//        String[] medias = new String[]{"team1.mp4", "team2.mp4", "video-short.mp4", "http://" + ip + ":5555/video/camera/1", "http://192.168.1.44:8080"};
//
//        Standings.loadFromWeb("http://" + ip + ":8000/scoreboard");
//
//        MediaPlayerFactory factory = new MediaPlayerFactory(new String[0]);
//        VideoPanel[] panels = new VideoPanel[4];
//        for (int i = 0; i < panels.length; i++) {
//            //    panels[i] = new EmbeddedVideoPanel("panel" + i, medias, width, height, factory);
//            panels[i] = new SmallVideoPanel("panel" + i, medias, width, height);
//        }
//
//        JFrame frame = new JFrame("Split screen");
//        frame.getContentPane().setLayout(new GridLayout(2, 2));
//        for (int i = 0; i < panels.length; i++) {
//            frame.getContentPane().add(panels[i]);
//        }
//        frame.setSize(2 * width, 2 * height);
////        frame.pack();
//        frame.setVisible(true);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        for (int i = 0; i < panels.length; i++) {
//            panels[i].getPlayer().play();
//        }
//
//        JFrame controller = new JFrame("Screens controller");
//        controller.getContentPane().setLayout(new GridLayout(2, 2));
//        for (int i = 0; i < 4; i++) {
//            controller.getContentPane().add(panels[i].getController());
//        }
//
//        controller.pack();
//        controller.setVisible(true);

        TickPlayer.scale = 1;
        new TickPlayer("Split screen", new SplitScreenGenerator ());
    }
}
