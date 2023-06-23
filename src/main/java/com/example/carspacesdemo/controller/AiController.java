package com.example.carspacesdemo.controller;

import com.example.carspacesdemo.common.BaseResponse;
import com.example.carspacesdemo.common.ErrorCode;
import com.example.carspacesdemo.config.ThreadPoolExecutorConfig;
import com.example.carspacesdemo.exception.BusinessException;
import com.example.carspacesdemo.manager.AiManager;
import com.example.carspacesdemo.model.dto.Ai.AiRequest;
import com.example.carspacesdemo.model.dto.Ai.AiSureCreateCarSpaceRequest;
import com.example.carspacesdemo.model.entity.AiResponse;
import com.example.carspacesdemo.model.entity.User;
import com.example.carspacesdemo.service.CarSpaceService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

import static com.example.carspacesdemo.common.ResultUtils.success;
import static com.example.carspacesdemo.constant.UserConstants.USER_LOGIN_STATE;

@RestController
@RequestMapping("/Ai")
public class AiController {
    @Resource
    AiManager aiManager;
    @Resource
    CarSpaceService carSpacesService;
    @Resource
    private ThreadPoolExecutor threadPoolExecutor;

    @PostMapping("/CarSpaceCreate")
    public BaseResponse<AiResponse> AiCreateCarSpace(@RequestBody AiRequest aiRequest){
        String aiStr = aiRequest.getAiStr();
        if(aiStr.isEmpty()){
            throw new BusinessException(ErrorCode.AI_ERROR,"要求为空");
        }
        String res = aiManager.doChat(1672139171709337601L, aiStr);
        String[] splits = res.split("【【【【【");
        if (splits.length < 2) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, splits[0]);
        }
        String response = "AI将为您创建一个位于"+splits[0]+"的车位，小时价格是"+splits[1]+"，确定以继续";
        return success(new AiResponse(splits[0],Integer.parseInt(splits[1]),response));
    }
    @PostMapping("/CarSpaceCreateSure")
    public BaseResponse<String> AiCreateCarSpaceSure(@RequestBody AiSureCreateCarSpaceRequest aiSureCreateCarSpaceRequest, HttpServletRequest httpServletRequest){
        User user = (User)httpServletRequest.getSession().getAttribute(USER_LOGIN_STATE);
        if(user==null){
            throw new BusinessException(ErrorCode.LOGIN_ERROR,"用户未登录");
        }
        Long id = user.getUserId();
        if(id <=0){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"系统出现错误");
        }
        CompletableFuture.runAsync(()->{
            try {
                String location = aiSureCreateCarSpaceRequest.getLocation();
                int price = aiSureCreateCarSpaceRequest.getPrice();
                carSpacesService.AiCarSpaceCreate(id, location, price, "");
            }catch (Exception e){
                throw new BusinessException(ErrorCode.DAO_ERROR,"输出库出现错误");
            }
        });
        return success("已经通知去做啦");
    }
}
