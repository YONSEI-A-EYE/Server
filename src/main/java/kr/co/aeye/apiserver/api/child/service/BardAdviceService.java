package kr.co.aeye.apiserver.api.child.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.aeye.apiserver.api.child.dto.bard.PostAdviceBardReq;
import kr.co.aeye.apiserver.api.child.dto.bard.SolutionObjectDto;
import kr.co.aeye.apiserver.api.child.utils.PostBardAPI;
import kr.co.aeye.apiserver.common.BaseException;
import kr.co.aeye.apiserver.common.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static kr.co.aeye.apiserver.common.BaseResponseStatus.SERVER_ERROR;

@Slf4j
@Service
@RequiredArgsConstructor
public class BardAdviceService {
//    I have used the Chat GPT API as the Bad API has been discontinued as of May 31st.
//    @Value("${bard.api-key}") String apiKey;
    @Value("${chatGpt.api-key}") String apiKey;

    public List<SolutionObjectDto> postAdviceToBard(PostAdviceBardReq postAdviceBardReq) throws BaseException, MalformedURLException, ProtocolException, ParseException, JsonProcessingException {
        PostBardAPI postBardAPI = new PostBardAPI(apiKey);
        List<SolutionObjectDto> solutionList;
        try{
            String answerFromBard = postBardAPI.getResponse(postAdviceBardReq);
            String jsonFormAnswer = extractJsonFormInString(answerFromBard);
            jsonFormAnswer = jsonFormAnswer.replaceAll("\\\\n|\\\\", "");
            ObjectMapper mapper = new ObjectMapper();
            solutionList = Arrays.asList(mapper.readValue(jsonFormAnswer, SolutionObjectDto[].class));
        } catch (Exception e){
            log.info("error in bard api={}", e.getMessage());
            throw new BaseException(SERVER_ERROR);
        }

        return solutionList;
    }

    public String extractJsonFormInString(String text) throws BaseException, ParseException {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(text);

        JSONArray choices = (JSONArray) jsonObject.get("choices");
        JSONObject choice = (JSONObject) choices.get(0);
        String result = String.valueOf(choice.get("text"));

        String jsonPattern = "(?s)```json\\s*(.*?)\\s*```";
        Pattern pattern = Pattern.compile(jsonPattern);
        Matcher matcher = pattern.matcher(result);
        String jsonContent;

        if (matcher.find()) {
            jsonContent = matcher.group(1);
        } else {
            throw new BaseException(BaseResponseStatus.BAD_REQUEST);
        }
        return jsonContent;
    }
}