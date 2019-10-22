package com.alibaba.otter.canal.parse;

import com.alibaba.otter.canal.parse.support.AuthenticationInfo;

/**
 * 支持可切换的数据复制控制器
 * 
 * @author jianghang 2012-6-26 下午05:41:43
 * @version 1.0.0
 */
public interface CanalHASwitchable {

    void doSwitch();

    void doSwitch(AuthenticationInfo newAuthenticationInfo);
}
