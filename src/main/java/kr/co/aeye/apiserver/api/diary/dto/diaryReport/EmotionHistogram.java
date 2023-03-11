package kr.co.aeye.apiserver.api.diary.dto.diaryReport;

import lombok.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Builder
@Getter
@Data
public class EmotionHistogram {

    private int excited;
    private int happy;
    private int calm;
    private int content;
    private int anticipate;
    private int tense;
    private int angry;
    private int sad;
    private int badSurprised;
    private int goodSurprised;
    private int relaxed;
    private int bored;
    private int tired;

    public String getMostFrequentEmotion(){
        Map<String, Integer> emotionMap = new HashMap<>();
        emotionMap.put("excited", this.excited);
        emotionMap.put("happy", this.happy);
        emotionMap.put("calm", this.calm);
        emotionMap.put("content", this.content);
        emotionMap.put("anticipate", this.anticipate);
        emotionMap.put("tense", this.tense);
        emotionMap.put("angry", this.angry);
        emotionMap.put("sad", this.sad);
        emotionMap.put("badSurprised", this.badSurprised);
        emotionMap.put("goodSurprised", this.goodSurprised);
        emotionMap.put("relaxed", this.relaxed);
        emotionMap.put("bored", this.bored);
        emotionMap.put("tired", this.tired);

        String mostFrequentEmotion = Collections.max(emotionMap.entrySet(), Map.Entry.comparingByValue()).getKey();

        return mostFrequentEmotion;
    }

}
