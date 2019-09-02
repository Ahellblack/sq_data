package com.siti.wisdomhydrologic.datepull.controller;

import com.siti.wisdomhydrologic.datepull.entity.RealFiveminSensorDataEntity;
import com.siti.wisdomhydrologic.datepull.mapper.RealFiveminSensorDataMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/fiveMinSensorData")
public class fiveMinSensorDataCtrl {

    @Resource
    RealFiveminSensorDataMapper realFiveminSensorDataMapper;
    @GetMapping(value = "/getAll")
    public void getAll(){
        List<RealFiveminSensorDataEntity> data=realFiveminSensorDataMapper.selectAllFiveMinData();
    }

}
