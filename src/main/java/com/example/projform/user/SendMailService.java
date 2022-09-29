package com.example.projform.user;

import lombok.AllArgsConstructor;
import net.glxn.qrgen.javase.QRCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.imageio.ImageIO;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Optional;
import java.util.Properties;

@Service
@AllArgsConstructor
public class SendMailService {

    @Autowired
    private  JavaMailSender javaMailSender;
    @Autowired
    private final UserRepository userRepository;

    private final AppConfig appConfig;
    private static final Logger logger = LoggerFactory.getLogger(SendMailService.class);

    private static String imagesPath= "C:\\Users\\Simo\\Documents\\JavaDev\\projform\\src\\main\\java\\com\\example\\projform\\imgs\\";
    public void sendMail(User user){

        Properties properties = System.getProperties();

        Session session = Session.getDefaultInstance(properties);

        session.setDebug(true);

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("momoouadii1998@gmail.com"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
            message.setSubject("test application form");
            // todo:: add email form to send to user
            getQrCodeForUser(user.getId());
            String html = "<div>" +
                    "<h3>Hello " +user.getFirst_name()+" "+user.getLast_name()+"</h3>"+
                    "<br/><p>this email is for confirmation , check your Qr code to know your id</p>"+
                    "<img src='"+appConfig.getUrl()+"/api/v1/qr/"+ user.getId()+"'/>"+
//                    "<img src= 'data:image/gif;base64,"+getQrCodeForUserData(user.getId())+"' width='250' height='250' alt='embedded folder icon' />"+
                    "<a href = '"+appConfig.getUrl()+"/api/v1/qr/"+user.getId()+"'>Qr here</a>"+
                    "</div>";

            Multipart multiPart = new MimeMultipart();

//            MimeBodyPart textPart = new MimeBodyPart();
            MimeBodyPart htmlPart = new MimeBodyPart();

            htmlPart.setContent(html, "text/html; charset=utf-8");
            htmlPart.setHeader("Cache-Control","<no-cache>");
            MimeBodyPart qrImage = new MimeBodyPart();

            byte[] imageBytes = ((DataBufferByte) getQrCodeForUser(user.getId()).getData().getDataBuffer()).getData();

            ByteArrayDataSource bds = new ByteArrayDataSource(imageBytes, "image/png");
            qrImage.setDataHandler(new DataHandler(bds));
            qrImage.setFileName("qrImage_"+user.getCode().substring(0,8)+".png");
            qrImage.setHeader("Content-ID", "<image>");
            qrImage.setHeader("Content-Type","image/png");

//            File file = new File(imagesPath+user.getId()+".png");
//            qrImage.attachFile(file);
//            multiPart.addBodyPart(textPart);

            multiPart.addBodyPart(htmlPart);
            multiPart.addBodyPart(qrImage);

            message.setContent(multiPart);

            logger.info("Sending...");
            javaMailSender.send(message);
            logger.info("Sent message successfully....");

        } catch (MessagingException e) {
            logger.error("Sent message not successful....");
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
    public BufferedImage getQrCodeForUser(Long id) throws Exception {
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
        logger.info(user.get().getCode());
        ByteArrayOutputStream stream = QRCode
                .from(user.get().getCode())
                .withSize(250, 250)
                .stream();

        ByteArrayInputStream bis = new ByteArrayInputStream(stream.toByteArray());
//        BufferedImage qrImage =
                return ImageIO.read(bis);
        }else{
            return null;
        }
//        ImageIO.write( qrImage, "png", new File(imagesPath+user.getCode().substring(0,8)+".png") );
//        return ImageIO.read(bis);
    }
//    public ByteArrayOutputStream getQrCodeForUserData(Long id) throws Exception {
//        Optional<User> user = userRepository.findById(id);
//
//        logger.info(user.get().getCode());
//        ByteArrayOutputStream stream = QRCode
//                .from(user.get().getCode())
//                .withSize(250, 250)
//                .stream();
//
////        ByteArrayInputStream bis = new ByteArrayInputStream(stream.toByteArray());
////        BufferedImage qrImage =
//        return stream;
////        ImageIO.write( qrImage, "png", new File(imagesPath+user.getCode().substring(0,8)+".png") );
////        return ImageIO.read(bis);
//    }




}
