package sni.common.services;

import sni.common.models.entities.FileLogEntity;

public interface NotificationService
{
    void notifyOfAction(FileLogEntity logEntity);
}
