package player.widgets;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import player.widgets.controllers.*;

/**
 * @author: pashka
 */
public class LogoWidget extends Widget {
    private final BufferedImage logo;
    private Controller controller;

    public LogoWidget() {
        BufferedImage logo;
        try {
            logo = ImageIO.read(new File("pics/logo.png"));
        } catch (IOException e) {
            logo = null;
        }
        this.logo = logo;

        controller = new Controller("Logo widget");
    }

    @Override
    public void paint(Graphics2D g, int width, int height) {
        g.drawImage(logo, null, width - 120, 20);
    }

    public Controller getController() {
        return controller;
    }
}
