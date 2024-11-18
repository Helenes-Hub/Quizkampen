import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class GameFlow extends Thread {

    Socket socket;

    public GameFlow(Socket socket){
        this.socket = socket;
    }

    public void run() {

        String questionText;
        String correctAnswer;
        List<String> options;

        try(PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()))){


            //JButton input på vilket tema som är valt
            String användarVal="ANIMALS";

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

        } catch (IOException e){
            e.printStackTrace();
        }
    }
}




