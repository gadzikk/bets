package model;

public class Result {
    private String name;
    private int slot;
    private double odd;

    public Result(String name, int slot, double odd) {
        this.name = name;
        this.slot = slot;
        this.odd = odd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        return name + " " + slot + "=" + odd;
    }
}
