package sni.common.services;

public interface MailSender
{
    void sendMail(String receiver, String title, String message);
}
