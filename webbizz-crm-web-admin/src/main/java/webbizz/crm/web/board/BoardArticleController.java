package webbizz.crm.web.board;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import webbizz.crm.domain.board.dto.BoardDto;
import webbizz.crm.domain.boardarticle.dto.BoardArticleCondition;
import webbizz.crm.domain.boardarticle.dto.BoardArticleDto;
import webbizz.crm.domain.boardarticle.dto.BoardArticleSaveDto;
import webbizz.crm.domain.member.entity.MemberUserDetails;
import webbizz.crm.response.ApiResponse;
import webbizz.crm.service.board.BoardArticleService;
import webbizz.crm.service.board.BoardService;

@Controller
@RequiredArgsConstructor
public class BoardArticleController {

    private final BoardService boardService;
    private final BoardArticleService boardArticleService;

    /**
     * 게시판 관리 - 게시판 목록 - 게시물 목록
     *
     * @param condition 검색 조건
     * @param model Model
     * @return 게시물 목록 화면
     * @author TAEROK HWANG
     */
    @GetMapping("/board/board/{boardId}/articles")
    @PreAuthorize("hasPermission('/board/board/' + #condition.boardId, 'GET')")
    public String boardBoardBoardIdArticles(@ModelAttribute("condition") BoardArticleCondition condition, Model model) {
        BoardDto board = boardService.searchById(condition.getBoardId());
        Page<BoardArticleDto> pageVars = boardArticleService.searchAllByCondition(condition);

        model.addAttribute("board", board);
        model.addAttribute("pageVars", pageVars);
        return "board/board_article_list";
    }

    /**
     * 게시판 관리 - 게시판 목록 - 게시물 목록 - 등록
     *
     * @param boardId 게시판 시퀀스
     * @param model Model
     * @return 게시물 등록 화면
     * @author TAEROK HWANG
     */
    @GetMapping("/board/board/{boardId}/write")
    @PreAuthorize("hasPermission('/board/board/' + #boardId, 'GET')")
    public String boardBoardBoardIdWrite(@PathVariable("boardId") Long boardId, Model model) {
        BoardDto board = boardService.searchById(boardId);

        model.addAttribute("board", board);
        return "board/board_article_write";
    }

    /**
     * 게시판 관리 - 게시판 목록 - 게시물 목록 - 상세
     *
     * @param boardId 게시판 시퀀스
     * @param id 게시물 시퀀스
     * @param model Model
     * @return 게시물 상세 화면
     * @author TAEROK HWANG
     */
    @GetMapping("/board/board/{boardId}/articles/{id}")
    @PreAuthorize("hasPermission('/board/board/' + #boardId, 'GET')")
    public String boardBoardBoardIdArticlesId(@PathVariable("boardId") Long boardId,
                                              @PathVariable("id") Long id,
                                              Model model) {

        BoardDto board = boardService.searchById(boardId);
        BoardArticleDto article = boardArticleService.searchByWithReadCount(boardId, id);

        model.addAttribute("board", board);
        model.addAttribute("article", article);
        return "board/board_article_detail";
    }

    /**
     * 게시판 관리 - 게시판 목록 - 게시물 목록 - 수정
     *
     * @param boardId 게시판 시퀀스
     * @param id 게시물 시퀀스
     * @param model Model
     * @return 게시물 수정 화면
     * @author TAEROK HWANG
     */
    @GetMapping("/board/board/{boardId}/articles/{id}/edit")
    @PreAuthorize("hasPermission('/board/board/' + #boardId, 'GET')")
    public String boardBoardBoardIdArticlesIdEdit(@PathVariable("boardId") Long boardId,
                                                  @PathVariable("id") Long id,
                                                  Model model) {

        BoardDto board = boardService.searchById(boardId);
        BoardArticleDto article = boardArticleService.searchBy(boardId, id);

        model.addAttribute("board", board);
        model.addAttribute("article", article);
        return "board/board_article_edit";
    }


    /**
     * 게시물 등록 API
     *
     * @param requestDto 게시물 저장 요청 DTO
     * @param memberUserDetails 로그인 회원 정보
     * @return 게시물 시퀀스
     */
    @PostMapping("/api/v1/board/article")
    @PreAuthorize("hasPermission('/board/board/' + #requestDto.boardId, 'POST')")
    @ResponseBody
    public ApiResponse<Long> boardArticlePostV1(@AuthenticationPrincipal MemberUserDetails memberUserDetails,
                                                @RequestBody @Validated BoardArticleSaveDto requestDto) {

        return ApiResponse.ok(boardArticleService.saveArticle(memberUserDetails, requestDto, HttpMethod.POST));
    }

    /**
     * 게시물 수정 API
     *
     * @param requestDto 게시물 저장 요청 DTO
     * @param memberUserDetails 로그인 회원 정보
     * @return 게시물 시퀀스
     * @author TAEROK HWANG
     */
    @PutMapping("/api/v1/board/article")
    @PreAuthorize("hasPermission('/board/board/' + #requestDto.boardId, 'POST')")
    @ResponseBody
    public ApiResponse<Long> boardArticlePutV1(@AuthenticationPrincipal MemberUserDetails memberUserDetails,
                                               @RequestBody @Validated BoardArticleSaveDto requestDto) {

        return ApiResponse.ok(boardArticleService.saveArticle(memberUserDetails, requestDto, HttpMethod.PUT));
    }

    /**
     * 게시물 삭제 API
     *
     * @param boardId 게시판 시퀀스
     * @param id 게시물 시퀀스
     * @return API 응답 결과
     * @author TAEROK HWANG
     */
    @DeleteMapping("/api/v1/board/{boardId}/article/{id}")
    @PreAuthorize("hasPermission('/board/board/' + #boardId, 'DELETE')")
    @ResponseBody
    public ApiResponse<Void> boardArticleIdDeleteV1(@PathVariable("boardId") Long boardId,
                                                    @PathVariable("id") Long id) {

        boardArticleService.deleteArticle(boardId, id);
        return ApiResponse.ok();
    }

}
