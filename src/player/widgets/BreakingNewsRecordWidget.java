package player.widgets;

import events.ContestData;
import events.Run;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author: pashka
 */
public class BreakingNewsRecordWidget extends BreakingNewsWidget {


    public BreakingNewsRecordWidget(String name, String url, Properties properties, Widget... blockingWidgets) {
        super(name, url, properties, blockingWidgets);
    }

    protected void showVideo(int problemId, ContestData contestData) {
        ArrayBlockingQueue<Run> queue = contestData.getTeamInfo(teamId).getRunsByProblem(problemId);

        Run[] runs = queue.toArray(new Run[queue.size()]);

        if (runs.length != 0) {
            System.err.println("BREAKING " + teamId + " " + (char) ('A' + problemId));
            System.err.println("Trying to open: " + (url + "/video/reaction/" + (runs[runs.length - 1]).id));

            Run run = runs[runs.length - 1];
            for (Run r : runs) {
                if (r.result.equals("AC")) {
                    run = r;
                    break;
                }
            }

            String videoUrl = url + "/video/reaction/" + (run.id);
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
            if (run.result.equals("AC")) {
                if (run.time == contestData.timeFirstSolved[problemId]) {
                    info = "First to solve";
                } else {
                    info = "Solved";
                }
            } else if (run.result.length() == 0) {
                info = "Submitted";
            } else {
                info = "Wrong submission on";
            }
            info += " problem " + (char) ('A' + problemId);
            opacityState = -1;
            widget.opacityState = -1;
        }
    }
}
