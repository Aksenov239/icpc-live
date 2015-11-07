import com.sun.jna.NativeLibrary;
import org.json.JSONException;
import player.TickPlayer;
import player.generator.BreakingNewsGenerator;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * User: Aksenov Vitaly
 * Date: 07.04.2015
 * Time: 18:49
 */
public class BreakingNews {
    public static void main(String[] args) throws InterruptedException, InvocationTargetException, IOException, JSONException {
        new BreakingNews().run();
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

        TickPlayer.scale = 1. * 1080 / 720;
	BreakingNewsGenerator generator = new BreakingNewsGenerator();                                                     
        new TickPlayer("Breaking news1", generator).frame.setLocation(1600, 0);
        new TickPlayer("Breaking news2", generator, 1600, 850).frame.setLocation(0, 0);
    }
}
