package com.dmc.web.main;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DmcCnfMainController {

    @GetMapping({"/", "/cnf", "/cnf/"})
    public String dmcCnfMain (){
        return "main/index";
    }
}
