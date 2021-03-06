package source;


import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class StringDate implements Comparable<StringDate>,Serializable {
    private static final DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/uuuu");
    private LocalDate date;

    public StringDate(LocalDate date){
        this.date=date;
    }
    public StringDate(String date){
        this.date=LocalDate.parse(date,format);
    }
    @Override
    public String toString(){
        if(date.equals(LocalDate.now()))
            return "today";
        else if(date.equals(LocalDate.now().plusDays(1)))
            return "tomorrow";
        else if(date.equals(LocalDate.now().minusDays(1)))
            return "yesterday";
        else
            return date.format(format);
    }

    @Override
    public int compareTo(StringDate o) {
        return date.compareTo(o.date);
    }

    @Override
    public boolean equals(Object obj) {
        if(this==obj)
            return true;
        if(!(obj instanceof StringDate))
            return false;
        StringDate temp=(StringDate) obj;
        return date.equals(temp.date);
    }
}