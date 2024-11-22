package Server;

import java.util.Arrays;
import java.util.List;

public enum ClassMaker {

    ANIMALS(Arrays.asList(
            new QuestionClass("Vad säger räven?",
                    Arrays.asList("Voff Voff", "Awoo", "Mjau", "Ring ding ding"), "Ring ding ding"),
            new QuestionClass("Vilket djur är skogens konung?",
                    Arrays.asList("Farfar", "Lodjuret", "Älgen", "Björnen"), "Älgen"),
            new QuestionClass("Vad kallas kråk-familjen på latin?",
                    Arrays.asList("Corvid", "Krokulidea", "Corpus", "Cronum"), "Corvid")
    )),
    SCIENCE(Arrays.asList(
            new QuestionClass("Vad heter den röda planeten?",
                    Arrays.asList("Venus", "Mars", "Rödis", "Saturnus"), "Mars"),
            new QuestionClass("Vad är det vetenskapliga namnet på vatten?",
                    Arrays.asList("H2O", "O2", "CO2", "Vattikus"), "H2O"),
            new QuestionClass("Viket kön börjar alla bebisar som i livmodern?",
                    Arrays.asList("Pojke", "Flicka", "Falskt påstående", "Hen"), "Flicka")
    ));

    private final List<QuestionClass> questions;

    ClassMaker(List<QuestionClass> questions) {
        this.questions = questions;
    }

    public List<QuestionClass> getQuestions() {
        return questions;
    }
}
