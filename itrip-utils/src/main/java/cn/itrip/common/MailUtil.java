package cn.itrip.common;

import com.sun.mail.util.MailSSLSocketFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.Properties;

/**
 * @description:
 * @author: zeng
 * @createDate: 2019-12-12
 * @version: v1.0
 */
public class MailUtil {

    private static String account = "793439348@qq.com"; // 发信人邮箱账号
    private static String authorizationCode = "qgtvecagxqvgbejf"; // 发信人授权码
    private static String password = ""; // 发信人邮箱密码
    private static String receiveMailAccount = "1774158262@qq.com"; // 收信人邮箱

    // 初始化发信人信息
//    static {
//        account = PropertiesUtils.get("database.properties", "mail.account");
//        password = PropertiesUtils.get("database.properties", "mail.password");
//    }

    public static boolean sendMail() {
        try {
            // 1.创建连接对象javax.mail.Session
            Session session;
            //创建properties对象，封装连接所需对象
            Properties properties = new Properties();
            properties.setProperty("mail.smtp.host", "smtp.qq.com"); // 发件人的邮箱的 SMTP 服务器地址
            properties.setProperty("mail.smtp.auth", "true");  // 打开认证
            properties.setProperty("mail.transport.protocol", "smtp");   // 设置使用的协议
            //qq邮箱设置所需信息，163邮箱不需要
            if (receiveMailAccount.substring(receiveMailAccount.length() - 7).equalsIgnoreCase("@qq.com")) {
                MailSSLSocketFactory sf = new MailSSLSocketFactory();
                sf.setTrustAllHosts(true);
                properties.put("mail.smtp.ssl.enable", "true");
                properties.put("mail.smtp.ssl.socketFactory", sf);
                session = Session.getDefaultInstance(properties, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(account, authorizationCode);
                    }
                });
            } else {
                // 163 邮箱
                session = Session.getDefaultInstance(properties);
            }
            // 2.创建邮件对象javax.mail.Message
            Message message = createMimeMessage(session, account, receiveMailAccount);
            // 3.发送邮箱
            Transport transport = session.getTransport();
            transport.connect(account, password);
            transport.sendMessage(message, message.getAllRecipients());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static MimeMessage createMimeMessage(Session session, String sendMail, String receiveMail)
          throws Exception {
        // 1. 创建一封邮件
        MimeMessage message = new MimeMessage(session);
        // 2. From: 发件人
        message.setFrom(new InternetAddress(sendMail, "爱旅行", "UTF-8"));
        // 3. To: 收件人
        message.setRecipient(MimeMessage.RecipientType.TO,
              new InternetAddress(receiveMail, "XX用户", "UTF-8"));
        // 4. Subject: 邮件主题
        message.setSubject("邮件测试", "UTF-8");
        // 5. Content: 邮件正文
        message.setContent("用户你好。。。", "text/html;charset=UTF-8");
        // 6. 设置发件时间
        message.setSentDate(new Date());
        // 7. 保存设置
        message.saveChanges();
        return message;
    }

    public static void main(String[] args) {
//        Properties properties = System.getProperties();
//        System.out.println(properties);
        System.out.println(sendMail());

    }
}
