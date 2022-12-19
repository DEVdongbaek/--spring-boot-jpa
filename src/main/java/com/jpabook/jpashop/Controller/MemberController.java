package com.jpabook.jpashop.Controller;

import com.jpabook.jpashop.domain.Address;
import com.jpabook.jpashop.domain.Member;
import com.jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model) { // Model은 Controlloer에서 View로 넘길 데이터를 담는다.
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    // @Valid을 인자앞에 붙여주게 되면, Form 객체에서 작성했던 Validation들이 동작하게 된다.
    public String create(@Valid MemberForm form, BindingResult result) { // Form에서 작성한 memberForm이 파라미터로 넘어오게 된다.

        if (result.hasErrors()) { // 만약 result에 error가 담겨서 온다면, 아래 페이지에 오류 메세지를 뿌려줌
            return "members/createMemberForm";
        }

        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());

        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);

        memberService.join(member);

        return "redirect:/"; // home으로 리다이렉트 시킨다.
    }

    @GetMapping("/members")
    public String list(Model model) {
        model.addAttribute("members", memberService.findMembers()); // ctrl + shift + n은 변수를 가독성있게 합치는 거다.

        return "members/memberList";
    }
}
