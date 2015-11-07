package player.widgets;

import player.PlayerInImage;
import player.widgets.controllers.Controller;
import player.widgets.controllers.VideoController;
import uk.co.caprica.vlcj.player.MediaPlayer;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author: pashka
 */
public class OverlayVideoWidget extends VideoWidget {

    public OverlayVideoWidget(int x, int y, int width, int height, String[] medias, String url,  int sleepTime, String name) {
        super(x, y, width, height, medias, url, sleepTime, name);
    }

    @Override
    public void paint(Graphics2D g, int width, int height) {
//        changeOpacity();
        if (opacityState > 0) {
            int hh = (int) (this.height * opacity);
            g.drawImage(image, x, y + (this.height - hh) / 2, this.width, hh, null);
        }
    }
}
