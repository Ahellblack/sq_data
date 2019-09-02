package com.siti.wisdomhydrologic.datepull.service;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;

/**
 * Created by DC on 2019/7/8.
 *
 * @data ${DATA}-13:56
 */
public interface Observable {

    void watchQueueState();

}
