package com.github.service.impl;

import com.github.service.EmailService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-07-15 15:49:36
 */
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
@RequiredArgsConstructor
public class QqEmailServiceImpl implements EmailService {

    /**
     * 发送邮件的工具类
     */
    @NonNull
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String mailFrom;

    @Override
    public void sendEmail(String email, String msg) {
        // 建立一个简单的邮件结构
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        // 邮件的发送人
        mailMessage.setFrom(this.mailFrom);
        // 邮件的收件人
        mailMessage.setTo(email);
        // 主题
        mailMessage.setSubject("登录验证码");
        // 邮件内容
        mailMessage.setText(msg);
        // 发送时间
        mailMessage.setSentDate(new Date());
        // 发送邮件
        this.mailSender.send(mailMessage);
    }
}
