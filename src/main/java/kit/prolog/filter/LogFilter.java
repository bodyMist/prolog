package kit.prolog.filter;

import kit.prolog.util.ClientIpUtil;
import kit.prolog.util.ReadableRequestWrapper;
import lombok.extern.log4j.Log4j2;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;

@Log4j2
public class LogFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        ReadableRequestWrapper readableRequest = new ReadableRequestWrapper((HttpServletRequest) request);
        String requestIp = ClientIpUtil.getClientIp(readableRequest);
        log.info("요청 IP: " + requestIp);
        log.info("요청 URL: " + readableRequest.getRequestURI());

        String requestMethod = readableRequest.getMethod();
        if(Objects.equals(requestMethod, "POST")){
            String bodyType = readableRequest.getContentType();
            if(bodyType != null && !bodyType.contains("form-data")){
                String json = readBodyData(readableRequest);
                log.info("요청 정보: " + json);
            }
        }else if(Objects.equals(requestMethod, "GET") || Objects.equals(requestMethod, "DELETE")){
            log.info("요청 정보: " + readableRequest.getQueryString());
        }else if(Objects.equals(requestMethod, "PUT")){
            String json = readBodyData(readableRequest);
            log.info("요청 정보: " + json);
            log.info("요청 정보: " + readableRequest.getQueryString());
        }
        chain.doFilter(readableRequest,response);
    }
    private String readBodyData(HttpServletRequest request) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(request.getInputStream()));
        StringBuilder builder = new StringBuilder();
        String buffer;
        while ((buffer = input.readLine()) != null) {
            if (builder.length() > 0) {
                builder.append("\n");
            }
            builder.append(buffer);
        }
        return builder.toString();
    }
}
