package kr.co.aeye.apiserver.api.child.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.aeye.apiserver.api.child.dto.bard.PostAdviceBardReq;
import kr.co.aeye.apiserver.api.child.dto.bard.PostAdviceFromBardRes;
import kr.co.aeye.apiserver.api.child.utils.PostBardAPI;
import kr.co.aeye.apiserver.common.BaseException;
import kr.co.aeye.apiserver.common.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static kr.co.aeye.apiserver.common.BaseResponseStatus.SERVER_ERROR;

@Slf4j
@Service
@RequiredArgsConstructor
public class BardAdviceService {
    @Value("${bard.api-key}") String apiKey;

    public PostAdviceFromBardRes postAdviceToBard(PostAdviceBardReq postAdviceBardReq) throws BaseException, MalformedURLException, ProtocolException, ParseException, JsonProcessingException {
        PostBardAPI postBardAPI = new PostBardAPI(apiKey);
        PostAdviceFromBardRes postAdviceFromBardRes;
        try{
            String answerFromBard = postBardAPI.getResponse(postAdviceBardReq);
            String jsonFormAnswer = extractJsonFormInString(answerFromBard);
            jsonFormAnswer = jsonFormAnswer.replaceAll("\\\\n|\\\\", "");
            ObjectMapper mapper = new ObjectMapper();
            postAdviceFromBardRes = mapper.readValue(jsonFormAnswer, PostAdviceFromBardRes.class);
        } catch (Exception e){
            throw new BaseException(SERVER_ERROR);
        }

        return postAdviceFromBardRes;
    }

    public String extractJsonFormInString(String text) throws BaseException {
        String jsonPattern = "(?s)```json\\s*(.*?)\\s*```";
        Pattern pattern = Pattern.compile(jsonPattern);
        Matcher matcher = pattern.matcher(text);
        String jsonContent;

        if (matcher.find()) {
            jsonContent = matcher.group(1);
        } else {
            throw new BaseException(BaseResponseStatus.BAD_REQUEST);
        }
        return jsonContent;
    }
}