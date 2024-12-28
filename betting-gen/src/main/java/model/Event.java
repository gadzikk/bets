package model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Event {
    private String name;
    private List<SlotOdd> slotOdds;

    public Event() {
        this.slotOdds = new ArrayList<>();
    }

    public Event(String name, List<SlotOdd> slotOdds) {
        this.name = name;
        this.slotOdds = slotOdds;
    }

    public void filterUnexpectedDraws() {
        slotOdds = slotOdds.stream()
                .filter(so -> so.getOdd() <= 8.0)
                .collect(Collectors.toList());
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

    public void addSlotOdd(double odd) {
        slotOdds.add(new SlotOdd(slotOdds.size(), odd));
    }

    @Override
    public String toString() {
        return "model.Event{" +
                "name='" + name + '\'' +
                ", slotOdds=" + slotOdds +
                '}';
    }
}
