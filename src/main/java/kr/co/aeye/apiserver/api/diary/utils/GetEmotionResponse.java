package kr.co.aeye.apiserver.api.diary.utils;

import kr.co.aeye.apiserver.common.BaseException;
import kr.co.aeye.apiserver.common.BaseResponseStatus;
import kr.co.aeye.apiserver.api.diary.EmotionResponse;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class GetEmotionResponse {

    public static String getEmotionResponse(String inputEmotion) throws BaseException{
        String[] emotionSet = inputEmotion.split("-");
        String emotion = emotionSet[0];

        switch (emotion){
            case "excited":
                return EmotionResponse.EXCITED.getResponse();
            case "happy":
                return EmotionResponse.HAPPY.getResponse();
            case "calm":
                return EmotionResponse.CALM.getResponse();
            case "content":
                return EmotionResponse.CONTENT.getResponse();
            case "anticipate":
                return EmotionResponse.ANTICIPATE.getResponse();
            case "tense":
                return EmotionResponse.TENSE.getResponse();
            case "angry":
                return EmotionResponse.ANGRY.getResponse();
            case "sad":
                return EmotionResponse.SAD.getResponse();
            case "badSurprised":
                return EmotionResponse.BAD_SURPRISED.getResponse();
            case "goodSurprised":
                return EmotionResponse.GOOD_SURPRISED.getResponse();
            case "relaxed":
                return EmotionResponse.RELAXED.getResponse();
            case "bored":
                return EmotionResponse.BORED.getResponse();
            case "tired":
                return EmotionResponse.TIRED.getResponse();
            default:
                throw new BaseException(BaseResponseStatus.WRONG_EMOTION);
        }


    }

    public static String getThemeFromEmotion(String emotion) throws BaseException{
        switch (emotion){
            case "excited":
            case "happy":
            case "calm":
            case "content":
            case "anticipate":
            case "goodSurprised":
            case "relaxed":
                return "positive";

            case "tense":
            case "angry":
            case "sad":
            case "badSurprised":
            case "bored":
            case "tired":
                return "negative";
            default:
                throw new BaseException(BaseResponseStatus.WRONG_EMOTION);
        }
    }
}
