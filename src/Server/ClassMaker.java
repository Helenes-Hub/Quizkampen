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
                    Arrays.asList("Corvid", "Krokulidea", "Corpus", "Cronum"), "Corvid"),
            new QuestionClass("Vilket är världens snabbaste landdjur?",
                    Arrays.asList("Lejon", "Gepard", "Antilop", "Varg"), "Gepard"),
            new QuestionClass("Vad kallas en grupp med lejon?",
                    Arrays.asList("Flock", "Pride", "Koloni", "Klan"), "Pride"),
            new QuestionClass("Vilket är det största däggdjuret på jorden?",
                    Arrays.asList("Elefant", "Blåval", "Havskatt", "Flodhäst"), "Blåval"),
            new QuestionClass("Hur många ben har en spindel?",
                    Arrays.asList("6", "8", "10", "12"), "8"),
            new QuestionClass("Vilket djur har längst livslängd?",
                    Arrays.asList("Elefant", "Galapagossköldpadda", "Haj", "Papegoja"), "Galapagossköldpadda"),
            new QuestionClass("Vad är världens minsta fågelart?",
                    Arrays.asList("Kolibri", "Minibri", "Kanariefågel", "Sparv"), "Kolibri"),
            new QuestionClass("Vilket djur sover stående?",
                    Arrays.asList("Häst", "Ko", "Giraff", "Alla ovanstående"), "Alla ovanstående")
    )),
    SCIENCE(Arrays.asList(
            new QuestionClass("Vad heter den röda planeten?",
                    Arrays.asList("Venus", "Mars", "Rödis", "Saturnus"), "Mars"),
            new QuestionClass("Vad är det vetenskapliga namnet på vatten?",
                    Arrays.asList("H2O", "O2", "CO2", "Vattikus"), "H2O"),
            new QuestionClass("Viket kön börjar alla bebisar som i livmodern?",
                    Arrays.asList("Pojke", "Flicka", "Falskt påstående", "Hen"), "Flicka"),
            new QuestionClass("Vilket organ producerar insulin?",
                    Arrays.asList("Levern", "Bukspottkörteln", "Magsäcken", "Njuren"), "Bukspottkörteln"),
            new QuestionClass("Vad mäter en seismograf?",
                    Arrays.asList("Grafer", "Lufttryck", "Jordskalv", "Vindstyrka"), "Jordskalv"),
            new QuestionClass("Hur lång tid tar ljus på sig att färdas från solen till oss?",
                    Arrays.asList("8 minuter", "1 minut", "30 sekunder", "1 sekund"), "8 minuter"),
            new QuestionClass("Vilken djur är känd för att kunna ändra färg?",
                    Arrays.asList("Skameleont", "Kameleont", "Gecko", "Grodor"), "Kameleont"),
            new QuestionClass("Vilken planet är den varmaste i vårt solsystem?",
                    Arrays.asList("Merkurius", "Venus", "Mars", "Jorden"), "Venus"),
            new QuestionClass("Vad är huvudkomponenten i solens energi?",
                    Arrays.asList("Fusionsreaktioner", "Kärnklyvning", "Kemiska reaktioner", "Gravitationskraft"), "Fusionsreaktioner"),
            new QuestionClass("Vad är det latinska namnet för överarmsben?",
                    Arrays.asList("Femur", "Humerus", "armus överus", "Radius"), "Humerus")
    ));

    private final List<QuestionClass> questions;

    ClassMaker(List<QuestionClass> questions) {
        this.questions = questions;
    }

    public List<QuestionClass> getQuestions() {
        return questions;
    }
}
