package Model;

public class Score {
    private static int score;

    public Score() {
        this.score = 0;
    }

    public static int getScore() {
        return score;
    }

    public static void incrementerScore() {
        score += 1;
    }
}
