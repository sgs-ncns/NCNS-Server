package dev.ncns.sns.search.controller;

import dev.ncns.sns.search.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping(value = "/api/search")
@RestController
public class UserController {

    private final UserService userService;

}
