package webbizz.crm.web.exhibition;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import webbizz.crm.domain.exhibition.dto.ExhibitionCondition;
import webbizz.crm.domain.exhibition.dto.ExhibitionSaveDto;
import webbizz.crm.domain.exhibition.enumset.BannerType;
import webbizz.crm.domain.exhibition.enumset.ExhibitionType;
import webbizz.crm.domain.member.entity.MemberUserDetails;
import webbizz.crm.response.ApiResponse;
import webbizz.crm.service.exhibition.ExhibitionService;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
public class ExhibitionController {

    private final ExhibitionService exhibitionService;

    /**
     * Thymeleaf 에서 {@link BannerType} Enum 을 사용하기 위한 ModelAttribute
     *
     * @return BannerType Enum
     * @author TAEROK HWANG
     */
    @ModelAttribute("bannerType")
    public BannerType bannerType() {
        return BannerType.values()[0];
    }

    /**
     * Thymeleaf 에서 {@link ExhibitionType} Enum 을 사용하기 위한 ModelAttribute
     *
     * @return ExhibitionType Enum
     * @author TAEROK HWANG
     */
    @ModelAttribute("exhibitionType")
    public ExhibitionType exhibitionType() {
        return ExhibitionType.values()[0];
    }

    @GetMapping("/exhibition")
    public String exhibition(@AuthenticationPrincipal MemberUserDetails memberUserDetails, HttpServletRequest request) {
        return "redirect:" + memberUserDetails.getFirstMappingUrl(request, "/exhibition/main-banner");
    }

    /**
     * 전시 관리 - 메인 배너 - 목록
     *
     * @param condition 검색 조건
     * @param model Model
     * @return 메인 배너 목록 화면
     * @author TAEROK HWANG
     */
    @GetMapping("/exhibition/main-banner")
    public String exhibitionMainBanner(@ModelAttribute("condition") ExhibitionCondition condition, Model model) {
        condition.setType(ExhibitionType.MAIN);

        model.addAttribute("exhibitionType", ExhibitionType.MAIN);
        model.addAttribute("exhibitionLink", "/exhibition/main-banner");
        model.addAttribute("pageVars", exhibitionService.searchAllByCondition(condition));
        return "exhibition/banner_list";
    }

    /**
     * 전시 관리 - 메인 배너 - 등록
     *
     * @return 메인 배너 등록 화면
     * @author TAEROK HWANG
     */
    @GetMapping("/exhibition/main-banner/write")
    public String exhibitionMainBannerWrite(Model model) {
        model.addAttribute("exhibitionType", ExhibitionType.MAIN);
        model.addAttribute("exhibitionLink", "/exhibition/main-banner");
        return "exhibition/banner_write";
    }

    /**
     * 전시 관리 - 메인 배너 - 상세
     *
     * @param id 전시 시퀀스
     * @return 메인 배너 상세 화면
     * @author TAEROK HWANG
     */
    @GetMapping("/exhibition/main-banner/{id}")
    public String exhibitionMainBannerId(@PathVariable("id") String id, Model model) {
        model.addAttribute("exhibitionType", ExhibitionType.MAIN);
        model.addAttribute("exhibitionLink", "/exhibition/main-banner");
        model.addAttribute("dto", exhibitionService.searchBy(Long.parseLong(id), ExhibitionType.MAIN));
        return "exhibition/banner_view";
    }

    /**
     * 전시 관리 - 팝업 배너 - 목록
     *
     * @return 팝업 배너 목록 화면
     * @author TAEROK HWANG
     */
    @GetMapping("/exhibition/popup-banner")
    public String exhibitionPopupBanner(@ModelAttribute("condition") ExhibitionCondition condition, Model model) {
        condition.setType(ExhibitionType.POPUP);

        model.addAttribute("exhibitionType", ExhibitionType.POPUP);
        model.addAttribute("exhibitionLink", "/exhibition/popup-banner");
        model.addAttribute("pageVars", exhibitionService.searchAllByCondition(condition));
        return "exhibition/banner_list";
    }

