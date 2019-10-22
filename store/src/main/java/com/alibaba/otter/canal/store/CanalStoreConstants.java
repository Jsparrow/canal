package com.alibaba.otter.canal.store;

/**
 * 常量值
 * 
 * @author jianghang 2012-6-14 下午09:40:33
 * @version 1.0.0
 */
public interface CanalStoreConstants {

    String CODE_POSITION_NOT_FOUND    = "position:%s not found";

    String CODE_POSITION_NOT_IN_ORDER = "position:%s not in order";

    String ENCODING                   = "utf8";

    int    MAX_STORECOUNT             = 100;

    int    ROLLOVERCOUNT              = 100;

}
