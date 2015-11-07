package player.widgets;

import java.awt.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author: pashka
 */
public class TeamInfoClientWidget extends TeamInfoWidget {
    public PrintWriter writer;
    protected String ip;
    protected int port;
    protected String url;
    protected Socket socket;
    protected int teamId;

    public TeamInfoClientWidget(String url, int width, int height, String[] medias, int sleepTime, String ip, int port, String name) {
        super(url, width, height, medias, sleepTime, name);
        this.url = url;
        this.ip = ip;
        this.port = port;
    }

    public void setTeam(int id, String type) {
        while (true) {
            if (socket != null && !socket.isOutputShutdown()) {
                try {
                    System.err.println(socket);
                    writer.println(id);
                    writer.println(type);
                    System.err.println("Send: " + id + " " + type);
                    writer.flush();
                    if (!writer.checkError())
                        break;
                } catch (Exception e) {
                    System.err.println("Problems with input stream. Reconnect...");
                }
            }
            try {
                socket = new Socket(ip, port);
                writer = new PrintWriter(socket.getOutputStream());
            } catch (IOException e) {
                System.err.println("Problems with connecting");
            }
        }

        teamId = id;
        if (type.equals("info")) {
            change("teaminfo/" + String.format("%03d", id) + ".jpg");
        } else {
            change(url + "/video/" + type + "/" + id);
        }
    }

//    public void paint(Graphics2D g, int width, int height) {
//    }

    protected int getTeamId() {
        return teamId;
    }
}
