package database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "command")
public class Command {

    @DatabaseField(generatedId = true, allowGeneratedIdInsert = true, canBeNull = false)
    private int     id;
    @DatabaseField(unique = true)
    private String  name;
    @DatabaseField
    private String  message;
    @DatabaseField
    private boolean isEnable;

    public Command() {

    }

    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(boolean isEnable) {
        this.isEnable = isEnable;
    }

}
