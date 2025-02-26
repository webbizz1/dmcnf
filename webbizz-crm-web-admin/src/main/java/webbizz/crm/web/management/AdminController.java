package webbizz.crm.web.management;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import webbizz.crm.domain.member.dto.MemberAdminSaveDto;
import webbizz.crm.domain.member.dto.MemberCondition;
import webbizz.crm.domain.member.entity.MemberUserDetails;
import webbizz.crm.domain.member.enumset.MemberRole;
import webbizz.crm.response.ApiResponse;
import webbizz.crm.service.member.AuthorityService;
import webbizz.crm.service.member.MemberAdminService;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final MemberAdminService memberAdminService;
    private final AuthorityService authorityService;

    @GetMapping("/management")
    public String management(@AuthenticationPrincipal MemberUserDetails memberUserDetails, HttpServletRequest request) {
        return "redirect:" + memberUserDetails.getFirstMappingUrl(request, "/management/admin");
    }

    /**
     * 운영 - 운영자 관리 - 목록
     *
     * @param condition 검색 조건
     * @param model Model
     * @return 운영자 목록 화면
     * @author TAEROK HWANG
     */
    @GetMapping("/management/admin")
    @PreAuthorize("hasPermission('/management/admin', 'GET')")
    public String managementAdmin(@ModelAttribute("condition") MemberCondition condition, Model model) {
        condition.setRole(MemberRole.ADMIN);

        model.addAttribute("pageVars", memberAdminService.searchAllMemberAdminByCondition(condition));
        return "management/admin_list";
    }

    /**
     * 운영 - 운영자 관리 - 등록
     *
     * @param model Model
     * @return 운영자 등록 화면
     * @author TAEROK HWANG
     */
    @GetMapping("/management/admin/write")
    @PreAuthorize("hasPermission('/management/admin', 'GET')")
    public String managementAdminWrite(Model model) {
        model.addAttribute("authorities", authorityService.searchAllByRole(MemberRole.ADMIN));
        return "management/admin_write";
    }

    /**
     * 운영 - 운영자 관리 - 상세
     *
     * @param id 운영자 시퀀스
     * @return 운영자 상세 화면
     * @author TAEROK HWANG
     */
    @GetMapping("/management/admin/{id}")
    @PreAuthorize("hasPermission('/management/admin', 'GET')")
    public String managementAdminId(@PathVariable("id") Long id, Model model) {
        model.addAttribute("dto", memberAdminService.searchMemberAdminById(id));
        model.addAttribute("authorities", authorityService.searchAllByRole(MemberRole.ADMIN));
        return "management/admin_view";
    }


    /**
     * 관리자 회원 등록 API
     *
     * @param requestDto 관리자 회원 저장 요청 DTO
     * @return 회원 시퀀스
     * @author TAEROK HWANG
     */
    @PostMapping("/api/v1/member")
    @PreAuthorize("hasPermission('/management/admin', 'POST')")
    @ResponseBody
    public ApiResponse<Long> memberPostV1(@RequestBody @Validated MemberAdminSaveDto requestDto) {
        return ApiResponse.ok(memberAdminService.saveMemberAdmin(requestDto, HttpMethod.POST));
    }

    /**
     * 관리자 회원 수정 API
     *
     * @param requestDto 관리자 회원 저장 요청 DTO
     * @return 회원 시퀀스
     * @author TAEROK HWANG
     */
    @PutMapping("/api/v1/member")
    @PreAuthorize("hasPermission('/management/admin', 'PUT')")
    @ResponseBody
    public ApiResponse<Long> memberPutV1(@RequestBody @Validated MemberAdminSaveDto requestDto) {
        return ApiResponse.ok(memberAdminService.saveMemberAdmin(requestDto, HttpMethod.PUT));
    }

    /**
     * 아이디 중복 체크 API
     *
     * @param loginId 로그인 아이디
     * @return 중복 여부 (NULL: 중복 없음, 그 외: 오류 메시지)
     * @author TAEROK HWANG
     */
    @GetMapping("/api/v1/member/check-login-id")
    @PreAuthorize("hasPermission('/management/admin', 'GET')")
    @ResponseBody
    public ApiResponse<String> checkLoginId(@RequestParam("loginId") String loginId) {
        return ApiResponse.ok(memberAdminService.checkLoginId(loginId, MemberRole.ADMIN));
    }

}
