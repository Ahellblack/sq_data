package com.siti.wisdomhydrologic.analysis.pipeline;

import com.siti.wisdomhydrologic.analysis.entity.Real;
import com.siti.wisdomhydrologic.analysis.vo.RealVo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Created by DC on 2019/7/18.
 *
 * @data ${DATA}-14:47
 */
public interface Valve<T,F,G> {

    void beforeProcess(List<T> val);

    void doProcess(List <T> realData, List<F> beforeData, Map<Integer, G> configMap);

}
