package com.github.service;

/**
 * 短信发送接口
 *
 * @author 许大仙
 * @version 1.0
 * @since 2022-07-15 15:13:32
 */
public interface SmsService {

    /**
     * 发送短信
     *
     * @param mobile 手机号码
     * @param msg    短信内容
     */
    void sendSms(String mobile, String msg);
}
