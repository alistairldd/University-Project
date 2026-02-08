package Model;

public class Score {
    /* notre classe score : elle contient un attribut
    score qui est un entier représentant le score du joueur,
     et des méthodes pour l'incrémenter, le réinitialiser et le récupérer*/
    private static int score;

    public Score() {
        score = 0;
    }

    public static int getScore() {
        return score;
    }

    public static void incrementerScore() {score += 1;}

    public static void reset() {score = 0;}
}
