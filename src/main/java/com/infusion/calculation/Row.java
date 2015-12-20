package com.infusion.calculation;

public class Row {
    private String instrumentName;
    private String date;
    private double price;

    public Row(String instrumentName, String date, double price) {
        this.instrumentName = instrumentName;
        this.date = date;
        this.price = price;
    }

    public String getInstrumentName() {
        return instrumentName;
    }

    public String getDate() {
        return date;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Row)) return false;

        Row row = (Row) o;

        if (Double.compare(row.price, price) != 0) return false;
        if (instrumentName != null ? !instrumentName.equals(row.instrumentName) : row.instrumentName != null) return false;
        return date != null ? date.equals(row.date) : row.date == null;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = instrumentName != null ? instrumentName.hashCode() : 0;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        temp = Double.doubleToLongBits(price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
