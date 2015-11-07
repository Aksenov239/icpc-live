package standings;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: pashka
 */
public class Standings {

    public Team[] teams;
    public Team[] teamById = new Team[200];

    static Map<String, String> shortNames = new HashMap<String, String>();

    static {
        try {
            BufferedReader in = new BufferedReader(new FileReader("shortnames.txt"));
            while (true) {
                String s = in.readLine();
                String[] ss = s.split("->");
                shortNames.put(ss[0].trim(), ss[1].trim());
            }
        } catch (Exception e) {
        }
    }

    static String shortName(String name) {
        if (shortNames.containsKey(name)) {
            return shortNames.get(name);
        } else if (name.length() > 15) {
            return name.substring(0, 12) + "...";
        } else {
            return name;
        }
    }

    public int getId(String name) {
        for (int i = 0; i < teamById.length; i++) {
            if (teamById[i] != null && teamById[i].name.equals(name)) {
                return i;
            }
        }
        return -1;
    }
}
