package player.widgets;

import player.widgets.controllers.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author: pashka
 */
public class DoubleVideoWidget extends Widget {

    private final OverlayVideoWidget leftWidget;
    private final OverlayVideoWidget rightWidget;


    Controller controller;

    public DoubleVideoWidget(int x1, int y1, int x2, int y2, int width, int height, String[] medias, String url, int sleepTime, String name) {
        leftWidget = new OverlayVideoWidget(x1, y1, width, height, medias, url, sleepTime, "Left video");
        rightWidget = new OverlayVideoWidget(x2, y2, width, height, medias, url, sleepTime, "Right video");

        controller = new Controller("Double video controller");
        controller.setLayout(new BoxLayout(controller, BoxLayout.Y_AXIS));

        JButton show = new JButton("show videos");
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

        JButton hide = new JButton("hide videos");
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
    }

    @Override
    public Controller getController() {
        return controller;
    }

}
