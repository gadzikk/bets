package file;

import model.Event;
import model.SlotOdd;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class FileProcessor {
    public static final String TEMPLATE = "C:\\Users\\gadzi\\IdeaProjects\\betting-gen\\src\\main\\resources\\template.txt";
    public static final String EXHBS = "C:\\Users\\gadzi\\IdeaProjects\\betting-gen\\src\\main\\resources\\exhbs.txt";

    public List<Event> readTemplate() throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(TEMPLATE));
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

    public List<Event> readExhbs() throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(EXHBS));
        var events = new ArrayList<Event>();
        Event event = null;

        String line = br.readLine();
        while (line != null) {
            if (line.contains("-") && !isDouble(line)) {
                line = line.replace(" / zagraj wyższy kurs w HIT DNIA", "");
                event = new Event();
                event.setName(line);
            } else if (line.contains("BetBuilder") || line.contains("ZALICZKA")
                    || line.contains("+") || line.isBlank() || line.isEmpty()) {
                line = br.readLine();
                continue;
            } else if (line.contains(":")) {
                events.add(event);
            } else if (isDouble(line)) {
                var odd = Double.parseDouble(line);
                if (event.getSlotOdds().size() >= 3) {
                    line = br.readLine();
                    continue;
                } else {
                    event.addSlotOdd(odd);
                }
            }

            line = br.readLine();
        }
        var slotSize = events.get(0).getSlotOdds().size();
        if (slotSize == 2) {
            rearrangeResultsFor2Slot(events);
        } else if (slotSize == 3) {
            rearrangeResultsFor3Slot(events);
            removeUnexpectedDraws(events);
        }

        System.out.println("##### EXHBS #####");
//        events.forEach(e -> System.out.println(e.getName() + " " + e.getSlotOdds()));
        events.forEach(e -> {
            System.out.print(e.getName());
            e.getSlotOdds().forEach(so -> System.out.print(", " + so.getOdd()));
            System.out.println("");
        });
        System.out.println("##### EXHBS ##### \n");
        return events;
    }

    private void rearrangeResultsFor3Slot(List<Event> events) {
        events.stream().flatMap(e -> e.getSlotOdds().stream()).forEach(so -> {
            var slot = so.getSlot();
            if (slot == 0) {
                so.setSlot(1);
            } else if (slot == 1) {
                so.setSlot(0);
            }
        });
    }

    private void rearrangeResultsFor2Slot(List<Event> events) {
        events.stream().flatMap(e -> e.getSlotOdds().stream()).forEach(so -> {
            var slot = so.getSlot();
            if (slot == 0) {
                so.setSlot(1);
            } else if (slot == 1) {
                so.setSlot(2);
            }
        });
    }

    private void removeUnexpectedDraws(List<Event> events) {
        events.forEach(Event::filterUnexpectedDraws);
    }

    private boolean isDouble(String sequence) {
        if (sequence == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(sequence);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
