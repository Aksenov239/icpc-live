package player.widgets.controllers;

import events.ContestData;
import events.EventsLoader;
import events.Run;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;


/**
 * Created by aksenov on 13.04.2015.
 */
public class SplitVideoController extends Controller {
    private VideoController[] controllers;
    private int pos;
    private int time;
    private int nextPosition;
    private int[] toShow;
    private int positionInShow;

    private boolean onScreen(int[] showingId, int place, int id) {
        boolean bad = false;

        for (int i = 0; i < showingId.length; i++)
            bad |= showingId[i] == id;

        return bad;
    }


    public long secondsFromStart(double timestamp) {
        return (long) (timestamp - EventsLoader.getContestData().startTime / 1000);
    }

    public SplitVideoController(String name, final Properties properties, VideoController... c) {
        super(name);
        controllers = c;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2));

        this.time = Integer.parseInt(properties.getProperty("split.changetime"));

        String[] show = properties.getProperty("split.setup").split(",");
        toShow = new int[show.length];
        for (int i = 0; i < toShow.length; i++) {
            toShow[i] = Integer.parseInt(show[i]);
        }

        final Checkbox checkbox = new Checkbox("auto");
        checkbox.setSize(new Dimension(50, 50));
        checkbox.setMinimumSize(checkbox.getPreferredSize());
        checkbox.setMaximumSize(checkbox.getPreferredSize());

        add(checkbox);
        for (Controller controller : controllers)
            panel.add(controller);
        add(panel);

        final int[] showingId = new int[4];
        final int[] showingPosition = new int[4];
        final int leaders = Integer.parseInt(properties.getProperty("split.leaders"));
        final String videoUrl = properties.getProperty("videos");
        final int replayWait = Integer.parseInt(properties.getProperty("replay.wait"));

        Arrays.fill(showingPosition, -1);

        new Timer().scheduleAtFixedRate(new TimerTask() {
            public void run() {
                ArrayBlockingQueue<Run> runs = EventsLoader.getAllRuns();
                long currentTime = secondsFromStart(System.currentTimeMillis() / 1000);
                while (!runs.isEmpty() && (!runs.peek().result.equals("AC") || secondsFromStart(runs.peek().timestamp) < currentTime - 600)) {
                    runs.poll();
                }
                ContestData data = EventsLoader.getContestData();

                if (!checkbox.getState()) {
                    for (int i = 0; i < controllers.length; i++) {
                        if (!controllers[pos].isAuto()) {
                            pos = (pos + 1) % controllers.length;
                        }
                        if (!controllers[i].isAuto()) {
                            showingId[i] = controllers[i].getTeamId();
                            showingPosition[i] = data.getPosition(showingId[i]);
                        }
                    }

                    if (!controllers[pos].isAuto()) {
                        return;
                    }
                }

                String url;
                int teamId, problemId;
                if (!runs.isEmpty() && secondsFromStart(runs.peek().timestamp) + 1. * replayWait / 1000 < currentTime) {
                    Run toShow = runs.poll();
                    url = videoUrl + "/video/reaction/" + toShow.id;
                    teamId = toShow.team;
                    showingId[pos] = toShow.team;
                    showingPosition[pos] = data.getPosition(showingId[pos]);
                    problemId = toShow.problem;
                } else {
                    int position = nextPosition;
                    int id = 0;
                    while (true) {
                        id = data.getId(position);

                        if (!onScreen(showingId, position, id)) {
                            break;
                        }
                        position = (position + 1) % leaders;
                    }

                    if (data.getTeamInfo(id).solved == 0) {
                        while (true) {
                            id = toShow[positionInShow];
                            position = data.getPosition(id);

                            if (!onScreen(showingId, position, id)) {
                                break;
                            }
                            positionInShow = (positionInShow + 1) % toShow.length;
                        }
                    }

                    problemId = -1;
                    teamId = id;
                    showingPosition[pos] = position;
                    showingId[pos] = id;
                    url = videoUrl + "/video/camera/" + teamId;
                    nextPosition = (nextPosition + 1) % leaders;
                }

                controllers[pos].change(url, teamId, problemId);

                pos = (pos + 1) % 4;
            }
        }, 0L, time);
    }

}
