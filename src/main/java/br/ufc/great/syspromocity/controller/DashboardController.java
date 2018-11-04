package br.ufc.great.syspromocity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Faz o controle do Dashboard
 * @author armandosoaressousa
 *
 */
@Controller
public class DashboardController {

    @RequestMapping("/")
    public String index() {
        return "dashboard/index";
    }

}
