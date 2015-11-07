import com.sun.jna.NativeLibrary;
import org.json.JSONException;
import player.TickPlayer;
import player.generator.TeamScreenGenerator;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * @author: pashka
 */
public class TeamScreen {

    public static void main(String[] args) throws InterruptedException, InvocationTargetException, IOException, JSONException {
        new TeamScreen().run();
    }

    private void run() throws InterruptedException, InvocationTargetException, IOException, JSONException {
        String dir = new File(".").getCanonicalPath();
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            if (System.getProperty("sun.arch.data.model").equals("32")) {
                NativeLibrary.addSearchPath("libvlc", dir + "libvlc/x86");
            } else {
                NativeLibrary.addSearchPath("libvlc", dir + "/libvlc/x64");
            }
        } else {
            NativeLibrary.addSearchPath("vlc", "/Applications/VLC.app/Contents/MacOS/lib");
        }

        TickPlayer.scale = 1. * 1080 / 720;
        new TickPlayer("Team screen", new TeamScreenGenerator()).frame.setLocation(1600, 0);
    }
}