    /**
     * 전시 관리 - 팝업 배너 - 등록
     *
     * @return 팝업 배너 등록 화면
     * @author TAEROK HWANG
     */
    @GetMapping("/exhibition/popup-banner/write")
    public String exhibitionPopupBannerWrite(Model model) {
        model.addAttribute("exhibitionType", ExhibitionType.POPUP);
        model.addAttribute("exhibitionLink", "/exhibition/popup-banner");
        return "exhibition/banner_write";
    }

    /**
     * 전시 관리 - 팝업 배너 - 상세
     *
     * @param id 전시 시퀀스
     * @return 팝업 배너 상세 화면
     * @author TAEROK HWANG
     */
    @GetMapping("/exhibition/popup-banner/{id}")
    public String exhibitionPopupBannerId(@PathVariable("id") String id, Model model) {
        model.addAttribute("exhibitionType", ExhibitionType.POPUP);
        model.addAttribute("exhibitionLink", "/exhibition/popup-banner");
        model.addAttribute("dto", exhibitionService.searchBy(Long.parseLong(id), ExhibitionType.POPUP));
        return "exhibition/banner_view";
    }

    /**
     * 전시 관리 - 상단 띠 배너 - 목록
     *
     * @return 상단 띠 배너 목록 화면
     * @author TAEROK HWANG
     */
    @GetMapping("/exhibition/top-banner")
    public String exhibitionTopBanner(@ModelAttribute("condition") ExhibitionCondition condition, Model model) {
        condition.setType(ExhibitionType.TOP);

        model.addAttribute("exhibitionType", ExhibitionType.TOP);
        model.addAttribute("exhibitionLink", "/exhibition/top-banner");
        model.addAttribute("pageVars", exhibitionService.searchAllByCondition(condition));
        return "exhibition/banner_list";
    }

    /**
     * 전시 관리 - 상단 띠 배너 - 등록
     *
     * @return 상단 띠 배너 등록 화면
     * @author TAEROK HWANG
     */
    @GetMapping("/exhibition/top-banner/write")
    public String exhibitionTopBannerWrite(Model model) {
        model.addAttribute("exhibitionType", ExhibitionType.TOP);
        model.addAttribute("exhibitionLink", "/exhibition/top-banner");
        return "exhibition/banner_write";
    }

    /**
     * 전시 관리 - 상단 띠 배너 - 상세
     *
     * @param id 전시 시퀀스
     * @return 상단 띠 배너 상세 화면
     * @author TAEROK HWANG
     */
    @GetMapping("/exhibition/top-banner/{id}")
    public String exhibitionTopBannerId(@PathVariable("id") String id, Model model) {
        model.addAttribute("exhibitionType", ExhibitionType.TOP);
        model.addAttribute("exhibitionLink", "/exhibition/top-banner");
        model.addAttribute("dto", exhibitionService.searchBy(Long.parseLong(id), ExhibitionType.TOP));
        return "exhibition/banner_view";
    }

    /**
     * 전시 관리 - 홍보 배너 - 목록
     *
     * @return 홍보 배너 목록 화면
     * @author TAEROK HWANG
     */
    @GetMapping("/exhibition/promotion-banner")
    public String exhibitionPromotionBanner(@ModelAttribute("condition") ExhibitionCondition condition, Model model) {
        condition.setType(ExhibitionType.PROMOTION);

        model.addAttribute("exhibitionType", ExhibitionType.PROMOTION);
        model.addAttribute("exhibitionLink", "/exhibition/promotion-banner");
        model.addAttribute("pageVars", exhibitionService.searchAllByCondition(condition));
        return "exhibition/banner_list";
    }

    /**
     * 전시 관리 - 홍보 배너 - 등록
     *
     * @return 홍보 배너 등록 화면
     * @author TAEROK HWANG
     */
    @GetMapping("/exhibition/promotion-banner/write")
    public String exhibitionPromotionBannerWrite(Model model) {
        model.addAttribute("exhibitionType", ExhibitionType.PROMOTION);
        model.addAttribute("exhibitionLink", "/exhibition/promotion-banner");
        return "exhibition/banner_write";
    }

