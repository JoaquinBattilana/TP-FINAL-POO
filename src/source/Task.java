package source;


import java.io.Serializable;

public class Task implements Comparable<Task>,Serializable {

    private int id;
    private String description;
    private StringDate expirationDate;
    private Boolean complete;

    public Task(int id, String description) {
        complete=false;
        this.id = id;
        this.description = description;
        this.expirationDate = null;
    }

    public Task(int id, String description, StringDate expiration_date) {
        this(id,description);
        this.expirationDate = expiration_date;
    }

    public void complete() {
        this.complete = true;
    }
    public boolean completed(){
        return complete;
    }

    @Override
    public int compareTo(Task o) {
        int comp1=complete.compareTo(o.complete);
        if(comp1!=0)
            return comp1;
        if(expirationDate!=null && o.expirationDate!=null) {
            int comp2 = expirationDate.compareTo(o.expirationDate);
            if(comp2!=0)
                return comp2;
        }
        else if(expirationDate==null && o.expirationDate!=null)
            return -1;
        else if(o.expirationDate==null && expirationDate!=null)
            return 1;
        return id-o.id;
    }

    @Override
    public boolean equals(Object obj) {
        if(this==obj)
            return true;
        if(!(obj instanceof Task))
            return false;
        Task temp=(Task) obj;
        if(id==temp.id)
            return true;
        return false;
    }

    @Override
    public int hashCode(){
        int result=17;
        result=31*result+this.id;
        return result;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }


    public StringDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(StringDate date){
        expirationDate=date;
    }
    public void setDescription(String description){
        this.description=description;
    }

}