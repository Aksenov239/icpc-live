package player.widgets.controllers;

import javax.swing.*;

/**
 * User: Aksenov Vitaly
 * Date: 07.04.2015
 * Time: 15:28
 */
public class Controller extends JPanel {
    private String name;
    
    public Controller(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
}
