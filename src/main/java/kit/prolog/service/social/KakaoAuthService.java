package kit.prolog.service.social;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.expression.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
@PropertySource("classpath:social/kakao.properties")
public class KakaoAuthService {
    @Value("${kakao.client_id}")
    private String client_id; //프론트에서 호출한 uri와 동일하게
    @Value("${kakao.requestTokenUrl}")
    private String requestTokenUrl;
    @Value("${kakao.requestUserInfoUrl}")
    private String requestUserInfoUrl;
    public String getKaKaoAccessToken(String code) {
        String accessToken = "";
        try {
            URL url = new URL(requestTokenUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //POST 요청을 위해 기본값이 false인 setDoOutput을 true로
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            //POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=");
            sb.append(client_id); // TODO REST_API_KEY 입력
            sb.append("&code=");
            sb.append(code);
            bw.write(sb.toString());
            bw.flush();

            //결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            if(responseCode == 200){
                //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                String line = "";
                String result = "";

                while ((line = br.readLine()) != null) {
                    result += line;
                }

                //Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
                JSONObject jObject = new JSONObject(result);
                accessToken = jObject.get("access_token").toString();

                br.close();
                bw.close();
                return accessToken;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getKakaoUserKey(String token) {
        String id = "";
        try {
            URL url = new URL(requestUserInfoUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Authorization", "Bearer " + token);
            urlConnection.setRequestMethod("GET");

            int responseCode = urlConnection.getResponseCode();
            if(responseCode == 200){
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line = "";
                String res = "";
                while((line=br.readLine())!=null){
                    res += line;
                }
                JSONObject jObject = new JSONObject(res);
                id = jObject.get("id").toString();
                br.close();
                return id;
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
