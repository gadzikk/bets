package file;

import model.Event;
import model.SlotOdd;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class FileProcessor {
    public static final String SOURCE = "C:\\Users\\gadzi\\IdeaProjects\\betting-gen\\src\\main\\resources\\template.txt";

    public List<Event> read() throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(SOURCE));
        var events = new ArrayList<Event>();

        String line = br.readLine();
        while (line != null) {
            String[] extractedLine = line.split(",");
            if (extractedLine.length == 4) {
                events.add(
                        new Event(extractedLine[0], List.of(
                                new SlotOdd(1, Double.parseDouble(extractedLine[1])),
                                new SlotOdd(0, Double.parseDouble(extractedLine[2])),
                                new SlotOdd(2, Double.parseDouble(extractedLine[3])))
                        )
                );
            } else {
                events.add(
                        new Event(extractedLine[0], List.of(
                                new SlotOdd(1, Double.parseDouble(extractedLine[1])),
                                new SlotOdd(2, Double.parseDouble(extractedLine[2]))))
                );
            }


            line = br.readLine();
        }
        System.out.println("##### FILE #####");
        events.forEach(e -> System.out.println(e.getName() + " " + e.getSlotOdds()));
        System.out.println("##### FILE ##### \n");
        return events;
    }
}
