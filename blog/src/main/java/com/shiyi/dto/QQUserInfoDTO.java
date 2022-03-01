package com.shiyi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author blue
 * @date 2022/1/5
 * @apiNote
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QQUserInfoDTO {

    /**
     * 昵称
     */
    private String nickname;

    /**
     * qq头像
     */
    private String figureurl_qq_1;


}
