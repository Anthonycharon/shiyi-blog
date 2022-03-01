package com.shiyi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author blue
 * @date 2021/12/25
 * @apiNote
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplyCountDTO {

    /**
     * 评论id
     */
    private Integer commentId;

    /**
     * 回复数量
     */
    private Integer replyCount;

}
