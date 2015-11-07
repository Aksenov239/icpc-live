package player;

import player.widgets.PlayerWidget;
import player.widgets.TeamInfoWidget;
import player.widgets.controllers.VideoController;
import uk.co.caprica.vlcj.player.MediaPlayer;

import javax.swing.*;
import java.awt.*;

/**
 * User: Aksenov Vitaly
 * Date: 07.04.2015
 * Time: 17:24
 */
public class SmallVideoPanel extends VideoPanel implements PlayerWidget {
    protected PlayerInImage player;
    protected String[] medias;
    protected int width;
    protected int height;
    protected String name;
    protected TeamInfoWidget teamInfoWidget;
    boolean block;

    public SmallVideoPanel(String name, String[] medias, int width, int height) {
        this.width = width;
        this.height = height;
        this.name = name;
        this.medias = medias;
        setSize(width, height);
        player = new PlayerInImage(width, height, this, medias[0]);
        controller = new VideoController(this, medias, name + " controller");
        teamInfoWidget = new TeamInfoWidget("", 0, 0, null, 0, "");
    }

    public void change(int id) {
        change(medias[id]);
    }

    public void change(final String url) {
        new Thread() {
            public void run() {
                block = true;
                PlayerInImage player2 = new PlayerInImage(width, height, null, url);
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                JComponent component = player.getComponent();
                player.setComponent(null);
                block = false;
                player2.setComponent(component);
                player.stop();
                player = player2;
            }
        }.start();
    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(player.getImage(), 0, 0, null);
        if (!block)
            teamInfoWidget.setTeam(controller.getTeamId(), "");
        teamInfoWidget.paint(g2, width, height);
    }

    public MediaPlayer getPlayer() {
        return player.getPlayer();
    }
}
