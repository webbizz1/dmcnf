package webbizz.crm.util;

import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

public class RequestUtils {

    /**
     * 요청한 경로 (PATH) 반환 ('/' 부터 시작, QueryString 없음)
     *
     * @return 요청한 경로 (PATH)
     * @author TAEROK HWANG
     */
    public static String getPath() {
        return ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
    }

    /**
     * 요청한 경로 반환 ('/' 부터 시작, QueryString 있음)
     *
     * @return 요청한 경로
     * @author TAEROK HWANG
     */
    public static String getFullPath() {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        if (!(requestAttributes instanceof ServletRequestAttributes))
            return null;

        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        String path = request.getRequestURI();
        String queryString = request.getQueryString();

        return path + (StringUtils.hasText(queryString) ? "?" + queryString : "");
    }

    /**
     * 클라이언트 IP 주소 반환
     *
     * @return 클라이언트 IP 주소
     * @author TAEROK HWANG
     */
    public static String getRemoteAddr() {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        if (!(requestAttributes instanceof ServletRequestAttributes))
            return null;

        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        String clientIp = request.getHeader("IPV6_ADR");

        if (!StringUtils.hasText(clientIp) || "unknown".equalsIgnoreCase(clientIp))
            clientIp = request.getHeader("X-Forwarded-For");

        if (!StringUtils.hasText(clientIp) || "unknown".equalsIgnoreCase(clientIp))
            clientIp = request.getHeader("Proxy-Client-IP");

        if (!StringUtils.hasText(clientIp) || "unknown".equalsIgnoreCase(clientIp))
            clientIp = request.getHeader("WL-Proxy-Client-IP");

        if (!StringUtils.hasText(clientIp) || "unknown".equalsIgnoreCase(clientIp))
            clientIp = request.getHeader("HTTP_CLIENT_IP");

        if (!StringUtils.hasText(clientIp) || "unknown".equalsIgnoreCase(clientIp))
            clientIp = request.getHeader("HTTP_X_FORWARDED_FOR");

        if (!StringUtils.hasText(clientIp) || "unknown".equalsIgnoreCase(clientIp))
            clientIp = request.getHeader("HTTP_X_FORWARDED");

        if (!StringUtils.hasText(clientIp) || "unknown".equalsIgnoreCase(clientIp))
            clientIp = request.getHeader("HTTP_FORWARDED_FOR");

        if (!StringUtils.hasText(clientIp) || "unknown".equalsIgnoreCase(clientIp))
            clientIp = request.getHeader("HTTP_FORWARDED");

        if (!StringUtils.hasText(clientIp) || "unknown".equalsIgnoreCase(clientIp))
            clientIp = request.getHeader("HTTP_VIA");

        if (!StringUtils.hasText(clientIp) || "unknown".equalsIgnoreCase(clientIp))
            clientIp = request.getRemoteAddr();

        return clientIp;
    }

    /**
     * 로컬 IP와 회사 IP 목록
     */
    public static List<String> getLocalIpAddress() {
        return Arrays.asList(
                "127.0.0.1",        // IPv4
                "0:0:0:0:0:0:0:1",  // IPv6
                "::1",              // IPv6 축약형
                "121.167.147.150"   // webbizz
        );
    }

}
