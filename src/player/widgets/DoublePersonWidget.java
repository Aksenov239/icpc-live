package player.widgets;

import player.widgets.controllers.Controller;
import player.widgets.controllers.PersonController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Properties;


/**
 * @author: pashka
 */
public class DoublePersonWidget extends Widget {

    private final PersonWidget leftWidget;
    private final PersonWidget rightWidget;
    private final PersonWidget centralWidget;

    Controller controller;

    private void dump(HashMap<String, String> persons) {
        try {
            PrintWriter out = new PrintWriter("persons");

            for (String person : persons.keySet()) {
                out.println(person + ";" + persons.get(person));
            }

            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DoublePersonWidget(String name, Properties property) {
        leftWidget = new PersonWidget(POSITION_LEFT, Long.parseLong(property.getProperty("person.left")), "Left person");
        rightWidget = new PersonWidget(POSITION_RIGHT, Long.parseLong(property.getProperty("person.right")), "Right person");
        centralWidget = new PersonWidget(POSITION_CENTER, Long.parseLong(property.getProperty("person.center")), "Center person");

        controller = new Controller("Persons info controller");
        controller.setLayout(new BoxLayout(controller, BoxLayout.Y_AXIS));

        JButton show = new JButton("show info lines");
        show.setAlignmentX(Component.CENTER_ALIGNMENT);

        show.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        leftWidget.setVisible(true);
                        rightWidget.setVisible(true);
                    }
                });

        controller.add(show);

        JButton hide = new JButton("hide info lines");
        hide.setAlignmentX(Component.CENTER_ALIGNMENT);
        hide.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        leftWidget.setVisible(false);
                        rightWidget.setVisible(false);
                    }
                });
        controller.add(hide);

        final JTextField personName = new JTextField(50);
        final JTextField description = new JTextField(50);

        personName.setMaximumSize(personName.getPreferredSize());
        personName.setMinimumSize(personName.getPreferredSize());

        description.setMaximumSize(description.getPreferredSize());
        description.setMinimumSize(description.getPreferredSize());

        controller.add(personName);
        controller.add(description);

        JButton showSpecial = new JButton("show special");
        showSpecial.setAlignmentX(Component.CENTER_ALIGNMENT);
        showSpecial.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                centralWidget.setPerson(personName.getText(), description.getText());
                centralWidget.setVisible(true);
                personName.setText("");
                description.setText("");
            }
        });
        controller.add(showSpecial);

        final JButton reload = new JButton("reload");
        reload.setAlignmentX(Component.CENTER_ALIGNMENT);
        reload.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((PersonController) leftWidget.getController()).reload();
                ((PersonController) rightWidget.getController()).reload();
            }
        });

        controller.add(reload);

        JPanel doubleVideo = new JPanel();
        doubleVideo.setLayout(new GridLayout(1, 2));
        doubleVideo.add(leftWidget.getController());
        doubleVideo.add(rightWidget.getController());
        controller.add(doubleVideo);
    }

    @Override
    public void paint(Graphics2D g, int width, int height) {
        leftWidget.paint(g, width, height);
        rightWidget.paint(g, width, height);
        centralWidget.paint(g, width, height);
    }

    @Override
    public Controller getController() {
        return controller;
    }

}
