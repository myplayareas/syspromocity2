package br.ufc.great.syspromocity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Faz o controle do domínio de Controle de Acesso
 * @author armandosoaressousa
 *
 */
@Controller
public class AccessControlController {

    @RequestMapping(value="/accesscontrol")
    public String index() {
        return "accesscontrol/list";
    }
	
}
