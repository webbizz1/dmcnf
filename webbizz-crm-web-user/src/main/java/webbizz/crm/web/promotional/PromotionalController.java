package webbizz.crm.web.promotional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import webbizz.crm.service.menu.MenuRenderService;
import webbizz.crm.service.menu.MenuService;


@Controller
@RequiredArgsConstructor
public class PromotionalController {

    private final MenuService menuService;
    private final MenuRenderService menuRenderService;

    @GetMapping("/promotional/materials")
    public String materials() {

        return "promotional/promotional_materials";
    }
}
