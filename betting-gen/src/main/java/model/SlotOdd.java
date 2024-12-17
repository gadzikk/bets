package model;

public class SlotOdd {
    private int slot;
    private double odd;

    public SlotOdd(int slot, double odd) {
        this.slot = slot;
        this.odd = odd;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public double getOdd() {
        return odd;
    }

    public void setOdd(double odd) {
        this.odd = odd;
    }

    @Override
    public String toString() {
        return slot + "=" +odd;
    }
}
