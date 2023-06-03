package kr.co.aeye.apiserver.api.child.utils;

import com.squareup.okhttp.*;
import kr.co.aeye.apiserver.api.child.dto.bard.PostAdviceBardReq;
import kr.co.aeye.apiserver.common.BaseException;
import kr.co.aeye.apiserver.common.BaseResponseStatus;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;

import java.util.concurrent.TimeUnit;


@RequiredArgsConstructor
public class PostBardAPI {
    /**
     *   I wanted to use the Bard API, but it was not available, so I used OpenAI.
     */
    @NonNull private String apiKey;
//    private String url = "https://api.bardapi.dev/chat";
    private String url = "https://api.openai.com/v1/completions";

    public String setPostBody(PostAdviceBardReq postAdviceBardReq){
        String childName = postAdviceBardReq.getChildName();
        int childAge = postAdviceBardReq.getChildAge();
        String childTemperament = postAdviceBardReq.getChildTemperament();
        String content = postAdviceBardReq.getContent();

        String text = String.format(
                "%s is %d years old. %s has %s. %s. " +
                        "What can I do? " +
                        "Show me 5 solutions and each solution has index, title and content. " +
                        "Response should be only json format which consist of solution object list." +
                        "Wrap the JSON with ```json and ```",
                childName, childAge, childName, childTemperament, content);

        JSONObject postData = new JSONObject();
        postData.put("model", "text-davinci-003");
        postData.put("prompt", text);
        postData.put("temperature", 1.0);
        postData.put("max_tokens", 2048);

        String postBody = postData.toString();
        return postBody;
    }

    public String getResponse (PostAdviceBardReq postAdviceBardReq) throws BaseException {
        String result;
        Response response;
        ResponseBody body;
        try {
            String postBody = this.setPostBody(postAdviceBardReq);

            OkHttpClient client = new OkHttpClient();
            client.setConnectTimeout(15, TimeUnit.SECONDS);
            client.setReadTimeout(30, TimeUnit.SECONDS);

            RequestBody requestBody = RequestBody.create(
                    MediaType.parse("application/json"), postBody);

            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .addHeader("Content-Type", "application/json")
                    .post(requestBody)
                    .build();

            response = client.newCall(request).execute();
            body = response.body();
            result = body.string();
            body.close();

            if (!response.isSuccessful() || result == null) {
                throw new BaseException(BaseResponseStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.BAD_REQUEST);
        }
        return result;
    }
}
