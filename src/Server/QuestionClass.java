package Server;

import java.util.List;

//definerar varje fråga som en egen enhet

public class QuestionClass {

    private final String question;
    private final List<String> options;
    private final String correctAnswer;


    public QuestionClass(String question, List <String> options, String correctAnswer) {

        this.question = question;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getOptions() {
        return options;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }
}
