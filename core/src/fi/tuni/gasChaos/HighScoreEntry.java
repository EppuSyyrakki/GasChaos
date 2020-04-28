package fi.tuni.gasChaos;


public class HighScoreEntry {
    private String name;
    private int score;

    // Needed for json parsing, same reason as with Cow.java and Field.java.
    public HighScoreEntry() {
    }

    public HighScoreEntry(String name, int score) {
        this.name = name;
        this.score = score;
    }
    // Getters and setters ...

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
