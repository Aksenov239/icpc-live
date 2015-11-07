package player.widgets;

import events.TeamInfo;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author: pashka
 */
public class TeamVideoWidget extends TeamWidget {
    TeamInfo team;
    static BufferedImage teamImage;

    static {
        try {
            teamImage = ImageIO.read(new File("pics/team.png"));
        } catch (IOException e) {
        }
    }

    public TeamVideoWidget(int x, int y, int width, int height, String[] medias, String url, int sleepTime, String name) {
        super(x, y, width, height, medias, url, sleepTime, name);
        FONT1 = Font.decode("Open Sans Italic 20");
        X /= 2;
        Y /= 2;
        GAP_X /= 2;
        GAP_Y /= 2;
        PR_WIDTH /= 2;
        RUN_WIDTH /= 2;
        RUN_SMALL_WIDTH /= 2;
        HEIGHT /= 2;
        STAR_SIZE /= 2;
        FONT2 = Font.decode("Open Sans Italic 15");
    }

    public int getTeamId() {
        return controller.getTeamId();
    }
}
