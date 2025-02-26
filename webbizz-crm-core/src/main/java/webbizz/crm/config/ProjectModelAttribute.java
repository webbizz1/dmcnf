package webbizz.crm.config;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;

/**
 * ModelAttribute 설정을 위한 Config <br />
 * Thymeleaf 3.1 버전 부터 #request 관련 객체를 모두 사용할 수 없음 <br />
 * Thymeleaf 템플릿 페이지에서 사용할 수 있도록 ModelAttribute 로 등록 <br />
 * Repository 관련 코드를 이 클래스에 넣지 말 것 (중요 - 리소스 및 fragment 마다 요청이 일어남) <br />
 * {@link ControllerAdvice} annotation 필요
 *
 * @author TAEROK HWANG
 */
@ControllerAdvice
public class ProjectModelAttribute {

    /**
     * 현재 페이지의 정보를 담고 있는 ServletUriComponentsBuilder 객체 반환
     *
     * @return ServletUriComponentsBuilder 객체
     * @author TAEROK HWANG
     */
    @ModelAttribute("uriBuilder")
    public ServletUriComponentsBuilder getUriBuilder() {
        return ServletUriComponentsBuilder.fromCurrentRequest();
    }

    /**
     * 현재 페이지의 URI 반환 (QueryString 없음)
     *
     * @param request HttpServletRequest
     * @return 요청한 URI
     * @author TAEROK HWANG
     */
    @ModelAttribute("requestURI")
    public String getRequestURI(final HttpServletRequest request) {
        return request.getRequestURI();
    }

    @ModelAttribute("requestFullURI")
    public String getRequestFullURI(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String queryString = request.getQueryString();

        return requestURI + (queryString == null ? "" : "?" + queryString);
    }

    /**
     * 현재 페이지의 QueryString 반환
     *
     * @param request HttpServletRequest
     * @return 요청 파라미터
     * @author TAEROK HWANG
     */
    @ModelAttribute("queryString")
    public String getQueryString(final HttpServletRequest request) {
        return request.getQueryString();
    }

    /**
     * 현재 페이지의 QueryString 에 담겨있는 값 반환 (q=XXX)
     *
     * @param request HttpServletRequest
     * @return 요청 QueryString 값
     * @author TAEROK HWANG
     */
    @ModelAttribute("queryStringValue")
    public String getQueryStringValue(final HttpServletRequest request) {
        String queryString = request.getParameter("q");

        if (!StringUtils.hasText(queryString))
            return "";

        return "?" + queryString;
    }

    /**
     * 현재 페이지의 referer (이전 페이지) 반환
     *
     * @param request HttpServletRequest
     * @return 요청 referer 값 (도메인이 일치하지 않거나 로그인 페이지라면 null 반환)
     * @author TAEROK HWANG
     */
    @ModelAttribute("referer")
    public String getReferer(final HttpServletRequest request) {
        String referer = request.getHeader("referer");

        // 로그인 관련 페이지에서 이동한 경우 referer 반환하지 않음
        if (referer != null && referer.contains("/login"))
            return null;

        // 도메인이 일치할 경우에만 referer 반환
        if (referer != null && referer.startsWith(request.getScheme() + "://" + request.getServerName()))
            return referer;

        return null;
    }

    /**
     * 애플리케이션 진입 메인 클래스 반환
     *
     * @return 패키지를 포함한 메인 클래스 이름
     * @author TAEROK HWANG
     */
    @ModelAttribute("mainClass")
    public String getMainClass() {
        return System.getProperty("sun.java.command");
    }

}
