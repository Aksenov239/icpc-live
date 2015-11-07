package player.widgets;

import player.PlayerInImage;
import player.widgets.controllers.Controller;
import player.widgets.controllers.VideoController;
import uk.co.caprica.vlcj.player.MediaPlayer;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author: pashka
 */
public abstract class VideoWidget extends Widget implements PlayerWidget {
    protected PlayerInImage player;
    protected final VideoController controller;
    protected BufferedImage image;
    private final String[] medias;
    protected AtomicBoolean inChange;
    protected AtomicBoolean ready;
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected int sleepTime;
    protected String URL;

    public VideoWidget(int x, int y, int width, int height, String[] medias, String url, int sleepTime, String name) {
        this.URL = url;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        player = new PlayerInImage(width, height, null, medias.length > 0 ? medias[0] : null);
        image = player.getImage();
        controller = new VideoController(this, medias, url, name + " controller");
        this.medias = medias;
        this.sleepTime = sleepTime;
        inChange = new AtomicBoolean();
        ready = new AtomicBoolean(medias.length > 0);
    }

    public void change(final int id) {
        change(medias[id]);
    }

    public void change(final String url) {
        new Thread() {
            public void run() {
                ready.set(false);
                PlayerInImage player2 = new PlayerInImage(width, height, null, url);
                try {
                    Thread.sleep(sleepTime);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                JComponent component = player.getComponent();
                player.setComponent(null);
                player2.setComponent(component);
                inChange.set(true);
                player.stop();
                player = player2;
                image = player2.getImage();
                ready.set(true);
                URL = url;
            }
        }.start();
    }

    public void stop() {
        if (player != null)
            player.stop();
    }

    public boolean readyToShow() {
        return ready.get();
    }

    @Override
    public Controller getController() {
        return controller;
    }

    public MediaPlayer getPlayer() {
        return player.getPlayer();
    }
}
