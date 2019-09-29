package com.siti.wisdomhydrologic.analysis.pipeline;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by DC on 2019/7/18.
 *
 * @data ${DATA}-11:49
 */
//抽象和实现分离
public abstract class AbstractProcessPipeline {

    public List<Valve> handlerChain = new ArrayList<>(20);

    /**
     * 添加handler
     */
    public void setHandler(Valve indecator) {
        handlerChain.add(indecator);
    }

    /**
     * 处理方法
     */
    public abstract void doInterceptor(List<Valve> val, Map previous) ;

    /**
     * 处理方法
     */
    public abstract void doInterceptor(List<Valve> val) ;


}
