package com.example.carspacesdemo.controller;

import com.example.carspacesdemo.common.BaseResponse;
import com.example.carspacesdemo.model.Carspace;
import com.example.carspacesdemo.service.CarSpacesService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

import static com.example.carspacesdemo.common.ResultUtils.success;

@RestController
@RequestMapping("/carSpaces")
public class CarSpacesController {
    @Resource
    private CarSpacesService carSpacesService;

    @RequestMapping("/getCarSpaces")
    public BaseResponse getCarSpaces(@RequestBody long userId){
        List<Carspace> carSpaces = carSpacesService.getCarSpaces();
        return success(carSpaces);
    }
}
