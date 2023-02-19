package kr.co.aeye.apiserver.src.diary.utils;

import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;
import java.io.IOException;

public class GoogleEmotion {

    public static Sentiment getEmotionFromGoogleCloud (String content) throws RuntimeException{
        /**
         * get sentiment(score, magnitude) from google cloud api
         */
        // Instantiates a client
        LanguageServiceClient language;
        try {
            language = LanguageServiceClient.create();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // The text to analyze
        Document doc = Document.newBuilder().setContent(content).setType(Document.Type.PLAIN_TEXT).build();

        // Detects the sentiment of the text
        return language.analyzeSentiment(doc).getDocumentSentiment();
    }

    // temp emotion -> score, magnitude
    public static String getTempEmotionFromScoreMagnitude(float score, float magnitude) {
        /**
         * convert score, magnitude to emotion
         */
        if (score > 0.5) {
            if (magnitude >= 0.75) {
                return "excited";
            } else if (magnitude > 0.5) {
                return "happy";
            } else if (magnitude > 0.25) {
                return "content";
            } else {
                return "relaxed";
            }
        } else if (score > 0.1) {
            if (magnitude > 0.25) {
                return "goodSurprised";
            } else {
                return "calm";
            }
        } else if (score >= -0.1) {
            return "anticipate";
        } else if (score >= -0.5) {
            if (magnitude > 0.25) {
                return "badSurprised";
            } else {
                return "tired";
            }
        } else {
            if (magnitude >= 0.75) {
                return "tense";
            } else if (magnitude > 0.5) {
                return "angry";
            } else if (magnitude > 0.25) {
                return "sad";
            } else {
                return "bored";
            }
        }
    }
}
