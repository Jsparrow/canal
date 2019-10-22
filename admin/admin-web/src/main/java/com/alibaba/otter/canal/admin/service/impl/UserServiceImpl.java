package com.alibaba.otter.canal.admin.service.impl;

import java.security.NoSuchAlgorithmException;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.otter.canal.admin.common.exception.ServiceException;
import com.alibaba.otter.canal.admin.model.User;
import com.alibaba.otter.canal.admin.service.UserService;
import com.alibaba.otter.canal.protocol.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用户信息业务层
 *
 * @author rewerma 2019-07-13 下午05:12:16
 * @version 1.0.0
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	private static byte[] seeds = "canal is best!".getBytes();

    @Override
	public User find4Login(String username, String password) {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return null;
        }
        User user = User.find.query().where().eq("username", username).findOne();
        if (user == null) {
            throw new ServiceException(new StringBuilder().append("user:").append(username).append(" auth failed!").toString());
        }
        try {
            byte[] pass = SecurityUtil.scramble411(password.getBytes(), seeds);
            if (!SecurityUtil.scrambleServerAuth(pass, SecurityUtil.hexStr2Bytes(user.getPassword()), seeds)) {
                throw new ServiceException(new StringBuilder().append("user:").append(user.getName()).append(" passwd incorrect!").toString());
            }
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage(), e);
			throw new ServiceException(new StringBuilder().append("user:").append(user.getName()).append(" auth failed!").toString());
        }

        user.setPassword("");
        return user;
    }

    @Override
	public void update(User user) {
        User userTmp = User.find.query().where().eq("username", user.getUsername()).findOne();
        if (userTmp == null) {
            throw new ServiceException();
        }

        try {
            byte[] pass = SecurityUtil.scramble411(user.getOldPassword().getBytes(), seeds);
            if (!SecurityUtil.scrambleServerAuth(pass, SecurityUtil.hexStr2Bytes(userTmp.getPassword()), seeds)) {
                throw new ServiceException("old passwd is unmatch");
            }

            user.setId(userTmp.getId());
            user.setPassword(SecurityUtil.scrambleGenPass(user.getPassword().getBytes()));
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage(), e);
			throw new ServiceException("passwd process failed");
        }

        user.update("username", "nn:password");
    }
}
