package com.coop.domain.notification.service;

import com.coop.domain.notification.entity.NotificationRecipient;
import com.coop.domain.notification.repository.NotificationRecipientRepository;
import com.coop.global.common.enums.ErrorCode;
import com.coop.global.exception.error.NotFoundException;
import com.coop.presentation.notification.dto.response.NotificationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRecipientRepository notificationRecipientRepository;

    //단건조회 + 읽음 처리
    @Transactional
    public NotificationResponse readNotification(Long memberId, Long notificationId) {
        NotificationRecipient recipient = notificationRecipientRepository
                .findByToMemberIdAndNotificationId(memberId, notificationId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOTIFICATION_NOT_FOUND));
        recipient.markAsRead();
        return NotificationResponse.from(recipient);
    }

    //내 리뷰 전체 ID 내림차순 조회 + 읽음 처리 X
    public List<NotificationResponse> readAllNotifications(Long memberId) {
        List<NotificationRecipient> recipients = notificationRecipientRepository
                .findAllByToMemberIdOrderByIdDesc(memberId);
        return recipients.stream()
                .map(NotificationResponse::from)
                .toList();
    }

    //ID List 읽음 처리
    @Transactional
    public void markAsRead(Long memberId, List<Long> notificationIds) {
        List<NotificationRecipient> recipients = notificationRecipientRepository
                .findAllByToMemberIdAndNotification_IdIn(memberId, notificationIds);

        if (recipients.size() != notificationIds.size()) {
            throw new NotFoundException(ErrorCode.NOTIFICATION_NOT_FOUND);
        }
        recipients.forEach(NotificationRecipient::markAsRead);
    }
}
