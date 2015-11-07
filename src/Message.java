/**
 * Created by aksenov on 08.05.2015.
 */
public class Message {
    String message;
    long creationTime;
    long endTime;
    boolean isAd;

    public Message(String message, long time, long duration, boolean isAd) {
        this.message = message;
        this.creationTime = time;
        this.endTime = time + duration;
        this.isAd = isAd;
    }

    public Message(String message, long time, long duration) {
        this(message, time, duration, false);
    }

    @Override
    public String toString() {
        return "Message{" +
                "isAd=" + isAd +
                ", endTime=" + endTime +
                ", creationTime=" + creationTime +
                ", message='" + message + '\'' +
                '}';
    }
}
