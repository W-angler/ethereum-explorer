package com.w_angler.ethereum.explorer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {
    @RequestMapping(value = {"/","index.html","eth", "eth.html"})
    public String eth() {
        return "eth";
    }
}
