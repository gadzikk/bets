package model;

import java.util.List;

public class Event {
    private String name;
    private List<SlotOdd> slotOdds;

    public Event(String name, List<SlotOdd> slotOdds) {
        this.name = name;
        this.slotOdds = slotOdds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SlotOdd> getSlotOdds() {
        return slotOdds;
    }

    public void setSlotOdds(List<SlotOdd> slotOdds) {
        this.slotOdds = slotOdds;
    }

    @Override
    public String toString() {
        return "model.Event{" +
                "name='" + name + '\'' +
                ", slotOdds=" + slotOdds +
                '}';
    }
}
