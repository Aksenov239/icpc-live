package player.widgets.controllers;

import player.VideoPanel;
import player.widgets.VideoWidget;
import uk.co.caprica.vlcj.player.MediaPlayer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * Created by aksenov on 13.04.2015.
 */
public class VideoController extends Controller {

    private String[] medias;
    private MediaPlayer player;
    private String url;
    private Checkbox auto;

    private interface MediaChanger {
        public void change(String url);
    }

    private int teamId;
    private int problemId = -1;
    private MediaChanger mediaChanger;

    private void mainControllerPart(ActionListener showListener, final MediaChanger mediaChanger, final MediaPlayer player, final String[] medias) {
        this.mediaChanger = mediaChanger;
        this.player = player;
        this.medias = medias;
        setLayout(new SpringLayout());

        JButton show = new JButton("show/hide");
        show.addActionListener(showListener);

        add(show);

        JButton pause = new JButton("pause/play");
        pause.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (player.isPlaying())
                    player.pause();
                else
                    player.play();
            }
        });

        add(pause);

        final JTextField team = new JTextField(12);
        team.setMaximumSize(team.getPreferredSize());
        team.setMinimumSize(team.getPreferredSize());
        final JComboBox combo = new JComboBox(new String[]{"screen", "camera"});
        combo.setMaximumSize(combo.getPreferredSize());
        JButton button = new JButton("Show!");
        auto = new Checkbox("auto");
        auto.setMinimumSize(auto.getPreferredSize());
        auto.setMaximumSize(auto.getPreferredSize());

        add(team);
        add(combo);
        add(button);
        add(auto);

        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int teamId = Integer.parseInt(team.getText());
                team.setText("");
                String type = (String) combo.getSelectedItem();

                change(url + "/video/" + type + "/" + teamId, teamId, -1);
            }
        });

        setBorder(new EmptyBorder(10, 10, 10, 10));

        SpringUtilities.makeCompactGrid(this, 1, 6, 0, 0, 0, 0);
    }

    public VideoController(final VideoWidget widget, final String[] medias, String url, String name) {
        super(name);

        this.url = url;

        mainControllerPart(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                widget.setVisible(!widget.isVisible());
            }
        }, new MediaChanger() {
            public void change(String url) {
                widget.change(url);
            }
        }, widget.getPlayer(), medias);
    }

    public VideoController(final VideoPanel panel, String[] medias, String name) {
        super(name);

        mainControllerPart(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setVisible(!panel.isVisible());
            }
        }, new MediaChanger() {
            public void change(String url) {
                panel.change(url);
            }
        }, panel.getPlayer(), medias);
    }

    public void setVideoToPlay(int play) {
        mediaChanger.change(medias[play]);
        teamId = play + 1;
    }

    public int getTeamId() {
        return teamId;
    }

    public int getProblemId() {
        return problemId;
    }

    public void change(String url, int teamId, int problemId) {
        mediaChanger.change(url);
        this.teamId = teamId;
        this.problemId = problemId;
    }

    @Deprecated
    public void setTeamId(int i) {
        mediaChanger.change(medias[i]);
        this.teamId = i + 1;
    }

    public boolean isAuto() {
        return auto.getState();
    }

}
