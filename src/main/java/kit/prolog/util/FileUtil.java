package kit.prolog.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
@Log4j2
@RequiredArgsConstructor
public class FileUtil {

    public List<String> fileRead(String path) {
        List<String> newLines = new ArrayList<>();

        try {
            ClassPathResource resource = new ClassPathResource(path);
            InputStream inputStream = new BufferedInputStream(resource.getInputStream());
            newLines = new BufferedReader(
                    new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                    .lines().collect(Collectors.toList());

            inputStream.close();
            return newLines;
        } catch (FileNotFoundException e) {
            log.info("파일 경로 에러");
        } catch (IOException e) {
            log.info("파일 읽기 에러");
        }



        return newLines;
    }

    public void fileWrite(List<String> lines, String path) {
        try {
            ClassPathResource resource = new ClassPathResource(path);
            OutputStream outputStream = new FileOutputStream(resource.getFile());

            for (String line : lines) {
                line += "\r\n";
                outputStream.write(line.getBytes(StandardCharsets.UTF_8));
            }
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            log.info("파일 경로 에러");
        }
    }
}
