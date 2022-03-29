package com.shiyi.controller.api;

import com.shiyi.annotation.BusinessLog;
import com.shiyi.common.ApiResult;
import com.shiyi.entity.FeedBack;
import com.shiyi.service.FeedBackService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author blue
 * @date 2022/1/13
 * @apiNote
 */
@RestController
@RequestMapping("/web/feedback")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApiFeedBackController {

    private final FeedBackService feedBackService;


    @PostMapping(value = "/add")
    @ApiOperation(value = "添加反馈", httpMethod = "POST", response = ApiResult.class, notes = "添加反馈")
    @BusinessLog(value = "首页-用户添加反馈",type = "添加",desc = "添加反馈")
    public ApiResult save(@RequestBody FeedBack feedBack) {
        return  feedBackService.addFeedback(feedBack);
    }

}
