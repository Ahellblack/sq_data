package com.siti.wisdomhydrologic.analysis.pipeline;

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

    void beforeProcess(List<T> val,Map<String,G> compare);

    void doProcess(Map<Integer, T> val,  Map<Integer, F> configMap);

    void doProcess(Map<Integer, T> val,  Map<Integer, F> configMap,LocalDateTime time,Map<String,G> compare);

}
