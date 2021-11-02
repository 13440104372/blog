package com.zbw.blog.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import java.util.Map;

@Component
public class MailUtil {
    @Value("${mail.fromMail.sender}")
    private String sender;

    private JavaMailSender javaMailSender;

    private TemplateEngine templateEngine;

    /**
     * 发送邮件
     * @param receiver 接收者
     * @param subject 主题
     * @param emailTemplate 邮件模板
     * @param dataMap 数据
     */
    public void sendTemplateMail(String receiver, String subject, String emailTemplate, Map<String, Object> dataMap) throws Exception {
        Context context = new Context();
        for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
            context.setVariable(entry.getKey(), entry.getValue());
        }
        String templateContent = templateEngine.process(emailTemplate, context);
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(sender);
        helper.setTo(receiver);
        helper.setSubject(subject);
        helper.setText(templateContent, true);
        javaMailSender.send(message);
    }

    @Autowired
    public void setJavaMailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Autowired
    public void setTemplateEngine(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }
}


