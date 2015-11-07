package player.widgets;

import player.widgets.controllers.Controller;
import player.widgets.controllers.TeamInfoController;

import java.awt.*;

/**
 * @author: pashka
 */
public class TeamInfoWidget extends TeamWidget {
    private String url;
    private Controller controller;
    private int teamId;

    public TeamInfoWidget(String url, int width, int height, String[] medias, int sleepTime, String name) {
        super(0, 0, width, height, medias, url, sleepTime, name);

        this.url = url;
        teamId = 1;
        controller = new TeamInfoController(this);
    }

    public void setTeam(int id, String type) {
        teamId = id;
//        player.change(url + "/video/" + type + "/" + id);
        if (type.equals("info")) {
            change("teaminfo/" + String.format("%03d", id) + ".jpg");
        } else
//            change("video/team" + ((id - 1) % 7 + 1) + ".mp4");
            change(url + "/video/" + type + "/" + id);
    }

    protected int getTeamId() {
        return teamId;
    }

    public Controller getController() {
        return controller;
    }
}
