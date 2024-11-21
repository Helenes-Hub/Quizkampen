public class Protocol {
    protected final int INITIAL = 0;
    protected final int CHOOSE_CATEGORY = 1;
    protected final int QUIZZING = 2;
    protected final int WAITING = 3;
    protected final int SHOW_SCORE_THIS_ROUND = 4;
    protected final int FINAL = 5;

    protected int state = INITIAL;

    //Denna metod behöver ordnas för fler och så rätt saker skickas
    public String getOutput(int state) {
        if (state == INITIAL) {
            state = CHOOSE_CATEGORY;
            return "Welcome to QuizZzkampen! Please enter a username: ";
        } else if (state == CHOOSE_CATEGORY) {
            state = QUIZZING;
            return "Choose a category";
        }else if (state == QUIZZING) {
            state = WAITING;
            return "FRÅGOR";
        } else if (state == WAITING) {
            state = SHOW_SCORE_THIS_ROUND;
            return "Waiting for your opponent. Here is your score this round: SCORE";
        } else if (state == SHOW_SCORE_THIS_ROUND) {
            state = FINAL;
            return "Score this round: SCORE";
        } else if (state == FINAL) {
            return "FINAL RESULT";
        }
        return "Unexpected error";
    }

}
