package player.widgets;

import events.ContestData;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.Properties;

/**
 * @author: pashka
 */
public class BreakingNewsLiveWidget extends BreakingNewsWidget {


    public BreakingNewsLiveWidget(String name, String url, Properties properties, Widget... blockingWidgets) {
        super(name, url, properties, blockingWidgets);
        showTime = Integer.MAX_VALUE;
    }

    protected void showVideo(int problemId, ContestData contestData) {

        System.err.println("BREAKING LIVE" + teamId + " " + (char) ('A' + problemId));
        System.err.println("Trying to open: " + (url + "/video/camera/" + teamId));


        String videoUrl = url + "/video/camera/" + teamId;
        try {
            HttpURLConnection huc = (HttpURLConnection) (new URL(videoUrl)).openConnection();
            huc.setRequestMethod("GET");
            huc.setRequestProperty("Authorization", "Basic " + Base64.getEncoder().encodeToString((login + ":" + password).getBytes()));
            huc.connect();
            System.err.println("Response code " + huc.getResponseCode() + " " + huc.getContentType() + " " + huc.getContentLength());
            if (huc.getResponseCode() != 200) {
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }


        widget.change(videoUrl);
        setVisible(true);
        show = System.currentTimeMillis();
        info = "Submitted";
        info += " problem " + (char) ('A' + problemId);
        opacityState = -1;
        widget.opacityState = -1;
    }
}
