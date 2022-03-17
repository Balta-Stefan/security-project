package sni.common.services.implementations;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import sni.common.models.entities.FileEntity;
import sni.common.models.entities.FileLogEntity;
import sni.common.services.MailSender;
import sni.common.services.NotificationService;
import sni.common.models.entities.UserEntity;
import sni.common.models.enums.Role;
import sni.common.repositories.UsersRepository;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService
{
    private final UsersRepository usersRepository;
    private final MailSender mailSender;


    public NotificationServiceImpl(UsersRepository usersRepository, MailSender mailSender)
    {
        this.usersRepository = usersRepository;
        this.mailSender = mailSender;
    }

    @Override
    @Async
    public void notifyOfAction(FileLogEntity logEntity)
    {
        List<UserEntity> admins = this.usersRepository.findAllByRole(Role.ADMIN);

        FileEntity affectedFile = logEntity.getAffectedFile();
        UserEntity user = logEntity.getCreatedBy();

        String title = "Obavještenje o uklonjenom fajlu";
        String message = "Uklonjeni fajl: (" + affectedFile.getFileId() + ")" + affectedFile.getName() +
                ", od strane korisnika: (" + user.getUserId() + ")" + user.getUsername();

        for(UserEntity admin : admins)
        {
            String receiver = admin.getEmail();

            this.mailSender.sendMail(receiver, title, message);
        }
    }
}
