package webbizz.crm.aop;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import webbizz.crm.domain.member.entity.MemberUserDetails;
import webbizz.crm.domain.visitorlog.VisitorLogRepository;
import webbizz.crm.domain.visitorlog.dto.VisitorLogSaveDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Aspect
@RequiredArgsConstructor
public class ControllerAspect {

    private final VisitorLogRepository visitorLogRepository;

    @Value("${spring.application.name}")
    private String applicationName;

    /**
     * Controller 에서 발생하는 모든 GET 요청에 대한 로깅 <br />
     * GET 요청이 성공적으로 처리되었을 때만 로깅을 수행하며, 리다이렉트는 로깅하지 않음
     *
     * @param joinPoint AspectJ JoinPoint
     * @return Object AspectJ JoinPoint 의 결과
     * @throws Throwable 예외 발생 시 전파
     * @author TAEROK HWANG
     */
    @Around("@annotation(org.springframework.web.bind.annotation.GetMapping) && " +
            "execution(String *..*web..*Controller.*(..)) || " +
            "execution(org.springframework.web.servlet.ModelAndView *..*web..*Controller.*(..))")
    public Object doLogging(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();

        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        HttpServletResponse response = requestAttributes.getResponse();

        // 200 OK 이면서 리다이렉트가 아닌 경우에만 로그를 남김
        if (response == null || response.getStatus() != 200 || result.toString().startsWith("redirect:"))
            return result;

        Long memberId = -1L;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof MemberUserDetails) {
            memberId = ((MemberUserDetails) authentication.getPrincipal()).getMember().getId();
        }

        visitorLogRepository.insertOrUpdate(new VisitorLogSaveDto(
                this.applicationName,
                memberId,
                request.getRemoteAddr(),
                request.getRequestURI()
        ));

        return result;
    }

}
