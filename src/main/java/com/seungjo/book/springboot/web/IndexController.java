package com.seungjo.book.springboot.web;

import com.seungjo.book.springboot.config.auth.LoginUser;
import com.seungjo.book.springboot.config.auth.dto.SessionUser;
import com.seungjo.book.springboot.domain.posts.Posts;
import com.seungjo.book.springboot.domain.user.Role;
import com.seungjo.book.springboot.domain.user.User;
import com.seungjo.book.springboot.service.posts.PostsService;
import com.seungjo.book.springboot.web.dto.PostsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostsService postsService;

    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || AnonymousAuthenticationToken.class.isAssignableFrom(authentication.getClass())) {
            return false;
        }
        return authentication.isAuthenticated();
    }
    
    //@LoginUser를 사용하여 세션 정보를 가져옴
    @GetMapping("/")
    public String index(Model model, @LoginUser SessionUser user){
        model.addAttribute("posts", postsService.findAllDesc());
        
        if (user != null) {
            model.addAttribute("loginUserName", user.getName());
        }
        return "index";
    }


    @GetMapping("/posts/save")
    public String postsSave(){
        return "post/posts-save";
    }

    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model){
        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("post", dto);
        return "post/posts-update";
    }

    @GetMapping("/posts/search")
    public String search(String keyword, Model model){
        List<Posts> searchList = postsService.search(keyword);

        model.addAttribute("searchList", searchList);

        return "post/posts-search";
    }

    @GetMapping("/login")
    public String getLoginPage(Model model, @LoginUser SessionUser user) throws Exception {
        if (isAuthenticated()) {
            model.addAttribute("loginUserName", user.getName());
            return "index";
        }
        return "oauth/login";
    }

    @GetMapping("/introduce")
    public String introducePage(Model model, @LoginUser SessionUser user) {
        if (user != null) {
            model.addAttribute("loginUserName", user.getName());
        }
        return "nav/introduce";
    }

    @GetMapping("/notice")
    public String noticePage(Model model, @LoginUser SessionUser user){

        if (user != null) {
            model.addAttribute("loginUserName", user.getName());

        }

        model.addAttribute("posts", postsService.findAllDesc());
        return "nav/notice";
    }

}