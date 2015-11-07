package player.widgets;

import events.ContestData;
import events.EventsLoader;
import events.Run;
import player.TickPlayer;
import player.widgets.controllers.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by user on 19/05/2015.
 */
public abstract class BreakingNewsWidget extends Widget {
    protected static final int SLEEP = 1000;
    protected final OverlayVideoWidget widget;
    protected final int X = (int) (950 * TickPlayer.scale);
    protected final int Y = (int) (500 * TickPlayer.scale);
    protected final int WIDTH = (int) (246 * TickPlayer.scale);
    protected final int HEIGHT = (int) (144 * TickPlayer.scale);
    private final int PLATE_WIDTH = (int) (326 * TickPlayer.scale);
    private final int GAP = (int) (5 * TickPlayer.scale);
    protected String login;
    protected String password;
    protected long showTime = 30000;
    protected String url;
    Controller controller;
    protected long show = -1;
    protected int teamId;
    protected String info;

    public BreakingNewsWidget(String name, String url, Properties properties, Widget... blockingWidgets) {
        login = properties.getProperty("login");
        showTime = Long.parseLong(properties.getProperty("breaking.news.length"));
        password = properties.getProperty("password");
        controller = new Controller(name);
        this.url = url;
        widget = new OverlayVideoWidget(X, Y, WIDTH, HEIGHT, new String[0], "", SLEEP, "Breaking news video");
        controller.setLayout(new BoxLayout(controller, BoxLayout.X_AXIS));

        JLabel label = new JLabel("Set event");

        final JTextField event = new JTextField(50);

        event.setMinimumSize(event.getPreferredSize());
        event.setMaximumSize(event.getPreferredSize());

        controller.add(label);
        controller.add(event);

        JButton show = new JButton("show");
        show.setAlignmentX(Component.CENTER_ALIGNMENT);

        show.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        for (Widget widget : blockingWidgets) {
                            widget.setVisible(false);
                        }
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException ex) {
                                }
                                setUp(event.getText());
                            }
                        }).start();
                    }
                });

        controller.add(show);
        JButton hide = new JButton("hide");
        hide.setAlignmentX(Component.CENTER_ALIGNMENT);

        hide.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        setVisible(false);
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                try {
//                                    Thread.sleep(1000);
//                                } catch (InterruptedException e1) {
//                                }
//                                widget.stop();
//                            }
//                        }).start();
                    }
                });

        controller.add(hide);
    }

    public void setUp(String command) {
        String[] ss = command.split(" ");
        if (ss.length < 2) {
            return;
        }

        int problemId = -1;

        try {
            teamId = Integer.parseInt(ss[0]);
            problemId = ss[1].toUpperCase().charAt(0) - 'A';
        } catch (Throwable e) {
            teamId = -1;
            problemId = -1;
        }

        /*Here comes some magic*/
        ContestData contestData = EventsLoader.getContestData();
        if (teamId == -1) {
            for (int i = 0; i < command.length(); i++) {
                for (int j = i + 1; j < command.length(); j++) {
                    teamId = Math.max(teamId, contestData.getIdByName(command.substring(i, j)));
                }
            }

            String[] parts = command.split(" ");
            for (int i = 0; i < parts.length; i++) {
                if (parts[i].length() == 1 && Character.isUpperCase(parts[i].charAt(0)) && (parts[i].charAt(0) - 'A') < contestData.getProblemNumber()) {
                    problemId = parts[i].toUpperCase().charAt(0) - 'A';
                }
            }
        }

        //TEST PURPOSE

//        while (true) {
//            try {
//                HttpURLConnection huc = (HttpURLConnection) (new URL(url + "/video/reaction/1000")).openConnection();
//                huc.setRequestMethod("GET");
//                huc.setRequestProperty("Authorization", "Basic " + Base64.getEncoder().encodeToString((login + ":" + password).getBytes()));
//                huc.connect();
//                System.err.println("Response code " + huc.getResponseCode() + " " + huc.getContentType() + " " + huc.getContentLength());
//                if (huc.getResponseCode() == 200) {
//                    break;
//                }
//                Thread.sleep(10000);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
        //TEST PURPOSE

        if (teamId != -1) {
            showVideo(problemId, contestData);
        }
    }

    protected abstract void showVideo(int problemId, ContestData contestData);

    @Override
    public void paint(Graphics2D g, int width, int height) {
//        if (isVisible()) {
//            drawTeamPane(g, EventsLoader.getContestData().getTeamInfo(teamId),
//                    X + (WIDTH - PLATE_WIDTH)/2, Y + HEIGHT + GAP, PLATE_WIDTH, 1);
//        }
        if (System.currentTimeMillis() - show > showTime) {
            setVisible(false);
        }
        changeOpacity();
        if (opacityState > 0) {
//            if (widget.readyToShow())
            widget.setOpacityState(opacityState);
            widget.paint(g, width, height);
            int y = Y + HEIGHT + GAP;
            int x = X + (WIDTH - PLATE_WIDTH) / 2;
            drawTeamPane(g, EventsLoader.getContestData().getTeamInfo(teamId),
                    x, y, PLATE_WIDTH, opacityState);
            drawTextInRect(g, info, (int) (x - 0.005 * PLATE_WIDTH), y, -1, PLATE_WIDTH / 10, POSITION_RIGHT, ACCENT_COLOR, Color.WHITE, opacityState);
        }
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        widget.setVisible(visible);
    }

    @Override
    public Controller getController() {
        return controller;
    }
}
