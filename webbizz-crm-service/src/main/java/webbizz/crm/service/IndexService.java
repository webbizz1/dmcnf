package webbizz.crm.service;

import org.springframework.web.servlet.ModelAndView;
import webbizz.crm.domain.member.entity.MemberUserDetails;

public interface IndexService {

    /**
     * 인덱스 페이지 카운트 조회
     *
     * @param memberUserDetails 사용자 정보
     * @author TAEROK HWANG
     */
    ModelAndView getIndexCounts(MemberUserDetails memberUserDetails);

}
