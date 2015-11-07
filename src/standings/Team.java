package standings;

/**
 * @author: pashka
 */
public class Team implements Comparable<Team> {

    public int id;
    public int rank;
    public String name;

    public int solved;
    public int penalty;
    public String group;

    public Result[] results;
    public String shortName;

    @Override
    public int compareTo(Team o) {
        return Integer.compare(rank, o.rank);
    }

    public static class Result {
        public int attempts;
        public int pending;
        public int time;
        public State state;
    }

    enum State {
        TRIED,
        SOLVED,
        FIRST,
        PEND
    }

}
