package com.gw.email.controller;

import com.gw.common.HttpResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author LiZhengKai
 * @Date 2021/7/27 18:00
 * @Description
 */

@RestController
@RequestMapping(value = "send")
public class EmailController {
    @Autowired
    private JavaMailSender mailSender;

    @GetMapping
    private HttpResult send(String from, String to, String subject, String text){
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            String [] tos=to.split(",");
            List<String> list=new ArrayList<>();
            for (int i = 0; i <tos.length ; i++) {
                String a = String.valueOf(tos[i]);
                list.add(a);
            }
            for (int i = 0; i <list.size() ; i++) {
                //收件人
                message.setTo(list.get(i));
                // 发件人
                message.setFrom(from);
                // 邮件标题
                message.setSubject(subject);
                // 邮件内容
                message.setText(text);
                mailSender.send(message);
            }
            return HttpResult.ok();
        } catch (Exception e) {
            return HttpResult.error();
        }
    }
}

