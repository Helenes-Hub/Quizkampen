import java.io.FileInputStream;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class GameFlow {

    public GameFlow() {
        int timer=0;
        int rounds=0;
        int questionsPerRound=0;


//-----properties load and set start
        Properties p = new Properties();
        try{
            p.load(new FileInputStream("src/Settings.properties"));
        }

        catch (Exception e) {
            System.out.println("filen hittades inte");
        }

        timer= Integer.parseInt(p.getProperty("timer", "10"));
        rounds = Integer.parseInt(p.getProperty("rounds", "4"));
        questionsPerRound = Integer.parseInt(p.getProperty("questionsPerRound", "4"));

        //----properties load and set end

        String questionText;
        String correctAnswer;
        List<String> options;

        //JButton input på vilket tema som är valt
        String användarVal="ANIMALS";

        //hämtar in listan med alla frågor i temat, shufflar
        List<QuestionClass> allThemedQuestions= ClassMaker.valueOf(användarVal).getQuestions();
        Collections.shuffle(allThemedQuestions);

        //hämtar ut de första shufflade frågorna med antal questionsPerRound
        List<QuestionClass> questions=allThemedQuestions.subList(0, questionsPerRound);


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

    public static void main(String[] args) {
        new GameFlow();
    }
}




