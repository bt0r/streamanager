package database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "vote")
public class Vote {

    @DatabaseField(generatedId = true, allowGeneratedIdInsert = true, canBeNull = false)
    private int    id;
    @DatabaseField
    private String user;
    @DatabaseField
    private String value;
    @DatabaseField
    private int    question;

    public Vote() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String name) {
        this.user = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getQuestion() {
        return question;
    }

    public void setQuestion(int id) {
        this.question = id;
    }

}
