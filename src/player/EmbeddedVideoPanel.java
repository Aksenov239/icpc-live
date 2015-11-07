package player;

import player.widgets.controllers.Controller;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * User: Aksenov Vitaly
 * Date: 07.04.2015
 * Time: 17:24
 */
public class EmbeddedVideoPanel extends VideoPanel {
    final PlayerInstance playerInstance;

    public EmbeddedVideoPanel(String name, String[] medias, int width, int height, MediaPlayerFactory factory) {
        initialization(name, medias, width, height, factory.newEmbeddedMediaPlayer());
        playerInstance = new PlayerInstance((EmbeddedMediaPlayer) player);

        add(playerInstance.videoSurface());

        playerInstance.mediaPlayer().setVideoSurface(factory.newVideoSurface(playerInstance.videoSurface()));
    }            

    public PlayerInstance getPlayerInstance() {
        return playerInstance;
    }
}
