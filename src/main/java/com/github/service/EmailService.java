package com.github.service;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-07-15 15:49:01
 */
public interface EmailService {


    /**
     * 发送邮件
     *
     * @param email 手机号码
     * @param msg    邮件内容
     */
    void sendEmail(String email, String msg);
}
