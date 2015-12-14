package com.infusion;

/**
 * Created by tvolkov on 12.12.15.
 */
public class Row {
    private String intrumentName;
    private String date;
    private String price;

    public Row(String intrumentName, String date, String price) {
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

    public String getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Row)) return false;

        Row row = (Row) o;

        if (intrumentName != null ? !intrumentName.equals(row.intrumentName) : row.intrumentName != null) return false;
        if (date != null ? !date.equals(row.date) : row.date != null) return false;
        return price != null ? price.equals(row.price) : row.price == null;

    }

    @Override
    public int hashCode() {
        int result = intrumentName != null ? intrumentName.hashCode() : 0;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        return result;
    }
}
