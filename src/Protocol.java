/*public class Protocol {
    protected static final int INITIAL = 0;
    protected static final int ENTER_USERNAME = 1;
    protected static final int CHOOSE_CATEGORY = 2;
    protected static final int QUIZZING = 3;
    protected static final int WAITING = 4;
    protected static final int SHOW_SCORE_THIS_ROUND = 5;
    protected static final int FINAL = 6;

    protected int state = INITIAL;

    //Denna metod behöver ordnas för fler och så rätt saker skickas
    public int getOutput(int state) {
        if (state == INITIAL) {
            state = ENTER_USERNAME;
            return 1;
        } else if (state == ENTER_USERNAME) {
            state = CHOOSE_CATEGORY;
            return 2;
        } else if (state == CHOOSE_CATEGORY) {
            state = QUIZZING;
            return 3;
        } else if (state == QUIZZING) {
            state = WAITING;
            return 4;
        } else if (state == WAITING) {
            state = SHOW_SCORE_THIS_ROUND;
            return 5;
        } else if (state == SHOW_SCORE_THIS_ROUND) {
            state = FINAL;
            return 6;

        }
        return -1;
    }
}

 */
