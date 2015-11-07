package events;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by aksenov on 17.04.2015.
 */
public class TeamInfo {

    private ArrayBlockingQueue<Run>[] problem_runs;

    public int id;
    public int rank;
    public String name;

    public int solved;
    public int penalty;
    public int lastAccepted;
    public String region;

    public String shortName;

    public TeamInfo(int problems) {
        problem_runs = new ArrayBlockingQueue[problems];
        for (int i = 0; i < problems; i++) {
            problem_runs[i] = new ArrayBlockingQueue<Run>(100);
        }
    }

    public ArrayBlockingQueue<Run>[] getRuns() {
        return problem_runs;
    }

    public ArrayBlockingQueue<Run> getRunsByProblem(int problemId) {
        return problem_runs[problemId];
    }

    public void addRun(Run run, int problemId){
        problem_runs[problemId].add(run);
    }

}
