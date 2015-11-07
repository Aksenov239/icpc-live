package player.widgets.controllers;

import events.ContestData;
import events.EventsLoader;
import events.TeamInfo;
import player.widgets.TeamInfoWidget;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author: pashka
 */
public class TeamInfoController extends Controller {

    public TeamInfoController(final TeamInfoWidget widget) throws HeadlessException {
        super("Team info controller");

        this.setSize(200, 100);
        final JPanel controlPanel = new JPanel();
        //final JTextField textField = new JTextField(50);
        final ButtonGroup group = new ButtonGroup();
        JRadioButton[] buttons = new JRadioButton[]{new JRadioButton("screen"), new JRadioButton("camera"), new JRadioButton("info")};
        for (int i = 0; i < buttons.length; i++) {
            group.add(buttons[i]);
            controlPanel.add(buttons[i]);
        }
//        final JComboBox combo = new JComboBox(new String[]{"screen", "camera", "info"});
        JButton button = new JButton("Show!");
        //add(new JLabel("Team id"));
        //add(textField);
//        controlPanel.add(combo);
        controlPanel.add(button);

        ContestData contestData = EventsLoader.getContestData();
        while (contestData.getTeamNumber() == 0) {
            ;
        }
//        Standings standings = StandingsLoader.getLoaded();
        String[] data = new String[contestData.getTeamNumber() - 1];
        for (int i = 0; i < data.length; i++) {
            TeamInfo teamInfo = contestData.getTeamInfo(i + 1);
            if (teamInfo != null)
                data[i] = (i + 1) + ". " + teamInfo.name + " (" + teamInfo.shortName + ")";
        }

        final JList list = new JList(data);

        list.setLayoutOrientation(JList.VERTICAL_WRAP);
        list.setVisibleRowCount(-1);
        list.setSelectedIndex(0);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (JRadioButton button : buttons) {
                    if (button.isSelected())
                        widget.setTeam(list.getSelectedIndex() + 1, button.getText());
                    System.err.println(button.getText());
                }
            }
        });

        setLayout(new SpringLayout());
        controlPanel.setSize(new Dimension(200, 200));
        controlPanel.setMaximumSize(controlPanel.getPreferredSize());
        controlPanel.setMinimumSize(controlPanel.getPreferredSize());
        add(controlPanel);
        JScrollPane pane = new JScrollPane(list);
        add(pane);
        SpringUtilities.makeCompactGrid(this, 2, 1, 0, 0, 0, 0);
//        setMinimumSize(getPreferredSize());
//        setMaximumSize(getPreferredSize());
    }

}
