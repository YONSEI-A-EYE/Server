package kr.co.aeye.apiserver.api.diary;

import lombok.Getter;

@Getter
public enum EmotionResponse {

    EXCITED("You had a wonderful day!"),
    HAPPY("I’m glad you are happy!"),
    CALM("You look happy!"),
    CONTENT("You are doing good so far!"),
    ANTICIPATE("Something better is around the corner!"),
    TENSE("You are feeling tensed. Let’s relax a little bit."),
    ANGRY("It seems that you are angry. Let’s take a breather"),
    SAD("Don’t be sad. You are surrounded by love."),
    BAD_SURPRISED("This could be a blessing in disguise."),
    GOOD_SURPRISED("What a surprise!"),
    RELAXED("You look relaxed today!"),
    BORED("Being bored is more useful than being busy!"),
    TIRED("You look tired. Let’s take a break for a while.");

    private final String response;
    private EmotionResponse(String response){
        this.response = response;
    }

}
