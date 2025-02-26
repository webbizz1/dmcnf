package webbizz.crm.interceptor;

import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import webbizz.crm.domain.menu.dto.MenuDto;
import webbizz.crm.domain.menu.dto.MenuPageDto;
import webbizz.crm.service.menu.MenuService;
import webbizz.crm.util.RequestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class MenuInterceptor implements HandlerInterceptor {

    private static final Pattern PAGE_URL_PATTERN = Pattern.compile("^/pages/(\\d+)$");
    private static final Long KOREAN_GROUP_ID = 1L;
    private static final Long ENGLISH_GROUP_ID = 2L;

    private final MenuService menuService;

    @Override
    public void postHandle(final HttpServletRequest request,
                           final HttpServletResponse response,
                           final Object handler,
                           final ModelAndView modelAndView) {

        if (modelAndView == null || modelAndView.getModel().containsKey("menuPage")) return;

        final String requestUrl = RequestUtils.getFullPath();
        MenuDto menuDto = this.findMenuForCurrentLocale(requestUrl);

        final MenuPageDto menuPageDto = Optional.ofNullable(menuDto)
                .map(menu -> menuService.getMenuPage(menu.getId()))
                .orElseGet(MenuPageDto::new); // 디폴트 DTO 생성

        modelAndView.addObject("menuPage", menuPageDto);
    }

    /**
     * 지정된 그룹 ID에 따라 지역 변경
     * 한국 그룹인 경우 한국, 아닐시 영어
     *
     * @param request HTTP 요청 객체
     * @param groupId 그룹 ID (1: 한국, 2: 영어)
     */
    private void changeUpdateLocale(final HttpServletRequest request, final Long groupId) {
        final Locale newLocale = groupId.equals(KOREAN_GROUP_ID) ? Locale.KOREA : Locale.ENGLISH;
        LocaleContextHolder.setLocale(newLocale);
        request.getSession().setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, newLocale);
    }

    /**
     * 다른 로케일에 해당하는 메뉴를 조회, 필요한 경우 지역 강제 변경
     *
     * @param request    HTTP 요청 객체
     * @param requestUrl 요청 url
     * @return 다른 로케일에 해당하는 MenuDto, 없으면 null
     */
    private MenuDto findMenuForOtherLocale(final HttpServletRequest request, final String requestUrl) {
        final Long otherGroupId = this.getOtherGroupId();
        final MenuDto otherMenu = menuService.findByMenuUrlAndGroupId(requestUrl, otherGroupId);

        if (otherMenu != null) {
            changeUpdateLocale(request, otherGroupId);
        }

        return otherMenu;
    }

    /**
     * 현재 지역에 해당하는 그룹 아이디 반환
     *
     * @return 그룹 ID (한국: 1L, 영어: 2L)
     */
    private Long getCurrentGroupId() {
        return isCurrentLocaleKorean() ? KOREAN_GROUP_ID : ENGLISH_GROUP_ID;
    }

    /**
     * 다른 지역에 해당하는 그룹 아이디 반환
     *
     * @return 그룹 ID (한국: 1L, 영어: 2L)
     */
    private Long getOtherGroupId() {
        return isCurrentLocaleKorean() ? ENGLISH_GROUP_ID : KOREAN_GROUP_ID;
    }

    /**
     * 현재 지역이 한국인지 확인
     *
     * @return 그룹 ID (한국: true, 영어: false)
     */
    private boolean isCurrentLocaleKorean() {
        return LocaleContextHolder.getLocale().getLanguage().equals(Locale.KOREAN.getLanguage());
    }

    /**
     * 현재 지역에 해당하는 메뉴 조회
     *
     * @param requestUrl 요청 Url
     * @return 현재 로케일에 해당하는 MenuDto, 없으면 null
     */
    private MenuDto findMenuForCurrentLocale(final String requestUrl) {
        return menuService.findByMenuUrlAndGroupId(requestUrl, getCurrentGroupId());
    }

}
