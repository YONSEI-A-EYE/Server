package kr.co.aeye.apiserver.api.child.service;

import kr.co.aeye.apiserver.api.child.dto.bard.PostAdviceBardReq;
import kr.co.aeye.apiserver.api.child.dto.bard.PostAdviceFromBardRes;
import kr.co.aeye.apiserver.common.BaseException;
import kr.co.aeye.apiserver.common.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class BardAdviceService {
    public void postAdviceToBard(PostAdviceBardReq postAdviceBardReq) throws BaseException, MalformedURLException, ProtocolException {
        String childName = postAdviceBardReq.getChildName();
        int childAge = postAdviceBardReq.getChildAge();
        String childTemperament = postAdviceBardReq.getChildTemperament();
        String content = postAdviceBardReq.getContent();

        String bardInput = String.format(
                "{\'input\': \'" +
                        "%s is %d years old. " +
                        "%s has %s. " +
                        "%s. " +
                        "What can I do? " +
                        "Give me 5 solutions. " +
                        "Give me title and solutions with object list in json format." +
                        "Solution object has index, title and content\'}",
                childName, childAge, childName, childTemperament, content);
        String authorizationHeader = "Bearer " + apiKey;
        try{
            URL bardChatUrl = new URL("https://api.bardapi.dev/chat");
            HttpURLConnection connection = (HttpURLConnection) bardChatUrl.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "text/plain");
            connection.setRequestProperty("Authorization", authorizationHeader);
            connection.setDoOutput(true);

            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(bardInput.getBytes("UTF-8"));
            outputStream.close();

            int responseCode = connection.getResponseCode();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
            }
            bufferedReader.close();

            // Print the response
            System.out.println("Response Code: " + responseCode);
            System.out.println("Response: " + response.toString());

            // Close the connection
            connection.disconnect();


        } catch (Exception e){
            throw new BaseException(BaseResponseStatus.BAD_REQUEST);
        }
    }
}
