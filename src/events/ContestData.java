package events;

import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by aksenov on 05.05.2015.
 */
public class ContestData {
    ArrayBlockingQueue<Run> runs;
    String[] languages = new String[4];
    TeamInfo[] teamInfos = new TeamInfo[200];
    public double[] timeFirstSolved = new double[20];
    int teamNumber;
    int problemNumber;

    TeamInfo[] standings;
    public long startTime;

    void recalcStandings() {
        standings = new TeamInfo[teamNumber];
        int n = 0;
        Arrays.fill(timeFirstSolved, 1e100);
        for (TeamInfo team : teamInfos) {
            if (team == null) continue;

            team.solved = 0;
            team.penalty = 0;
            team.lastAccepted = 0;
            for (int j = 0; j < problemNumber; j++) {
                ArrayBlockingQueue<Run> runs = team.getRuns()[j];
                int wrong = 0;
                for (Run run : runs) {
                    if (run.result.equals("AC")) {
                        team.solved++;
                        int time = (int) run.time / 60;
                        team.penalty += wrong * 20 + time;
                        team.lastAccepted = Math.max(team.lastAccepted, time);
                        timeFirstSolved[j] = Math.min(timeFirstSolved[j], run.time);
                        break;
                    } else if (run.result.length() > 0) {
                        wrong++;
                    }
                }
            }
            standings[n++] = team;
        }

        Comparator<TeamInfo> comparator = new Comparator<TeamInfo>() {
            @Override
            public int compare(TeamInfo o1, TeamInfo o2) {
                if (o1.solved != o2.solved) {
                    return -Integer.compare(o1.solved, o2.solved);
                }
                if (o1.penalty != o2.penalty) {
                    return Integer.compare(o1.penalty, o2.penalty);
                }
                return Integer.compare(o1.lastAccepted, o2.lastAccepted);
            }
        };
        Arrays.sort(standings, 0, n, comparator);

        for (int i = 0; i < n; i++) {
            if (i > 0 && comparator.compare(standings[i], standings[i - 1]) == 0) {
                standings[i].rank = standings[i - 1].rank;
            } else {
                standings[i].rank = i + 1;
            }
        }
    }

    public TeamInfo getTeamInfo(int teamId) {
        return teamInfos[teamId];
    }

    public int getPosition(int teamId) {
        if (standings == null) {
            return 1;
        }
        for (int i = 0; i < standings.length; i++) {
            if (standings[i].id == teamId) {
                return i;
            }
        }
        return -1;
    }

    public int getId(int position) {
        return standings == null ? teamInfos[position + 1].id : standings[position].id;
    }

    public int getIdByName(String name) {
        for (int i = 0; i < teamNumber; i++) {
            if (teamInfos[i + 1].name.equals(name) || teamInfos[i + 1].shortName.equals(name)) {
                return i + 1;
            }
        }
        return -1;
    }

    public int getTeamNumber() {
        return teamNumber;
    }

    public int getProblemNumber() {
        return problemNumber;
    }

    public TeamInfo[] getStandings() {
        return standings;
    }
}