    /**
     * 전시 관리 - 홍보 배너 - 상세
     *
     * @param id 전시 시퀀스
     * @return 홍보 배너 상세 화면
     * @author TAEROK HWANG
     */
    @GetMapping("/exhibition/promotion-banner/{id}")
    public String exhibitionPromotionBannerId(@PathVariable("id") String id, Model model) {
        model.addAttribute("exhibitionType", ExhibitionType.PROMOTION);
        model.addAttribute("exhibitionLink", "/exhibition/promotion-banner");
        model.addAttribute("dto", exhibitionService.searchBy(Long.parseLong(id), ExhibitionType.PROMOTION));
        return "exhibition/banner_view";
    }

    /**
     * 전시 관리 - 홍보 배너 2 - 목록
     *
     * @return 홍보 배너 목록 화면
     * @author TAEROK HWANG
     */
    @GetMapping("/exhibition/promotion-banner-2")
    public String exhibitionPromotionBanner2(@ModelAttribute("condition") ExhibitionCondition condition, Model model) {
        condition.setType(ExhibitionType.PROMOTION_2);

        model.addAttribute("exhibitionType", ExhibitionType.PROMOTION_2);
        model.addAttribute("exhibitionLink", "/exhibition/promotion-banner-2");
        model.addAttribute("pageVars", exhibitionService.searchAllByCondition(condition));
        return "exhibition/banner_list";
    }

    /**
     * 전시 관리 - 홍보 배너 2 - 등록
     *
     * @return 홍보 배너 등록 화면
     * @author TAEROK HWANG
     */
    @GetMapping("/exhibition/promotion-banner-2/write")
    public String exhibitionPromotionBanner2Write(Model model) {
        model.addAttribute("exhibitionType", ExhibitionType.PROMOTION_2);
        model.addAttribute("exhibitionLink", "/exhibition/promotion-banner-2");
        return "exhibition/banner_write";
    }

    /**
     * 전시 관리 - 홍보 배너 2 - 상세
     *
     * @param id 전시 시퀀스
     * @return 홍보 배너 상세 화면
     * @author TAEROK HWANG
     */
    @GetMapping("/exhibition/promotion-banner-2/{id}")
    public String exhibitionPromotionBanner2Id(@PathVariable("id") String id, Model model) {
        model.addAttribute("exhibitionType", ExhibitionType.PROMOTION_2);
        model.addAttribute("exhibitionLink", "/exhibition/promotion-banner-2");
        model.addAttribute("dto", exhibitionService.searchBy(Long.parseLong(id), ExhibitionType.PROMOTION_2));
        return "exhibition/banner_view";
    }


    /**
     * 전시 정보 등록 API
     *
     * @param requestDto 전시 저장 요청 DTO
     * @return 전시 시퀀스
     * @author TAEROK HWANG
     */
    @PostMapping("/api/v1/exhibition")
    @ResponseBody
    public ApiResponse<Long> exhibitionMainBannerPostV1(@RequestBody @Validated ExhibitionSaveDto requestDto) {
        return ApiResponse.ok(exhibitionService.saveExhibition(requestDto, HttpMethod.POST));
    }

    /**
     * 전시 정보 수정 API
     *
     * @param requestDto 전시 저장 요청 DTO
     * @return 전시 시퀀스
     * @author TAEROK HWANG
     */
    @PutMapping("/api/v1/exhibition")
    @ResponseBody
    public ApiResponse<Long> exhibitionMainBannerPutV1(@RequestBody @Validated ExhibitionSaveDto requestDto) {
        return ApiResponse.ok(exhibitionService.saveExhibition(requestDto, HttpMethod.PUT));
    }

    /**
     * 전시 정보 삭제 API
     *
     * @param id 전시 시퀀스
     * @return API 응답 결과
     * @author TAEROK HWANG
     */
    @DeleteMapping("/api/v1/exhibition/{id}")
    @ResponseBody
    public ApiResponse<Void> exhibitionMainBannerDeleteV1(@PathVariable("id") Long id) {
        exhibitionService.deleteExhibition(id);
        return ApiResponse.ok();
    }

}
