package com.infusion;

/**
 * Created by tvolkov on 12.12.15.
 */
public class Row {
    private String intrumentName;
    private String date;
    private double price;

    public Row(String intrumentName, String date, double price) {
        this.intrumentName = intrumentName;
        this.date = date;
        this.price = price;
    }

    public String getIntrumentName() {
        return intrumentName;
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
        if (intrumentName != null ? !intrumentName.equals(row.intrumentName) : row.intrumentName != null) return false;
        return date != null ? date.equals(row.date) : row.date == null;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = intrumentName != null ? intrumentName.hashCode() : 0;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        temp = Double.doubleToLongBits(price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
