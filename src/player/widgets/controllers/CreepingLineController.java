package player.widgets.controllers;

import player.widgets.CreepingLineWidget;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author: pashka
 */
public class CreepingLineController extends Controller {

    private final CreepingLineWidget painter;

    public CreepingLineController(final CreepingLineWidget painter) throws HeadlessException {
        super("Creeping line controller");
        this.painter = painter;
        this.setSize(200, 100);
        final JTextField textField = new JTextField(50);
        textField.setText("Tsinghua Univ is the first team solved problem D. Congratulations!");
        JButton button = new JButton("Send!");
        setLayout(new FlowLayout());
        add(textField);
        add(button);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                painter.addMessage(textField.getText());
            }
        });
        setMinimumSize(getPreferredSize());
        setMaximumSize(getPreferredSize());
    }

}
