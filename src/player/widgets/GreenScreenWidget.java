package player.widgets;

import player.widgets.controllers.Controller;
import player.widgets.controllers.SpringUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author: pashka
 */
public class GreenScreenWidget extends Widget {
    private Controller controller;
    private final Color color = Color.black;

    public GreenScreenWidget() {
        controller = new Controller("");
        controller.setSize(new Dimension(200, 200));

        controller.setLayout(new FlowLayout());

        JButton show = new JButton("show");
        show.setMaximumSize(show.getPreferredSize());
        show.setMaximumSize(show.getPreferredSize());
        show.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(!isVisible());
                if (isVisible()) {
                    show.setText("show");
                    show.setMaximumSize(show.getPreferredSize());
                } else {
                    show.setText("hide");
                    show.setMaximumSize(show.getPreferredSize());
                }
            }
        });
        controller.add(show);
        controller.setMaximumSize(controller.getPreferredSize());
        controller.setMinimumSize(controller.getPreferredSize());
//        SpringUtilities.makeCompactGrid(controller, 1, 1, 0, 0, 0, 0);
    }

    @Override
    public void paint(Graphics2D g, int width, int height) {
        if (isVisible()) {
            g.setColor(color);
            g.fillRect(0, 0, width, height);
        }
    }

    @Override
    public Controller getController() {
        return controller;
    }
}
