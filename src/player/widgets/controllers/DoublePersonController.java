package player.widgets.controllers;

import player.widgets.PersonWidget;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by aksenov on 13.04.2015.
 */
public class DoublePersonController extends Controller {
    private JList sourceList;
    private Person[] personsList;

    public class Person implements Comparable<Person> {
        int id;
        String name;
        String description;

        public Person(int id, String name, String description) {
            this.id = id;
            this.name = name;
            this.description = description;
        }

        public int compareTo(Person p) {
            return Integer.compare(id, p.id);
        }

        public String toString() {
            return id + ". " + name;
        }
    }

    public void reload() {
        int selectedIndex = sourceList.getSelectedIndex();
        Person selectedPerson = personsList == null || personsList.length <= selectedIndex || selectedIndex < 0 ? null : personsList[selectedIndex];

        DefaultListModel listModel = (DefaultListModel) sourceList.getModel();

        try {
            BufferedReader br = new BufferedReader(new FileReader("persons"));
            String line;
            int l = 0;
            personsList = new Person[1000];
            while ((line = br.readLine()) != null) {
                String[] z = line.split(";");
                personsList[l++] = new Person(l, z[0], z.length == 1 ? "" : z[1]);
            }

            personsList = Arrays.copyOf(personsList, l);

            Arrays.sort(personsList);
        } catch (IOException e) {
            e.printStackTrace();
        }

        listModel.removeAllElements();
        for (int i = 0; i < personsList.length; i++) {
            listModel.insertElementAt(personsList[i].toString(), i);
        }

        int id = 0;
        for (int i = 0; i < personsList.length; i++) {
            if (selectedPerson != null && selectedPerson.name.equals(personsList[i].name)) {
                id = i;
            }
        }
        sourceList.setSelectedIndex(id);
    }

    private void mainControllerPart(final PersonWidget personWidget) {
        setLayout(new SpringLayout());

        JButton show = new JButton("show/hide");
        show.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                personWidget.setVisible(!personWidget.isVisible());
            }
        });

        add(show);

        DefaultListModel listModel = new DefaultListModel();

        sourceList = new JList(listModel);
        sourceList.setSize(new Dimension(200, 200));
        sourceList.setLayoutOrientation(JList.VERTICAL);
//        sourceList.setVisibleRowCount(-1);

        sourceList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                int personId = sourceList.isSelectedIndex(e.getFirstIndex()) ? e.getFirstIndex() : e.getLastIndex();
                if (personId >= 0 && personId < personsList.length)
                    personWidget.setPerson(personsList[personId].name, personsList[personId].description);
            }
        });

        reload();

        if (personsList.length != 0)
            sourceList.setSelectedIndex(0);

        JScrollPane scrollPane = new JScrollPane(sourceList);
        scrollPane.setSize(new Dimension(200, 200));

        add(scrollPane);

        SpringUtilities.makeCompactGrid(this, 2, 1, 0, 0, 0, 0);
    }

    public DoublePersonController(final PersonWidget widget, String name) {
        super(name);

        mainControllerPart(widget);
    }

}
