package kit.prolog.service.social;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
public class GithubAuthService {
    public String getGithubAccessToken(String code)  {
        String accessToken = "";

        try{
            URL url = new URL("https://github.com/login/oauth/access_token");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.45 Safari/537.36");

            // 이 부분에 client_id, client_secret, code를 넣어주자.
            // 여기서 사용한 secret 값은 사용 후 바로 삭제하였다.
            // 실제 서비스나 깃허브에 올릴 때 이 부분은 항상 주의하자.

            // client_secret : 729505c3586bcee7fb0d47b9182eb7060123e5ce
            try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()))) {
                bw.write("client_id=0006efe23ef0c6ecb6c0&client_secret=729505c3586bcee7fb0d47b9182eb7060123e5ce&code=" + code);
                bw.flush();
            }

            //결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

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
        }catch (IOException e){
            e.printStackTrace();
        }

        return accessToken;
    }

    public void createGithubUser(String access_token) throws IOException {
        URL url = new URL("https://api.github.com/user");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setDoOutput(true);
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.45 Safari/537.36");
        conn.setRequestProperty("Authorization", "token " + access_token);

        int responseCode = conn.getResponseCode();
        System.out.println("responseCode : " + responseCode);

        //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line = "";
        String result = "";

        while ((line = br.readLine()) != null) {
            result += line;
        }
        System.out.println("response body : " + result);
    }

    private String getResponse(HttpURLConnection conn, int responseCode) throws IOException {
        StringBuilder sb = new StringBuilder();
        if (responseCode == 200) {
            try (InputStream is = conn.getInputStream();
                 BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                for (String line = br.readLine(); line != null; line = br.readLine()) {
                    sb.append(line);
                }
            }
        }
        return sb.toString();
    }

    public void createKakaoUser(String token) {

    }
}
