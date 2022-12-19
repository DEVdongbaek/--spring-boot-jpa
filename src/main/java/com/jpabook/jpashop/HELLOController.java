package com.jpabook.jpashop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HELLOController {
    
    // hello라는 url에 매핑되는 메소드
    @GetMapping("hello")
    // Model은 model 객체에 데이터를 실어서 view에 보낼 수 있도록 한다.
    public String hello(Model model) {
        // data라는 key에 hello!!! value 전달
        model.addAttribute("data", "hello!!!");
        return "hello"; // hello.html에 전달한다는 뜻
    }
}
