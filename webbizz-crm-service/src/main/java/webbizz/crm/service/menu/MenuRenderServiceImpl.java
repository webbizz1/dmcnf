package webbizz.crm.service.menu;

import lombok.RequiredArgsConstructor;
import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import webbizz.crm.domain.YN;
import webbizz.crm.domain.board.dto.BoardDto;
import webbizz.crm.domain.board.dto.InnerBoardDto;
import webbizz.crm.domain.board.enumset.BoardConfigKey;
import webbizz.crm.domain.boardarticle.dto.BoardArticleCondition;
import webbizz.crm.domain.boardarticle.dto.BoardArticleDto;
import webbizz.crm.domain.member.entity.MemberUserDetails;
import webbizz.crm.domain.menu.dto.MenuPageDto;
import webbizz.crm.domain.menu.enumset.MenuMode;
import webbizz.crm.exception.BusinessException;
import webbizz.crm.service.board.BoardArticleService;
import webbizz.crm.service.board.BoardService;

@Service("menuRenderService")
@RequiredArgsConstructor
public class MenuRenderServiceImpl extends EgovAbstractServiceImpl implements MenuRenderService {

    private final MenuService menuService;
    private final BoardService boardService;
    private final BoardArticleService boardArticleService;

    @Override
    public ModelAndView renderBoardPage(final MenuPageDto menuPageDto,
                                        final MenuMode menuMode,
                                        final BoardArticleCondition condition,
                                        final MemberUserDetails memberUserDetails) {

        // Controller 에서 사용할 Model 객체
        ModelAndView modelAndView = new ModelAndView();

        InnerBoardDto innerBoardDto = menuPageDto.getMenu().getBoard();
        if (innerBoardDto == null)
            throw new BusinessException(404, "페이지를 찾을 수 없습니다.");

        // 게시판 기본 조건 적용
        condition.setBoardId(innerBoardDto.getId());
        condition.setViewYn(YN.Y);

        // 게시판 기본 정보
        BoardDto boardDto = boardService.searchById(innerBoardDto.getId());
        modelAndView.addObject("board", boardDto);

        switch (menuMode) {
            case LIST: {
                // 게시판 목록 권한 체크
                if (!boardDto.hasPermission(BoardConfigKey.GRANT_ARTICLE_LIST, memberUserDetails)) {
                    throw new BusinessException(403, "게시물 목록 권한이 없습니다.");
                }

                Page<BoardArticleDto> pageVars = boardArticleService.searchAllByCondition(condition);
                modelAndView.addObject("pageVars", pageVars);

                modelAndView.addObject("condition", condition);
                modelAndView.setViewName("pages/type_board_list");
                break;
            }

            case READ: {
                // 게시판 상세 권한 체크
                if (!boardDto.hasPermission(BoardConfigKey.GRANT_ARTICLE_READ, memberUserDetails)) {
                    throw new BusinessException(403, "게시물 읽기 권한이 없습니다.");
                }

                BoardArticleDto article =
                        boardArticleService.searchByWithReadCount(condition.getBoardId(), condition.getId());
                modelAndView.addObject("article", article);

                modelAndView.setViewName("pages/type_board_detail");
                break;
            }

            case WRITE: {
                // 게시판 글쓰기 권한 체크
                if (!boardDto.hasPermission(BoardConfigKey.GRANT_ARTICLE_WRITE, memberUserDetails)) {
                    throw new BusinessException(403, "글 쓰기 권한이 없습니다.");
                }

                modelAndView.setViewName("pages/type_board_write");
                break;
            }
        }

        return modelAndView;
    }

}
