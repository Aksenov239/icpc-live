package player.widgets.controllers;

import javax.swing.*;
import java.awt.*;

/**
 * User: Aksenov Vitaly
 * Date: 07.04.2015
 * Time: 15:53
 */
public class ControllerGenerator {
    public static JFrame createControllerFrame(String name, Controller... controllers) {
        JFrame controller = new JFrame(name);
//        controller.getContentPane().setLayout(new GridLayout(2 * controllers.length, 1));
//        for (Controller controller1 : controllers) {
//            JLabel label = new JLabel(controller1.getName());
//            controller.getContentPane().add(label);
//            controller.getContentPane().add(controller1);
//        }
        controller.getContentPane().setLayout(new SpringLayout());
        for (Controller controller1 : controllers) {
            JLabel label = new JLabel(controller1.getName());
            controller.getContentPane().add(label);
            controller.getContentPane().add(controller1);
        }
        SpringUtilities.makeCompactGrid(controller.getContentPane(), 2 * controllers.length, 1, 0, 0, 0, 0);
        controller.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        controller.pack();
        return controller;
    }

    public static Controller createVideosControllerFrame(int w, int h, Controller... controllers) {
        Controller controller = new Controller("Videos controller");
        controller.setLayout(new GridLayout(w, h));
        for (Controller contr : controllers) {
            controller.add(contr);
        }
        return controller;
    }
}
