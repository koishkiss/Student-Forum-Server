package student.forum.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageRouteController {

    @GetMapping("/")
    public String index() {
        return "forward:/index.html";
    }

    @GetMapping("/main")
    public String mainPage() {
        return "forward:/index.html";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "forward:/index.html";
    }

    @GetMapping("/post")
    public String postPage() {
        return "forward:/index.html";
    }

    @GetMapping("/personal/{child}")
    public String personalPage1() {
        return "forward:/index.html";
    }

    @GetMapping("/personal/{child}/{item}")
    public String personalPage2() {
        return "forward:/index.html";
    }

    @GetMapping("/visit/other/person/{uid}/{item}")
    public String otherPersonPage() {
        return "forward:/index.html";
    }

    @GetMapping("/section/{id}/post/{type}")
    public String sectionPage() {
        return "forward:/index.html";
    }

    @GetMapping("/section/{id}/setting")
    public String sectionPage2() {
        return "forward:/index.html";
    }

    @GetMapping("/message/{item}")
    public String messagePage() {
        return "forward:/index.html";
    }

    @GetMapping("/search")
    public String searchPage() {
        return "forward:/index.html";
    }

}
