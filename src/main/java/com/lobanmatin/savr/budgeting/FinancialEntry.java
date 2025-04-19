package com.lobanmatin.savr.budgeting;
import java.util.List;

public class FinancialEntry {
    protected final double value;
    protected final Frequency frequency;
    protected List<String> categories;


    public FinancialEntry(double value, Frequency frequency, List<String> categories) {
        this.frequency = frequency;
        this.categories = categories;
        this.value = value;
    }

    public double getFreqAmount(Frequency reqFrequency) {
        // HANDLE ERRORS FOR ONE TIME FREQ
        return this.value*(this.frequency.getPeriodsPerYear())/(reqFrequency.getPeriodsPerYear());
    }

    public void removeCategory(int index) {

    }

    public double getValue() { return this.value; }
    public Frequency getFrequency() { return this.frequency; }
    public List<String> getCategory() { return this.categories; }
    public void setCategory(int indexRemove, String newCategory) {
        // ERROR HANDLE INVALID INDEX
        this.categories.remove(indexRemove);
        this.categories.add(newCategory);
    }
}
