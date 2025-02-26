package webbizz.crm.web.management;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import webbizz.crm.domain.menu.dto.MenuDto;
import webbizz.crm.domain.menu.dto.MenuSaveDto;
import webbizz.crm.exception.BusinessException;
import webbizz.crm.response.ApiResponse;
import webbizz.crm.service.menu.MenuService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    /**
     * 운영 - 메뉴 관리
     *
     * @param groupId 그룹 ID
     * @param model Model
     * @return 메뉴 관리 화면
     * @author TAEROK HWANG
     */
    @GetMapping("/management/menu")
    @PreAuthorize("hasPermission('/management/menu', 'GET')")
    public String managementMenu(@RequestParam(name = "groupId", required = false) Long groupId, Model model) {
        if (groupId == null) {
            return "redirect:/management/menu?groupId=1";
        }
        if (groupId > 2) {
            throw new BusinessException(404, "페이지를 찾을 수 없습니다.");
        }

        model.addAttribute("groupId", groupId);
        model.addAttribute("menuDtos", menuService.searchAllForNonDeleted(groupId));
        return "management/menu_list";
    }


    /**
     * 메뉴 조회 API
     *
     * @param groupId 그룹 ID
     * @return 메뉴 DTO 리스트
     * @author TAEROK HWANG
     */
    @GetMapping("/api/v1/management/menu")
    @PreAuthorize("hasPermission('/management/menu', 'GET')")
    @ResponseBody
    public ApiResponse<List<MenuDto>> managementMenuV1(@RequestParam(name = "groupId",
                                                                     required = false,
                                                                     defaultValue = "1") Long groupId) {

        return ApiResponse.ok(menuService.searchAllForNonDeleted(groupId));
    }

    /**
     * 메뉴 저장 API
     *
     * @param groupId 그룹 ID
     * @param requestDto 메뉴 저장 요청 DTO
     * @return API 응답 결과
     * @author TAEROK HWANG
     */
    @PostMapping("/api/v1/management/menu")
    @PreAuthorize("hasPermission('/management/menu', 'POST')")
    @ResponseBody
    public ApiResponse<Void> managementMenuPostV1(@RequestParam(name = "groupId",
                                                                required = false,
                                                                defaultValue = "1") Long groupId,
                                                  @RequestBody @Validated List<MenuSaveDto> requestDto) {

        menuService.saveMenus(groupId, requestDto);
        return ApiResponse.ok();
    }

}
