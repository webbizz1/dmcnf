package webbizz.crm.web.board;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import webbizz.crm.domain.board.dto.BoardCondition;
import webbizz.crm.domain.board.dto.BoardDto;
import webbizz.crm.domain.board.dto.BoardSaveDto;
import webbizz.crm.domain.member.entity.MemberUserDetails;
import webbizz.crm.response.ApiResponse;
import webbizz.crm.service.board.BoardService;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/board")
    public String board(@AuthenticationPrincipal MemberUserDetails memberUserDetails, HttpServletRequest request) {
        return "redirect:" + memberUserDetails.getFirstMappingUrl(request, "/board/board");
    }

    /**
     * 게시판 관리 - 게시판 목록 - 목록
     *
     * @param condition 검색 조건
     * @param model Model
     * @return 게시판 목록 화면
     * @author TAEROK HWANG
     */
    @GetMapping("/board/board")
    @PreAuthorize("hasPermission('/board/board', 'GET')")
    public String boardBoard(@ModelAttribute("condition") BoardCondition condition, Model model) {
        model.addAttribute("pageVars", boardService.searchAllByCondition(condition));
        return "board/board_list";
    }

    /**
     * 게시판 관리 - 게시판 목록 - 생성
     *
     * @return 게시판 생성 화면
     * @author TAEROK HWANG
     */
    @GetMapping("/board/board/create")
    @PreAuthorize("hasPermission('/board/board', 'GET')")
    public String boardBoardCreate() {
        return "board/board_create";
    }

    /**
     * 게시판 관리 - 게시판 목록 - 수정
     *
     * @param id 게시판 시퀀스
     * @param model Model
     * @return 게시판 수정 화면
     * @author TAEROK HWANG
     */
    @GetMapping("/board/board/{id}")
    @PreAuthorize("hasPermission('/board/board/' + #id, 'GET')")
    public String boardBoardId(@PathVariable("id") Long id, Model model) {
        BoardDto board = boardService.searchById(id);

        model.addAttribute("board", board);
        return "board/board_view";
    }


    /**
     * 게시판 정보 생성 API
     *
     * @param requestDto 게시판 생성 요청 DTO
     * @return 게시판 시퀀스
     * @author TAEROK HWANG
     */
    @PostMapping("/api/v1/board")
    @PreAuthorize("hasPermission('/board/board', 'POST')")
    @ResponseBody
    public ApiResponse<Long> boardPostV1(@RequestBody @Validated BoardSaveDto requestDto) {
        return ApiResponse.ok(boardService.createBoard(requestDto));
    }

    /**
     * 게시판 정보 수정 API
     *
     * @param requestDto 게시판 수정 요청 DTO
     * @return 게시판 시퀀스
     * @author TAEROK HWANG
     */
    @PutMapping("/api/v1/board")
    @PreAuthorize("hasPermission('/board/board' + #requestDto.id, 'PUT')")
    @ResponseBody
    public ApiResponse<Long> boardPutV1(@RequestBody @Validated BoardSaveDto requestDto) {
        return ApiResponse.ok(boardService.updateBoard(requestDto));
    }

}
