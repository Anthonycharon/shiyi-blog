package com.shiyi.dto;

import lombok.Data;

import java.util.Set;

/**
 * @author blue
 * @date 2021/12/25
 * @apiNote
 */
@Data
public class LoginUserBackDTO {
    private Long id;
    private String email;
    private String avatar;
    private String nickname;
    private String loginType;
    private String token;
    private Set<Object> articleLikeSet;
}
