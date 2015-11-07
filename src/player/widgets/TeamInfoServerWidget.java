package player.widgets;

import player.widgets.controllers.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;

/**
 * @author: pashka
 */
public class TeamInfoServerWidget extends TeamWidget {
    private String url;
    private Controller controller;
    private int teamId;
    private BufferedReader reader;
    private ServerSocket serverSocket;

    public TeamInfoServerWidget(String url, int width, int height, String[] medias, int sleepTime, int port, String name) {
        super(0, 0, width, height, medias, url, sleepTime, name);

        this.url = url;
        teamId = 1;
        controller = new Controller("");
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("Can't start server");
            e.printStackTrace();
            return;
        }

        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        int id = Integer.parseInt(reader.readLine());
                        String type = reader.readLine();
                        System.err.println("Show: " + id + " " + type);
                        setTeam(id, type);
                        continue;
                    } catch (Exception e) {
                    }

                    try {
                        reader = new BufferedReader(new InputStreamReader(serverSocket.accept().getInputStream()));
                        continue;
                    } catch (IOException e) {
                        System.err.println("Some problems with connection");
                    }
                }
            }
        }).start();

        controller.setLayout(new FlowLayout());

        JButton show = new JButton("show team screen");
        show.setMaximumSize(show.getPreferredSize());
        show.setMaximumSize(show.getPreferredSize());
        show.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(!isVisible());
                if (!isVisible()) {
                    show.setText("show team screen");
                    show.setMaximumSize(show.getPreferredSize());
                } else {
                    show.setText("hide team screen");
                    show.setMaximumSize(show.getPreferredSize());
                }
            }
        });
        controller.add(show);
        controller.setMaximumSize(controller.getPreferredSize());
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
