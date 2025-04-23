package com.coop.domain.notification.repository;

import com.coop.domain.notification.entity.NotificationRecipient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NotificationRecipientRepository extends JpaRepository<NotificationRecipient, Long> {

    @Query("SELECT r FROM NotificationRecipient r WHERE r.toMemberId = :toMemberId AND r.notification.id = :notificationId")
    Optional<NotificationRecipient> findByToMemberIdAndNotificationId(Long toMemberId, Long notificationId);

    List<NotificationRecipient> findAllByToMemberIdOrderByIdDesc(Long toMemberId);

    List<NotificationRecipient> findAllByToMemberIdAndNotification_IdIn(Long toMemberId, List<Long> notificationIds);
}
