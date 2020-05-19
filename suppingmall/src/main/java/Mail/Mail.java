package Mail;

import com.supshop.suppingmall.user.User;
import lombok.*;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.internet.MimeMessage;

@Setter @Getter
@ToString @Builder
@NoArgsConstructor @AllArgsConstructor
public class Mail {

    private MimeMessage message;
    private MimeMessageHelper messageHelper;
    private User receiver;
    private String sendDate;
    private String verifyKey;

}
