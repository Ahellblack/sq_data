package com.siti.wisdomhydrologic.analysis.pipeline;


import java.util.List;
import java.util.stream.IntStream;

/**
 * Created by DC on 2019/7/24.
 * @data ${DATA}-14:56
 *
 */
public  class PipelineValve extends AbstractProcessPipeline {

    @Override
    public void setHandler(Valve indecator) {
        super.setHandler(indecator);
    }

    @Override
    public void doInterceptor(List val) {
        IntStream.range(0, handlerChain.size()).forEach(i -> {
            handlerChain.get(i).beforeProcess(val);
        });
    }

}

