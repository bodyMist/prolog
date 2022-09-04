package kit.prolog.service.social;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
@PropertySource("classpath:social/github.properties")
public class GithubAuthService {
    @Value("${github.client_id}")
    private String client_id; //프론트에서 호출한 uri와 동일하게
    @Value("${github.client_secret}")
    private String client_secret;
    @Value("${github.requestTokenUrl}")
    private String requestTokenUrl;
    @Value("${github.requestUserInfoUrl}")
    private String requestUserInfoUrl;
    public String getGithubAccessToken(String code)  {
        String accessToken = "";

        try{
            URL url = new URL(requestTokenUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.45 Safari/537.36");

            try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()))) {
                StringBuilder sb = new StringBuilder();
                sb.append("client_id=");
                sb.append(client_id);
                sb.append("&client_secret=");
                sb.append(client_secret);
                sb.append("&code=");
                sb.append(code);
                bw.write(sb.toString());
                bw.flush();
            }

            //결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            if(responseCode == 200){
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line = "";
                String result = "";
                while ((line = br.readLine()) != null) {
                    result += line;
                }
                System.out.println("response body : " + result);

                //Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
                JSONObject jObject = new JSONObject(result);
                accessToken = jObject.get("access_token").toString();
                System.out.println("access_token : " + accessToken);

                br.close();
                return accessToken;
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public String getGithubUserKey(String access_token) throws IOException {
        URL url = new URL(requestUserInfoUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setDoOutput(true);
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.45 Safari/537.36");
        conn.setRequestProperty("Authorization", "token " + access_token);

        int responseCode = conn.getResponseCode();
        if(responseCode == 200){
            //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);

            JSONObject jObject = new JSONObject(result);
            int socialKey = jObject.getInt("id");
            return String.valueOf(socialKey);
        }else{
            return null;
        }
    }
}
