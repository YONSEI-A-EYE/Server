package kr.co.aeye.apiserver.src.diary.utils;

public class GetSentimentLevel {

    public static float getSentimentLevel(float score, float magnitude){
        float sentiment = (float)Math.round(((score * magnitude + 1) * 2.5 * 100)/100.0);
        return sentiment;
    }
}
