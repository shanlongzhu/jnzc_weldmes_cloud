package com.gw.email.controller;

import com.gw.common.HttpResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @Author LiZhengKai
 * @Date 2021/8/13 18:00
 * @Description
 */

@RestController
@RequestMapping(value = "send")
public class EmailController {

    @PostMapping
    public HttpResult send(String from, String to, String subject, String text, String picture, String file){
        try {
            Properties properties = new Properties();
            // 设置 smtp 主机
            properties.put( "mail.smtp.host" , "smtp.qq.com" );
            // 使用 smtp 身份验证
            properties.put( "mail.smtp.auth" , "true" );
            MimeMessage message = new MimeMessage(Session.getInstance(properties, new Authenticator() {
                public PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication( "603453103@qq.com" , "ionfpprfyvfqbdif" );
                }
            }));
            String [] tos=to.split(",");
            List<String> list=new ArrayList<>();
            for (int i = 0; i <tos.length ; i++) {
                String a = String.valueOf(tos[i]);
                list.add(a);
            }
            for (int i = 0; i <list.size() ; i++) {
                // 关系正文和图片的
                MimeMultipart mm = new MimeMultipart();
                // 附件与正文（ text 和 img ）的关系
                MimeMultipart mm2 = new MimeMultipart();
                //收件人
                message.setRecipient(Message.RecipientType.TO, new InternetAddress(list.get(i)));
                // 发件人
                message.setFrom(from);
                // 邮件标题
                message.setSubject(subject);
                //创建邮件的正文
                MimeBodyPart texts = new MimeBodyPart();
                // setContent(“ 邮件的正文内容 ”,” 设置邮件内容的编码方式 ”)
                texts.setContent(text + "<img src='cid:a'>", "text/html;charset=gb2312");
                mm.addBodyPart(texts);
                if(picture==null&&file==null){
                    message.setContent(mm);
                    message.saveChanges();
                    Transport.send(message);
                }else if(picture!=null&&file==null) {
                    MimeBodyPart img = new MimeBodyPart();
                    DataHandler dh = new DataHandler(new FileDataSource(picture));
                    img.setDataHandler(dh);
                    // 创建图片的一个表示用于显示在邮件中显示
                    img.setContentID("a");
                    mm.addBodyPart(img);
                    mm.setSubType("related");
                    MimeBodyPart all = new MimeBodyPart();
                    all.setContent(mm);
                    mm2.addBodyPart(all);
                    message.setContent(mm2);
                    message.saveChanges();
                    Transport.send(message);
                } else if (picture==null&&file!=null) {
                    // 创建附件
                    MimeBodyPart attch = new MimeBodyPart();
                    DataHandler dh1 = new DataHandler(new FileDataSource(file));
                    attch.setDataHandler(dh1);
                    String filename1 = dh1.getName();
                    //防止中文乱码问题
                    attch.setFileName(MimeUtility.encodeText(filename1));
                    mm2.addBodyPart(attch);
                    mm2.setSubType("mixed");
                    message.setContent(mm2);
                    message.saveChanges();
                    Transport.send(message);
                }else if(picture!=null&&file!=null){
                    MimeBodyPart img = new MimeBodyPart();
                    DataHandler dh = new DataHandler(new FileDataSource(picture));
                    img.setDataHandler(dh);
                    img.setContentID("a");
                    mm.addBodyPart(img);
                    mm.setSubType("related");
                    MimeBodyPart all = new MimeBodyPart();
                    all.setContent(mm);
                    mm2.addBodyPart(all);
                    MimeBodyPart attch = new MimeBodyPart();
                    DataHandler dh1 = new DataHandler(new FileDataSource(file));
                    attch.setDataHandler(dh1);
                    String filename1 = dh1.getName();
                    attch.setFileName(MimeUtility.encodeText(filename1));
                    mm2.addBodyPart(attch);
                    mm2.setSubType("mixed");
                    message.setContent(mm2);
                    message.saveChanges();
                    Transport.send(message);
                }
            }
            return HttpResult.ok();
        } catch (Exception e) {
            return HttpResult.error();
        }
    }
}

