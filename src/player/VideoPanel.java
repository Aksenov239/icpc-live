package player;

import player.widgets.controllers.Controller;
import uk.co.caprica.vlcj.player.*;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import player.widgets.controllers.SpringUtilities;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import player.widgets.controllers.VideoController;

/**
 * User: Aksenov Vitaly
 * Date: 07.04.2015
 * Time: 17:24
 */
public abstract class VideoPanel extends JPanel {
    protected MediaPlayer player;
    protected VideoController controller;
    protected String[] medias;

    protected void initialization(String name, String[] medias, int width, int height, MediaPlayer player) {
        this.medias = medias;
        this.player = player;

        setLayout(new BorderLayout());
        setSize(width, height);

        controller = new VideoController(this, medias, name + " controller");

        player.setRepeat(true);
        player.setVolume(0);
        player.prepareMedia(medias[0]);
    }

    public MediaPlayer getPlayer() {
        return player;
    }

    public Controller getController() {
        return controller;
    }

    public void change(int id) {
        change(medias[id]);
    }

    public void change(String url) {player.playMedia(url); }
}
