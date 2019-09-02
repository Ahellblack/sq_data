package com.siti.wisdomhydrologic.datepull.service;

import com.siti.wisdomhydrologic.datepull.vo.TSDBVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by dell on 2019/7/23.
 */
@Service
public interface TSDBService {

    int insertTSDB(List<TSDBVo> list);
}
