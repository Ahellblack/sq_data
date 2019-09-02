package com.siti.wisdomhydrologic.datepull.service;

import com.siti.wisdomhydrologic.datepull.vo.DayVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by dell on 2019/7/18.
 */
@Service
public interface DayDataService {

    int addDayData(List<DayVo> dayVo);

    int addHourData(List<DayVo> hourVo);
}
