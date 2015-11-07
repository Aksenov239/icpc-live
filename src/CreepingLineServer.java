import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;

/**
 * Created by aksenov on 30.04.2015.
 */
public class CreepingLineServer {

    static final int ADVERT_TIMEOUT = 20000;
    final ServerSocket socket;

    public class Binder implements Runnable {
        public void run() {
            while (true) {
                try {
                    Socket client = socket.accept();
                    System.err.println("incoming connection from " + client.getInetAddress().toString());
                    new Thread(new Sender(client)).start();
                } catch (IOException e) {
                }
            }
        }
    }

    public class Sender implements Runnable {
        private Socket socket;

        public Sender(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try (BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream())) {
                while (true) {
                    Message[] pool = messagesTableModel.getMessages();
                    if (pool == null) {
                        System.err.println("pool == null");
                        continue;
                    }
                    for (int i = 0; i < pool.length; i++) {
                        if (!pool[i].isAd) {
                            out.write(("+" + pool[i].message + "\n").getBytes(StandardCharsets.UTF_8));
                        }
                    }
                    out.flush();
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
            }
        }
    }

    JTable messages;
    MessageTableModel messagesTableModel;

    public class Ticker extends Timer {
        public Ticker(int delay) {
            super(delay, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    messagesTableModel.tickMessages();
                }
            });
        }
    }

    public CreepingLineServer(int port) throws IOException {
        socket = new ServerSocket(port);

        new Thread(new Binder()).start();
        new Ticker(1000).start();
    }

    public void run() {
        JFrame controlFrame = new JFrame("Creeping line controller");

        final JTextField area = new JTextField(50);
        JButton addButton = new JButton("Add");
        final JCheckBox advertBox = new JCheckBox("Advert");
        messagesTableModel = new MessageTableModel();
        messages = new JTable(messagesTableModel);
        messages.setSize(new Dimension(200, 200));
        messages.setMinimumSize(new Dimension(200, 200));
        messages.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() >= 2) {
                    area.setText((String) messages.getValueAt(messages.getSelectedRow(), 0));
                }
            }
        });
        final JComboBox combo = new JComboBox(new String[]{"30 seconds", "1 minute", "2 minutes", "5 minutes", "2147483647 milliseconds"});
        final int[] duration = new int[]{30000, 60000, 120000, 300000, Integer.MAX_VALUE};

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                messagesTableModel.addMessage(new Message(area.getText(), System.currentTimeMillis(), duration[combo.getSelectedIndex()], advertBox.isSelected()));
                area.setText("");
            }
        });
        area.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    messagesTableModel.addMessage(new Message(area.getText(), System.currentTimeMillis(), duration[combo.getSelectedIndex()], advertBox.isSelected()));
                    area.setText("");
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        JButton remove = new JButton("Remove");
        remove.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                messagesTableModel.removeMessages(messages.getSelectedRows());
            }
        });
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(messages);

        controlFrame.getContentPane().setLayout(new BoxLayout(controlFrame.getContentPane(), BoxLayout.Y_AXIS));
        combo.setMinimumSize(combo.getPreferredSize());
        combo.setMaximumSize(combo.getPreferredSize());
        controlFrame.getContentPane().add(combo);
        controlFrame.getContentPane().add(advertBox);

        area.setMinimumSize(area.getPreferredSize());
        area.setMaximumSize(area.getPreferredSize());
        controlFrame.getContentPane().add(area);

        addButton.setMinimumSize(addButton.getPreferredSize());
        addButton.setMaximumSize(addButton.getPreferredSize());
        controlFrame.getContentPane().add(addButton);
        addButton.setAlignmentX(Button.CENTER_ALIGNMENT);
        remove.setMaximumSize(remove.getPreferredSize());
        remove.setMinimumSize(remove.getPreferredSize());
        controlFrame.getContentPane().add(remove);
        remove.setAlignmentX(Button.CENTER_ALIGNMENT);
        controlFrame.getContentPane().add(scrollPane);
        controlFrame.pack();
        controlFrame.setVisible(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Random rnd = new Random(System.nanoTime());
                    while (true) {
                        List<Message> fmsg = new ArrayList<>(Arrays.asList(messagesTableModel.getMessages()));
                        for (Iterator<Message> it = fmsg.iterator(); it.hasNext(); ) {
                            if (!it.next().isAd) {
                                it.remove();
                            }
                        }
                        if (!fmsg.isEmpty()) {
                            Message e = fmsg.get(rnd.nextInt(fmsg.size()));
                            messagesTableModel.addMessage(new Message(e.message, System.currentTimeMillis(), 7000));
                        }
                        Thread.sleep(ADVERT_TIMEOUT);
                    }
                } catch (InterruptedException e) {

                }
            }
        }).start();
        controlFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) throws IOException {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("resources/generator.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        new CreepingLineServer(Integer.parseInt(properties.getProperty("creeping.line.port"))).run();
    }
}
