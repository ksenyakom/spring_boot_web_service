package com.epam.esm.controller;

import com.epam.esm.dto.JsonResult;
import com.epam.esm.dto.TokenDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.facade.UserFacade;
import com.epam.esm.model.User;
import com.epam.esm.dto.LoginRequest;
import com.epam.esm.security.TokenProvider;
import com.epam.esm.service.ServiceException;
import com.epam.esm.service.UserService;
import com.epam.esm.validator.UserDtoValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;

@RestController
@RequestMapping("/auth")
public class AuthRestController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private UserDtoValidator userDtoValidator;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private UserFacade userFacade;

    @PostMapping("/login")
    public JsonResult<TokenDto> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProvider.createToken(authentication);
        TokenDto tokenDto = new TokenDto();
        tokenDto.setEmail(loginRequest.getEmail());
        tokenDto.setToken(token);

        return new JsonResult.Builder<TokenDto>()
                .withSuccess(true)
                .withResult(Collections.singletonList(tokenDto))
                .build();
    }

    @PostMapping("/signup")
    public JsonResult<User> registerUser(@RequestBody UserDto userDto, BindingResult result) {
        userDtoValidator.validate(userDto, result);
        if (result.hasErrors()) {
            throw new ServiceException(message(result), "42283");
        }
        User user = mapper.map(userDto, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userFacade.registerNewUserAccount(user);
    }

    private String message(BindingResult result) {
        StringBuilder sb = new StringBuilder();
        result.getFieldErrors()
                .forEach(fieldError -> sb.append(" ")
                        .append(fieldError.getField()).append(": ")
                        .append(fieldError.getCode()).append("."));
        return sb.toString();
    }
}
