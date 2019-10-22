package com.alibaba.otter.canal.meta.exception;

import com.alibaba.otter.canal.common.CanalException;

/**
 * @author zebin.xuzb @ 2012-6-21
 * @version 1.0.0
 */
public class CanalMetaManagerException extends CanalException {

    private static final long serialVersionUID = -654893533794556357L;

    public CanalMetaManagerException(String errorCode){
        super(errorCode);
    }

    public CanalMetaManagerException(String errorCode, Throwable cause){
        super(errorCode, cause);
    }

    public CanalMetaManagerException(String errorCode, String errorDesc){
        super(new StringBuilder().append(errorCode).append(":").append(errorDesc).toString());
    }

    public CanalMetaManagerException(String errorCode, String errorDesc, Throwable cause){
        super(new StringBuilder().append(errorCode).append(":").append(errorDesc).toString(), cause);
    }

    public CanalMetaManagerException(Throwable cause){
        super(cause);
    }

}
