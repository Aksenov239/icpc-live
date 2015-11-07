package standings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author: pashka
 */
public class StandingsLoader {

    public static final int PERIOD = 6000;

    private static AtomicReference<Standings> standings;

    public static Standings getLoaded() {
        return standings.get();
    }

    public StandingsLoader() {
//        try {
//            System.setProperty("file.encoding", "UTF-8");
//            Field charset = Charset.class.getDeclaredField("defaultCharset");
//            charset.setAccessible(true);
//            charset.set(null, null);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        standings = new AtomicReference<>(new Standings());
    }

    public void updater(final String url) {
        new java.util.Timer().scheduleAtFixedRate(
                new java.util.TimerTask() {
                    public void run() {
                        try {
                            loadFromWeb(url);
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, 0, PERIOD
        );
    }

    public void loadOnce(final String url) {
        try {
            loadFromWeb(url);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

    }

    private void loadFromFile(String filename) throws IOException, JSONException {
        byte[] encoded = Files.readAllBytes(Paths.get(filename));
        load(new String(encoded));
    }

    private void loadFromWeb(String filename) throws IOException, JSONException {
        URL url = new URL(filename);
        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), Charset.forName("UTF8")));
        StringBuilder file = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            file.append(line);
            file.append("\n");
        }
        load(file.toString());
    }

    private void load(String file) throws IOException, JSONException {
        JSONObject json = new JSONObject(file);
        JSONArray scoreboard = json.getJSONArray("scoreboard");
        int n = scoreboard.length();
        Standings res = new Standings();
        res.teams = new Team[n];

        for (int i = 0; i < n; i++) {
            JSONObject row = scoreboard.getJSONObject(i);
            Team team = new Team();
            team.rank = row.getInt("rank");
            team.solved = row.getInt("solved");
            team.penalty = row.getInt("score");
            team.name = row.getString("name");
            team.shortName = Standings.shortName(team.name);
            team.id = row.getInt("id");
            res.teams[i] = team;
            res.teamById[team.id] = team;
        }
        Arrays.sort(res.teams);
        standings.set(res);
    }

}
