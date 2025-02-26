package webbizz.crm.service.menu;

import org.springframework.web.servlet.ModelAndView;
import webbizz.crm.domain.boardarticle.dto.BoardArticleCondition;
import webbizz.crm.domain.member.entity.MemberUserDetails;
import webbizz.crm.domain.menu.dto.MenuPageDto;
import webbizz.crm.domain.menu.enumset.MenuMode;

public interface MenuRenderService {

    ModelAndView renderBoardPage(MenuPageDto menuPageDto,
                                 MenuMode menuMode,
                                 BoardArticleCondition condition,
                                 MemberUserDetails memberUserDetails);

}
