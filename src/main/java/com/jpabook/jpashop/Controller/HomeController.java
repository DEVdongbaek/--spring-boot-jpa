package com.jpabook.jpashop.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j // java.util.logging, logback 및 log4j와 같은 다양한 로깅 프레임 워크에 대한 추상화(인터페이스) 역할을 하는 라이브러리에요.
public class HomeController {


    @RequestMapping("/")
    public String home() {
        log.info("home controller");
        return "home"; // home html과 매핑
    }


}
