
public class MyDate {
    private int day;
    private int month;
    private int year;

    public MyDate(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    @Override
    public String toString() {
        return day + "/" + month + "/" + year;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof MyDate)) return false;
        MyDate other = (MyDate) obj;
        return this.day == other.day
                && this.month == other.month
                && this.year == other.year;
    }

    @Override
    public int hashCode() {
        int result = Integer.hashCode(day);
        result = 31 * result + Integer.hashCode(month);
        result = 31 * result + Integer.hashCode(year);
        return result;
    }
}
