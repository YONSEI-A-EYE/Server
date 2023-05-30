package kr.co.aeye.apiserver.api.child.utils;

import com.squareup.okhttp.*;
import kr.co.aeye.apiserver.api.child.dto.bard.PostAdviceBardReq;
import kr.co.aeye.apiserver.common.BaseException;
import kr.co.aeye.apiserver.common.BaseResponseStatus;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class PostBardAPI {
    @NonNull private String apiKey;
    private String url = "https://api.bardapi.dev/chat";

    public String setPostBody(PostAdviceBardReq postAdviceBardReq){
        String childName = postAdviceBardReq.getChildName();
        int childAge = postAdviceBardReq.getChildAge();
        String childTemperament = postAdviceBardReq.getChildTemperament();
        String content = postAdviceBardReq.getContent();

        String postBody = String.format(
                "{\"input\": \" %s is %d years old. "  +
                        "%s has %s. " +
                        "%s. " +
                        "What can I do? " +
                        "Show me 5 solutions and each solution has index, title and content. " +
                        "Response should be only json format which consist of solution object list.\"}",
                childName, childAge, childName, childTemperament, content);

        return postBody;
    }

    public String getResponse (PostAdviceBardReq postAdviceBardReq) throws BaseException {
        String result = null;
        Response response = null;
        ResponseBody body = null;
        try {
            String postBody = this.setPostBody(postAdviceBardReq);

            // OkHttp 객체 생성
            OkHttpClient client = new OkHttpClient();

            // RequestBody 생성
            RequestBody requestBody = RequestBody.create(
                    MediaType.parse("text/plain"), postBody);

            // Post 객체 생성
            Request request = new Request.Builder().url(url)
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .addHeader("Content-Type", "text/plain")
                    .post(requestBody)
                    .build();

            // 요청 전송
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                // 응답 Body
                body = response.body();
                if (body != null) {
                    result = body.string();
                    body.close();
                }
            } else {
                throw new BaseException(BaseResponseStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.BAD_REQUEST);
        } finally {
            if (result == null) {
                throw new BaseException(BaseResponseStatus.BAD_REQUEST);
            }

            return result;
        }
    }
}
