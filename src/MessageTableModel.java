import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;

public class MessageTableModel extends AbstractTableModel {

    private final ArrayList<TableModelListener> listeners = new ArrayList<>();
    private final java.util.List<Message> messages = Collections.synchronizedList(new ArrayList<Message>());
    final String BACKUP_FILENAME = "messages.backup";

    public MessageTableModel() {
        File file = new File(BACKUP_FILENAME);
        if (file.exists()) {
            try {
                Scanner sc = new Scanner(file, "UTF-8");
                while (sc.hasNextLine()) {
                    long start = Long.parseLong(sc.nextLine());
                    long end = Long.parseLong(sc.nextLine());
                    String msg = sc.nextLine();
                    boolean isAd = Boolean.parseBoolean(sc.nextLine());
                    messages.add(new Message(msg, start, end - start, isAd));
                }
                sc.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Thread.interrupted()) {
                    try {
                        File tmpFile = new File(BACKUP_FILENAME + ".tmp");
                        PrintWriter out = new PrintWriter(tmpFile);
                        for (Message e : messages) {
                            out.println(e.creationTime);
                            out.println(e.endTime);
                            out.println(e.message);
                            out.println(e.isAd);
                        }
                        out.close();
                        Files.move(tmpFile.toPath(), new File(BACKUP_FILENAME).toPath(), StandardCopyOption.REPLACE_EXISTING);
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public int getRowCount() {
        return messages.size();
    }

    public int getColumnCount() {
        return 3;
    }

    public Object getValueAt(int row, int col) {
        if (col == 0) {
            return messages.get(row).message;
        } else if (col == 1){
            return (messages.get(row).endTime - System.currentTimeMillis()) / 1000;
        } else {
            return messages.get(row).isAd + "";
        }
    }

    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return "Message";
            case 1:
                return "Time";
            case 2:
                return "Advert";
        }
        return "";
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
        listeners.add(l);
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
        listeners.remove(l);
    }

    public void fireListeners(boolean structural) {
        TableModelEvent e = structural ? new TableModelEvent(this) : new TableModelEvent(this, 0, messages.size() - 1);
        for (TableModelListener l : listeners) {
            l.tableChanged(e);
        }
    }

    public void addMessage(Message message) {
        messages.add(message);
        fireListeners(true);
    }

    public Message[] getMessages() {
        return messages.toArray(new Message[messages.size()]);
    }

    public void tickMessages() {
        synchronized (messages) {
            long time = System.currentTimeMillis();
            boolean wasRemoved = false;
            for (Iterator<Message> it = messages.iterator(); it.hasNext(); ) {
                Message message = it.next();
                if (message.endTime < time) {
                    it.remove();
                    wasRemoved = true;
                }
            }
            fireListeners(wasRemoved);
        }
    }

    public void removeMessage(int id) {
        removeMessages(new int[]{id});
    }

    public void removeMessages(int[] id) {
        synchronized (messages) {
            Arrays.sort(id);
            for (int i = id.length - 1; i >= 0; i--) {
                messages.remove(id[i]);
            }
            fireListeners(id.length > 0);
        }
    }
}
