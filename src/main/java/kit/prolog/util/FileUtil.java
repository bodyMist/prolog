package kit.prolog.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
@Log4j2
@RequiredArgsConstructor
public class FileUtil {
    private BufferedReader br;
    private FileReader fr;
    private BufferedWriter bw;
    private FileWriter fw;

    public List<String> fileRead(String path) {
        List<String> newLines = new ArrayList<>();

        try {
            File file = new ClassPathResource(path).getFile();
            this.fr = new FileReader(file);
            this.br = new BufferedReader(this.fr);
            String newLine = "";

            while((newLine = this.br.readLine()) != null) {
                newLines.add(newLine);
            }
            br.close();
            fr.close();
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
            File file = new ClassPathResource(path).getFile();
            fw = new FileWriter(file);
            bw = new BufferedWriter(this.fw);
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
            br.close();
            fr.close();
        } catch (IOException e) {
            log.info("파일 경로 에러");
        }
    }
}
