package database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "event")
public class Event {

    @DatabaseField(generatedId = true, allowGeneratedIdInsert = true, canBeNull = false)
    private int    id;
    @DatabaseField
    private String type;
    @DatabaseField(foreign = true)
    private User   user;
    @DatabaseField
    private String message;
    @DatabaseField
    private long   date;


    public Event() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }


    public String toString() {
        return "Id: " + getId() + ", Type: " + getType() + ", Username: " + getUser().getId() + ", Message: " + getMessage() + ", Date: " + getDate();
    }
}
