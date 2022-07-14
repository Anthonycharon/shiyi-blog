package com.shiyi.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TagDTO {
    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "名称")
    private String name;
}
