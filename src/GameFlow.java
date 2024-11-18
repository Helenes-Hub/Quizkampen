import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class GameFlow extends Thread {

    private Player player1;
    private Player player2;
    private Player currentPlayer;

    public GameFlow(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;

        currentPlayer = player1;

        this.player1.setOpponent(player2);
        this.player2.setOpponent(player1);

    }

    public void run() {

        String questionText;
        String correctAnswer;
        List<String> options;


        //JButton input på vilket tema som är valt
        String användarVal = "ANIMALS";

        ClassMaker selectedCategory = ClassMaker.valueOf(användarVal);
        List<QuestionClass> questions = selectedCategory.getQuestions();
        System.out.println(questions.get(0).getCorrectAnswer());

        for (QuestionClass question : questions) {
            options = question.getOptions();
            questionText = question.getQuestion();
            correctAnswer = question.getCorrectAnswer();
            //for (int i = 0; i < options.size(); i++) {
            //    System.out.println((i + 1) + ". " + options.get(i));
            //}

            String userAnswer = "JButton text.valueOf";

            //System.out.println(questionText);
            //System.out.println(options);
            //System.out.println(correctAnswer);

            if (userAnswer.equals(question.getCorrectAnswer())) {
                System.out.println("Yasss poäng!");
            } else {
                System.out.println("Fel, inga poäng ");
            }
        }

    }
}




