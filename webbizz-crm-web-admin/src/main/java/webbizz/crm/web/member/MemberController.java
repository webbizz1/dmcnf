package webbizz.crm.web.member;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import webbizz.crm.domain.member.dto.MemberCondition;
import webbizz.crm.domain.member.dto.MemberPasswordResetDto;
import webbizz.crm.domain.member.entity.MemberUserDetails;
import webbizz.crm.response.ApiResponse;
import webbizz.crm.service.member.MemberService;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/member")
    public String member(@AuthenticationPrincipal MemberUserDetails memberUserDetails, HttpServletRequest request) {
        return "redirect:" + memberUserDetails.getFirstMappingUrl(request, "/member/active");
    }

    /**
     * 회원 관리 - 활동 회원
     *
     * @param condition 검색 조건
     * @param model Model
     * @return 활동 회원 목록 화면
     * @author TAEROK HWANG
     */
    @GetMapping("/member/active")
    @PreAuthorize("hasPermission('/member/active', 'GET')")
    public String memberActive(@ModelAttribute("condition") MemberCondition condition, Model model) {
        model.addAttribute("pageVars", memberService.searchAllMemberByCondition(condition));
        return "member/active_list";
    }

    /**
     * 회원 관리 - 활동 회원 상세
     *
     * @param id 회원 시퀀스
     * @param model Model
     * @return 활동 회원 상세 화면
     * @author TAEROK HWANG
     */
    @GetMapping("/member/active/{id}")
    @PreAuthorize("hasPermission('/member/active/' + #id, 'GET')")
    public String memberActiveId(@PathVariable("id") Long id, Model model) {
        model.addAttribute("dto", memberService.searchMemberById(id));
        return "member/active_view";
    }

    /**
     * 회원 관리 - 탈퇴 회원
     *
     * @param condition 검색 조건
     * @param model Model
     * @return 탈퇴 회원 목록 화면
     * @author TAEROK HWANG
     */
    @GetMapping("/member/withdrawal")
    @PreAuthorize("hasPermission('/member/withdrawal', 'GET')")
    public String memberWithdrawal(@ModelAttribute("condition") MemberCondition condition, Model model) {
        model.addAttribute("pageVars", memberService.searchAllWithdrawalMemberByCondition(condition));
        return "member/withdrawal_list";
    }

    /**
     * 회원 관리 - 탈퇴 회원 상세
     *
     * @param id 회원 시퀀스
     * @param model Model
     * @return 탈퇴 회원 상세 화면
     */
    @GetMapping("/member/withdrawal/{id}")
    @PreAuthorize("hasPermission('/member/withdrawal/' + #id, 'GET')")
    public String memberWithdrawalId(@PathVariable("id") Long id, Model model) {
        model.addAttribute("dto", memberService.searchWithdrawalMemberById(id));
        return "member/withdrawal_view";
    }


    /**
     * 회원 비밀번호 초기화 API
     *
     * @param requestDto 회원 비밀번호 초기화 DTO
     * @return 초기화된 비밀번호
     * @author TAEROK HWANG
     */
    @PostMapping("/api/v1/member/active/reset-password")
    @PreAuthorize("hasPermission('/member/active/reset-password', 'POST')")
    @ResponseBody
    public ApiResponse<String> memberActiveResetPasswordPostV1(@RequestBody @Validated
                                                               MemberPasswordResetDto requestDto) {

        return ApiResponse.ok(memberService.resetPassword(requestDto));
    }

    /**
     * 활동 회원 엑셀 다운로드 API
     *
     * @param condition 검색 조건
     * @author TAEROK HWANG
     */
    @GetMapping("/api/v1/member/active/excel-download")
    @PreAuthorize("hasPermission('/member/active', 'GET')")
    public ResponseEntity<byte[]> memberActiveExcelDownload(MemberCondition condition) {
        return memberService.searchMemberForExcel(condition).toResponseEntity();
    }

    /**
     * 탈퇴 회원 엑셀 다운로드 API
     *
     * @param condition 검색 조건
     * @author TAEROK HWANG
     */
    @GetMapping("/api/v1/member/withdrawal/excel-download")
    @PreAuthorize("hasPermission('/member/withdrawal', 'GET')")
    public ResponseEntity<byte[]> memberWithdrawalExcelDownload(MemberCondition condition) {
        return memberService.searchWithdrawalMemberForExcel(condition).toResponseEntity();
    }

}
