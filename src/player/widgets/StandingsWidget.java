package player.widgets;

import events.ContestData;
import events.EventsLoader;
import events.TeamInfo;
import player.TickPlayer;
import player.widgets.controllers.Controller;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author: pashka
 */
public class StandingsWidget extends Widget {

    private  final int MOVING_TIME = (int) (500 * TickPlayer.scale);
    private final int PLATE_WIDTH = (int) (326 * TickPlayer.scale);
    private int STANDING_TIME = (int) (5000 * TickPlayer.scale);
    private int TOP_PAGE_STANDING_TIME = (int) (10000 * TickPlayer.scale);
    public int PERIOD = STANDING_TIME + MOVING_TIME;
    public int LENGTH;
    private final int X1 = (int) (31 * TickPlayer.scale);
    private final int X2 = (int) (55 * TickPlayer.scale);
    private final int X3 = (int) (275 * TickPlayer.scale);
    private final int X4 = (int) (314 * TickPlayer.scale);
    private final double DX = 349 * TickPlayer.scale;
    private final int Y1 = (int) (65 * TickPlayer.scale);
    private final double DY = 35 * TickPlayer.scale;
    public final int TEAMS_ON_PAGE = 12;
    public final Font FONT = Font.decode("Open Sans Italic " + (int)(22 * TickPlayer.scale));

    private final BufferedImage image;
    private Controller controller;
    //double opacity;
    //long last;
    int timer;
    int start;

    private ContestData contestData;

    public StandingsWidget() {
        BufferedImage image;
        try {
            image = ImageIO.read(new File("pics/standings.png"));
        } catch (IOException e) {
            image = null;
        }
        this.image = image;
        last = System.currentTimeMillis();

        controller = new Controller("Standings controlle");
        JButton top1 = new JButton("Show 1 page");
        top1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LENGTH = 12;
                start = 0;
                timer = -Integer.MAX_VALUE;
                setVisible(true);
            }
        });
        controller.add(top1);

        JButton top2 = new JButton("Show 2 pages");
        top2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TOP_PAGE_STANDING_TIME = 10000;
                STANDING_TIME = 10000;
                PERIOD = STANDING_TIME + MOVING_TIME;
                LENGTH = 24;
                start = 0;
                timer = 0;
                setVisible(true);
            }
        });
        controller.add(top2);

        JButton all = new JButton("Show all pages");
        all.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TOP_PAGE_STANDING_TIME = 10000;
                STANDING_TIME = 5000;
                PERIOD = STANDING_TIME + MOVING_TIME;
                LENGTH = contestData.getTeamNumber();
                start = 0;
                timer = -TOP_PAGE_STANDING_TIME + STANDING_TIME;
                setVisible(true);
            }
        });
        controller.add(all);

        JButton hide = new JButton("hide");
        hide.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        controller.add(hide);
        controller.setSize(200, 50);
        controller.setVisible(true);
        controller.setLocation(0, 0);
//        setVisible(true);
        controller.setMinimumSize(controller.getPreferredSize());
        controller.setMaximumSize(controller.getPreferredSize());
    }

    @Override
    public void paint(Graphics2D g, int width, int height) {

//        standings = StandingsLoader.getLoaded();
        contestData = EventsLoader.getContestData();
        if (contestData == null || contestData.getStandings() == null) return;
        if (LENGTH == 0)
            LENGTH = contestData.getTeamNumber();
        int dt = changeOpacity();

        if (opacityState > 0) {
            if (isVisible()) {
                timer = timer + dt;
                if (timer >= PERIOD) {
                    timer -= PERIOD;
                    start += TEAMS_ON_PAGE;
                }
            }
            int dx = 0;
            if (timer >= STANDING_TIME) {
                if (start + TEAMS_ON_PAGE >= LENGTH) {
                    setVisible(false);
                } else {
                    double t = (timer - STANDING_TIME) * 1.0 / MOVING_TIME;
                    dx = (int) ((2 * t * t * t - 3 * t * t) * width);
                }
            }
            int x = (int) ((width - (DX + DX + PLATE_WIDTH)) / 2);
            int y = (int) (height - 32 * TickPlayer.scale - 4.5 * DY);
//            g.setComposite(AlphaComposite.SrcOver.derive((float) opacity));
            if (start < LENGTH) {
                drawStandings(g, x + dx, y, contestData, start);
            }
            if (start + TEAMS_ON_PAGE < LENGTH) {
                drawStandings(g, x + dx + width, y, contestData, start + TEAMS_ON_PAGE);
            }
        } else {
            timer = -TOP_PAGE_STANDING_TIME;
            start = 0;
        }
    }

    private void drawStandings(Graphics2D g, int x, int y, ContestData contestData, int start) {
        for (int i = 0; i < TEAMS_ON_PAGE; i++) {
            if (start + i >= LENGTH)
                break;
            TeamInfo team = contestData.getStandings()[start + i];
            int dx = (int) (DX * (i / 4));
            int dy = (int) (DY * (i % 4));
            g.setFont(FONT);
            if (team != null)
            drawTeamPane(g, team, x + dx, y + dy, PLATE_WIDTH, opacityState);
        }
    }

    public Controller getController() {
        return controller;
    }
}
