package com.supshop.suppingmall.user;

import com.supshop.suppingmall.common.UserUtils;
import com.supshop.suppingmall.error.exception.user.AlreadyAppliedSellerException;
import com.supshop.suppingmall.page.ThirtyItemsCriteria;
import com.supshop.suppingmall.page.PageMaker;
import com.supshop.suppingmall.user.Form.ApplySellerForm;
import com.supshop.suppingmall.user.Form.PasswordCheckForm;
import com.supshop.suppingmall.user.Form.UpdateSellerForm;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/users/seller")
@Controller
@RequiredArgsConstructor
public class SellerController {

    private final UserService userService;
    private final ModelMapper modelMapper;
    private static final String redirectLoginUrl = "redirect:/users/loginform";
    private static final String redirectMainUrl = "redirect:/";
    private static final int pagingCount = 15;



    @GetMapping("/{id}/main")
    public String getSellerInfoPage(@PathVariable Long id,
                                    @AuthenticationPrincipal SessionUser sessionUser,
                                    Model model) {


        if(!UserUtils.isOwner(id, sessionUser)) {
            return redirectMainUrl;
        }
        User user = userService.getUser(id);
        model.addAttribute("store",user.getStoreVO());

        return "/user/seller/main";
    }

    @GetMapping("")
    @ResponseBody
    public ResponseEntity<String> getSellers(@RequestParam(required = false) String type,
                                             @RequestParam(required = false) String value) {

        List<SessionUser> stores = userService.getStore(type, value);
        if(stores.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().build();

    }

//    @GetMapping("/{id}")
//    public String getSeller(@PathVariable Long id, Model model, @AuthenticationPrincipal SessionUser sessionUser) {
//        if(!UserUtils.isOwner(id, sessionUser)) {
//            throw new IllegalArgumentException("유효하지 않은 회원입니다.");
//        }
//        model.addAttribute("user", sessionUser);
//        return "redirect:/users/seller/"+id+"/form";
//    }

    @GetMapping("/{id}/updateform")
    public String getSellerUpdateForm(@PathVariable Long id,
                                    @AuthenticationPrincipal SessionUser sessionUser,
                                    Model model) {


        if(!UserUtils.isOwner(id, sessionUser)) {
            throw new IllegalArgumentException("유효하지 않은 유저");
        }
        User user = userService.getUser(id);
        model.addAttribute("store",user.getStoreVO());

        return "/user/seller/updateForm";

    }

    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity updateSeller(@PathVariable Long id,
                                       @RequestBody @Valid UpdateSellerForm form,
                                       @AuthenticationPrincipal SessionUser sessionUser) {

        if(!UserUtils.isOwner(id, sessionUser)) {
            throw new IllegalArgumentException("유효하지 않은 유저입니다.");
        }

        User originUser = userService.getUser(id);

        // 제대로 비밀번호를 입력했는지 확인
        if(!userService.matchedPassword(originUser.getEmail(), form.getPassword())){
            throw new IllegalArgumentException("유효하지 않은 패스워드입니다.");
        }

        StoreVO storeVO = modelMapper.map(form, StoreVO.class);

        userService.patchUser(id, User.builder().storeVO(storeVO).build());

        return ResponseEntity.ok().build();
    }

    @GetMapping("/apply")
    public String getApplySellerRoleForm(Model model, @AuthenticationPrincipal SessionUser sessionUser) {
        User user = userService.getUser(sessionUser.getUserId());
        if ("Y".equals(user.getStoreVO().getStoreApplyYn())) {
           throw new AlreadyAppliedSellerException("이미 지원한 유저입니다.");
        }
        model.addAttribute("user", sessionUser);
        return "/user/seller/applySellerForm";
    }

    @PostMapping("/apply")
    public String applySellerRole(@Valid ApplySellerForm applyForm,
                                  @AuthenticationPrincipal SessionUser sessionUser) {
        User user = userService.getUser(sessionUser.getUserId());
        if("Y".equals(user.getStoreVO().getStoreApplyYn())) {
            throw new AlreadyAppliedSellerException("이미 지원한 유저입니다.");
        }
        StoreVO store = modelMapper.map(applyForm, StoreVO.class);
        userService.patchUser(sessionUser.getUserId(),User.builder().storeVO(store).build());
        return "redirect:/users/"+sessionUser.getUserId()+"/form";
    }

    @GetMapping("/applicant")
    public String getApplySellers(ThirtyItemsCriteria criteria,
                                 @RequestParam(required = false) String type,
                                 @RequestParam(required = false) String searchValue,
                                 Model model) {

        List<User> applySellerUsers = userService.getApplySellerUsers(criteria,type,searchValue);
        PageMaker pageMaker = new PageMaker(applySellerUsers.size(),pagingCount, criteria);

        model.addAttribute("userList",applySellerUsers);
        model.addAttribute("pageMaker", pageMaker);
        return "/user/admin/applySellerList";
    }


    @PatchMapping("/{id}/apply")
    @ResponseBody
    public ResponseEntity grantSellerRole(@PathVariable Long id) {
        userService.grantSeller(id);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/transfer")
    public String getTransferForm(@AuthenticationPrincipal SessionUser sessionUser,
                                  Model model){

        User user = userService.getUser(sessionUser.getUserId());

        model.addAttribute("user",user);

        return "/user/seller/transferForm";
    }


    @PostMapping("/transfer")
    @ResponseBody
    public ResponseEntity transferSellerToUser(@RequestBody PasswordCheckForm form,
                                               @AuthenticationPrincipal SessionUser sessionUser){

        User originUser = userService.getUser(sessionUser.getUserId());

        // 제대로 비밀번호를 입력했는지 확인
        if(!userService.matchedPassword(originUser.getEmail(), form.getPassword())){
            return ResponseEntity.badRequest().body("유효하지 않은 패스워드 입니다.");
        }

        User user = userService.getUser(originUser.getUserId());
        user.setRole(Role.USER);
        userService.patchUser(user.getUserId(), user);

        return ResponseEntity.ok().build();
    }

}
