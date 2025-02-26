package webbizz.crm.web.board;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class BoardOtherController {

    @GetMapping("/board/faq")
    public String boardFaq() {
        return "board/faq_list";
    }

    @GetMapping("/board/faq/write")
    public String boardFaqWrite() {
        return "board/faq_write";
    }

    @GetMapping("/board/faq/{id}")
    public String boardFaqId(@PathVariable("id") String id) {
        return "board/faq_view";
    }

}
