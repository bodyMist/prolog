package kit.prolog.util;

import org.springframework.lang.Nullable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class ClientIpUtil {
    private static final String[] IP_HEADER_CANDIDATES = {
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"
    };
    public static String getClientIp(HttpServletRequest request) {
        String ip = getIpXFF(request);

        for (String ipHeader: List.of(IP_HEADER_CANDIDATES)) {
            ip = request.getHeader(ipHeader);
            if (!notValidIp(ip)) return ip;
        }

        return ip != null ? ip : request.getRemoteAddr();
    }

    private static boolean notValidIp(String ip) {
        return ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip);
    }

    @Nullable
    private static String getIpXFF(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");

        if (isMultipleIpXFF(ip)) {
            ip = getClientIpWhenMultipleIpXFF(ip);
        }

        return ip;
    }

    private static boolean isMultipleIpXFF(String ip) {
         return ip != null && ip.contains(",");
    }

    private static String getClientIpWhenMultipleIpXFF(String ipList) {
         return ipList != null ? ipList.split(",")[0] : "";
    }
}
