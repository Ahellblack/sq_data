package com.siti.wisdomhydrologic.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2019/8/13.
 */
public class StationIdUtils {
    /**
     * 获取写入的基本站StationName
     */
    public static List<String> getBasicStationList() {
        String[] NidArray = {"大团闸", "大治河东闸（闸内）", "芦潮港", "三甲港闸（闸内）", "邬家路桥", "五号沟闸（闸内）", "杨思闸（闸外）", "杨思闸（闸内）", "洋泾闸（闸内）", "周浦", "祝桥"};
        List<String> NidList = new ArrayList<>();
        for (String stationId : NidArray) {
            NidList.add(stationId);
        }
        return NidList;
    }
}