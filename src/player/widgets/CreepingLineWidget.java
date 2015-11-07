package player.widgets;

import player.TickPlayer;
import player.widgets.controllers.Controller;
import player.widgets.controllers.CreepingLineController;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentSkipListSet;


/**
 * @author: pashka
 */
public class CreepingLineWidget extends Widget {

    private static final double V = 0.1;
    private double SEPARATOR = 50 * TickPlayer.scale;
    public int HEIGHT = (int) (32 * TickPlayer.scale);

    ArrayBlockingQueue<String> messagesQueue = new ArrayBlockingQueue<String>(100);
    ArrayDeque<Message> messagesOnScreen = new ArrayDeque<Message>();
    ConcurrentSkipListSet<String> inQueue = new ConcurrentSkipListSet<String>();

    long last;
    CreepingLineController controller;

    public class Updater implements Runnable {
        private String ip;
        private int port;

        public Updater(String ip, int port) {
            this.ip = ip;
            this.port = port;
        }

        public void run() {
            while (true) {
                try {
                    Socket socket = new Socket(ip, port);

                    BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
//                    while (true) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        if (line.startsWith("+")) {
                            String text = line.substring(1);
                            if (!inQueue.contains(text)) {
                                inQueue.add(text);
                                messagesQueue.add(text);
                            }
                        } else {
                            String text = line.substring(1);
                            inQueue.remove(text);
                        }
                    }
//                    }
                } catch (Exception e) {
                    System.err.println("cannot load messages");
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e1) {
                    }
//                    e.printStackTrace();
                }
            }
        }
    }

    public CreepingLineWidget(String ip, int port) {
        last = System.currentTimeMillis();
        controller = new CreepingLineController(this);

        new Thread(new Updater(ip, port)).start();
    }

    Font messageFont = Font.decode("Open Sans " + (int) (20 * TickPlayer.scale));

    @Override
    public void paint(Graphics2D g, int width, int height) {
//            g2.setColor(Color.red);
//            g2.setComposite(AlphaComposite.SrcOver.derive(0.3f));
//            g2.fillRoundRect(100, 100, 100, 80, 32, 32);
        g.setComposite(AlphaComposite.SrcOver.derive((float) (textOpacity)));
        g.setColor(MAIN_COLOR);
        g.fillRect(0, height - HEIGHT, width, HEIGHT);
        g.setComposite(AlphaComposite.SrcOver.derive((float) (1)));
        g.setFont(messageFont);
        g.setColor(Color.white);
        long time = System.currentTimeMillis();
        int dt = (int) (time - last);
        last = time;

        if (messagesQueue.size() > 0) {
            if (messagesOnScreen.size() == 0 ||
                    messagesOnScreen.getLast().position + messagesOnScreen.getLast().width + SEPARATOR < width) {
                Message message = null;
                while (messagesQueue.size() > 0) {
                    String text = messagesQueue.poll();
                    if (inQueue.contains(text)) {
                        message = new Message(
                                text, g
                        );
                        break;
                    }
                }
                if (message != null) {
                    message.position = width;
                    messagesOnScreen.addLast(message);
                }
            }
        }
        for (Message message : messagesOnScreen) {
            message.position -= V * dt;
            if (message.position + message.width >= 0) {
                g.drawString(message.message, (float) message.position, height - (int)(9 * TickPlayer.scale));
            }
        }
        while (messagesOnScreen.size() > 0 && messagesOnScreen.getFirst().position + messagesOnScreen.getFirst().width < 0) {
            Message toRemove = messagesOnScreen.removeFirst();
            inQueue.remove(toRemove.message);
        }
    }

    public void addMessage(String s) {
        inQueue.add(s);
        messagesQueue.add(s);
    }

    class Message {
        String message;
        double position;
        int width;

        public Message(String message, Graphics2D g) {
            this.message = message;
            width = g.getFontMetrics().stringWidth(message);
        }
    }

    public Controller getController() {
        return controller;
    }
}
