package player.widgets.controllers;

import player.widgets.ClockWidget;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by aksenov on 13.04.2015.
 */
public class ClockController extends Controller {

    private void mainControllerPart(final ClockWidget widget) {
        setLayout(new FlowLayout());

        JButton show = new JButton("show/hide");
        show.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                widget.setVisible(!widget.isVisible());
            }
        });

        add(show);

        setMaximumSize(getPreferredSize());
    }

    public ClockController(final ClockWidget widget, String name) {
        super(name);
        mainControllerPart(widget);
    }

}
