package database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;

@DatabaseTable(tableName = "question")
public class Question {
    private Logger logger = Logger.getLogger("streaManager");

    @DatabaseField(generatedId = true, allowGeneratedIdInsert = true, canBeNull = false)
    private int    id;
    @DatabaseField
    private String user;
    @DatabaseField
    private String content;
    @DatabaseField
    private String choices;
    @DatabaseField
    private long   beginDate;
    @DatabaseField
    private long   endDate;
    @DatabaseField
    private int    duration;
    @DatabaseField
    private boolean isEnable = false;

    public Question() {
        setBeginDate(new Date().getTime());
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ArrayList<String> getChoices() throws JSONException {
        JSONArray         choicesJSON    = new JSONArray(choices);
        ArrayList<String> choicesResults = new ArrayList<String>();

        for (int i = 0; i < choicesJSON.length(); i++) {
            String choice = choicesJSON.getString(i);
            choicesResults.add(choice);
        }

        return choicesResults;
    }

    public void setChoices(String choicesStr) throws JSONException{
        String[]          choices          = choicesStr.split("/");
        ArrayList<String> validatedChoices = new ArrayList<String>();

        for (Integer i = 0; i < choices.length; i++) {
            if (choices[i].length() != 0) {
                validatedChoices.add(choices[i]);
                logger.info("Choice added : " + choices[i]);
            }
        }
        JSONArray choicesJSON = new JSONArray(validatedChoices.toArray());

        this.choices = choicesJSON.toString();
    }

    public long getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(long beginDate) {
        this.beginDate = beginDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        setEndDate(new Date().getTime() + (60 * duration) * 1000);
        this.duration = duration;
    }

    public void setIsEnable(boolean isEnable) {
        this.isEnable = isEnable;
    }

    public boolean getIsEnable() {
        return isEnable;
    }
}
