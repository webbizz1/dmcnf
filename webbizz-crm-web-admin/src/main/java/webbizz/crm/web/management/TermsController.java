package webbizz.crm.web.management;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import webbizz.crm.domain.BaseEnumSet;
import webbizz.crm.domain.terms.dto.TermsCondition;
import webbizz.crm.domain.terms.dto.TermsSaveDto;
import webbizz.crm.domain.terms.enumset.TermsType;
import webbizz.crm.response.ApiResponse;
import webbizz.crm.service.terms.TermsService;

@Controller
@RequiredArgsConstructor
public class TermsController {

    private final TermsService termsService;

    /**
     * Thymeleaf 에서 {@link TermsType} Enum 을 사용하기 위한 ModelAttribute
     *
     * @return TermsType Enum
     * @author TAEROK HWANG
     */
    @ModelAttribute("termsType")
    public TermsType termsType() {
        return TermsType.values()[0];
    }

    /**
     * 첫 번째 약관 페이지로 이동
     */
    @GetMapping("/management/terms")
    public String managementTerms() {
        return "redirect:/management/terms/" + TermsType.TERMS.getCode().toLowerCase();
    }

    /**
     * 운영 - [약관명] 약관 - 목록
     *
     * @param termsCode 약관 유형 (LowerCase 된 약관 코드 문자열)
     * @param condition 검색 조건
     * @param model Model
     * @return 약관 목록 화면
     * @author TAEROK HWANG
     */
    @GetMapping("/management/terms/{termsCode}")
    public String managementTermsTermsCode(@PathVariable("termsCode") String termsCode,
                                           @ModelAttribute("condition") TermsCondition condition,
                                           Model model) {

        condition.setType(BaseEnumSet.of(TermsType.class, termsCode));

        model.addAttribute("listVars", termsService.searchAllByCondition(condition));
        return "management/terms_list";
    }

    /**
     * 운영 - [약관명] 약관 - 등록
     *
     * @param termsCode 약관 코드 (LowerCase)
     * @param model Model
     * @return 약관 등록 화면
     * @author TAEROK HWANG
     */
    @GetMapping("/management/terms/{termsCode}/write")
    public String managementTermsTermsCodeWrite(@PathVariable("termsCode") String termsCode, Model model) {
        model.addAttribute("termsCode", termsCode);
        model.addAttribute("termsType", BaseEnumSet.of(TermsType.class, termsCode));
        return "management/terms_write";
    }

    /**
     * 운영 - [약관명] 약관 - 상세
     *
     * @param termsCode 약관 코드 (LowerCase)
     * @param id 약관 시퀀스
     * @param model Model
     * @return 약관 상세 화면
     * @author TAEROK HWANG
     */
    @GetMapping("/management/terms/{termsCode}/{id}")
    public String managementTermsTermsCodeId(@PathVariable("termsCode") String termsCode,
                                             @PathVariable("id") Long id,
                                             Model model) {

        model.addAttribute("termsCode", termsCode);
        model.addAttribute("termsType", BaseEnumSet.of(TermsType.class, termsCode));
        model.addAttribute("dto", termsService.searchBy(id, BaseEnumSet.of(TermsType.class, termsCode)));
        return "management/terms_view";
    }


    /**
     * 약관 등록 API
     *
     * @param requestDto 약관 등록 요청 DTO
     * @return 약관 시퀀스
     * @author TAEROK HWANG
     */
    @PostMapping("/api/v1/terms")
    @ResponseBody
    public ApiResponse<Long> termsPostV1(@RequestBody @Validated TermsSaveDto requestDto) {
        return ApiResponse.ok(termsService.saveTerms(requestDto, HttpMethod.POST));
    }

    /**
     * 약관 수정 API
     *
     * @param requestDto 약관 수정 요청 DTO
     * @return 약관 시퀀스
     * @author TAEROK HWANG
     */
    @PutMapping("/api/v1/terms")
    @ResponseBody
    public ApiResponse<Long> termsPutV1(@RequestBody @Validated TermsSaveDto requestDto) {
        return ApiResponse.ok(termsService.saveTerms(requestDto, HttpMethod.PUT));
    }

    /**
     * 약관 활성화 API
     *
     * @param id 약관 시퀀스
     * @return API 응답 결과
     * @author TAEROK HWANG
     */
    @PostMapping("/api/v1/terms/{id}/active")
    @ResponseBody
    public ApiResponse<Void> termsActivePostV1(@PathVariable("id") Long id) {
        termsService.activeTerms(id);
        return ApiResponse.ok();
    }

}
