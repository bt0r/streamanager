package database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "battle")
public class Battle {

    @DatabaseField(generatedId = true, allowGeneratedIdInsert = true, canBeNull = false)
    private int    id;
    @DatabaseField
    private String name;
    @DatabaseField(foreign = true)
    private User   player1;
    @DatabaseField(foreign = true, canBeNull = true)
    private User   player2;
    @DatabaseField(foreign = true, canBeNull = true)
    private User   winner;
    @DatabaseField
    private boolean   isVersusBot;
    @DatabaseField
    private long   earnedPoints;
    @DatabaseField
    private long   date;


    public Battle() {
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

    public User getPlayer1() {
        return player1;
    }

    public void setPlayer1(User player1) {
        this.player1 = player1;
    }

    public User getPlayer2() { return player2; }

    public void setPlayer2(User player2) { this.player2 = player2; }

    public User getWinner() { return winner; }

    public void setWinner(User winner) { this.winner = winner; }

    public long getEarnedPoints() {
        return earnedPoints;
    }

    public void setEarnedPoints(long earnedPoints) {
        this.earnedPoints = earnedPoints;
    }

    public boolean isVersusBot() {
        return isVersusBot;
    }

    public void setIsVersusBot(boolean isVersusBot) {
        this.isVersusBot = isVersusBot;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

}
