package com.siti.wisdomhydrologic.realmessageprocess.service;


import java.util.Map;

/**
 * Created by DC on 2019/8/26.
 *
 * @data ${DATA}-13:34
 */
public interface Algorithm<T,K,F> {
     F calculate(T vo, Map<Integer, T> data,K config);
}
