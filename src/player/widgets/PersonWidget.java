package player.widgets;

import player.TickPlayer;
import player.widgets.controllers.Controller;
import player.widgets.controllers.PersonController;

import java.awt.*;

/**
 * @author: pashka
 */
public class PersonWidget extends Widget {

    private final int MARGIN = (int) (30 * TickPlayer.scale);
    private final int Y = (int) (580 * TickPlayer.scale);
    private final int HEIGHT1 = (int) (60 * TickPlayer.scale);
    private final int HEIGHT2 = (int) (30 * TickPlayer.scale);
    private final Font FONT1 = Font.decode("Open Sans " + (int)(40 * TickPlayer.scale));
    private final Font FONT2 = Font.decode("Open Sans " + (int)(20 * TickPlayer.scale));

    private Controller controller;
    private String name;
    private String description;
    private final int position;
    private final long visibleTime;


    public PersonWidget(int position, long visibleTime, String name) {
        this.position = position;
        this.visibleTime = visibleTime;
        controller = new PersonController(this, name);
    }

    public void setPerson(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public void paint(Graphics2D g, int width, int height) {
        changeOpacity();
        if (opacity > 0) {
            int x1;
            int x2;
            int dx = 0;//(int) ((HEIGHT1 - HEIGHT2) * Widget.MARGIN);
            if (position == POSITION_LEFT) {
                x1 = MARGIN;
                x2 = x1 + dx;
            } else if (position == POSITION_RIGHT) {
                x1 = width - MARGIN;
                x2 = x1 - dx;
            } else {
                x1 = width / 2;
                x2 = x1;
            }
            int y = Y;
            g.setFont(FONT1);
            drawTextInRect(g, name, x1, y, -1, HEIGHT1, position, ADDITIONAL_COLOR, Color.white, opacityState);
            y += HEIGHT1 + 2;
            g.setFont(FONT2);
            if (description != null && description.length() != 0) {
                drawTextInRect(g, description, x2, y, -1, HEIGHT2, position, MAIN_COLOR, Color.white, opacityState);
            }
        }
    }

    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            new java.util.Timer().schedule(new java.util.TimerTask() {
                public void run() {
                    setVisible(false);
                }
            }, visibleTime);
        }
    }

    public Controller getController() {
        return controller;
    }
}
