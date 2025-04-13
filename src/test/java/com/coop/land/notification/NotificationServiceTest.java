package com.coop.land.notification;

import com.coop.domain.notification.entity.Notification;
import com.coop.domain.notification.entity.NotificationRecipient;
import com.coop.domain.notification.enums.NotificationTarget;
import com.coop.domain.notification.repository.NotificationRecipientRepository;
import com.coop.domain.notification.service.NotificationService;
import com.coop.global.common.enums.ErrorCode;
import com.coop.global.exception.error.NotFoundException;
import com.coop.land.util.TestUtils;
import com.coop.presentation.notification.dto.response.NotificationResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @InjectMocks
    private NotificationService notificationService;

    @Mock
    private NotificationRecipientRepository notificationRecipientRepository;

    private final Long memberId = 1L;
    private final Long notificationId = 10L;

    @Test
    void readNotification_성공() {
        Notification mockNotification = TestUtils.createEntity(Notification.class, Map.of(
                "id", notificationId,
                "target", NotificationTarget.COMMENT
        ));
        NotificationRecipient mockRecipient = TestUtils.createEntity(NotificationRecipient.class, Map.of(
                "id", 100L,
                "notification", mockNotification,
                "toMemberId", memberId
        ));

        when(notificationRecipientRepository.findByToMemberIdAndNotificationId(memberId, notificationId))
                .thenReturn(Optional.of(mockRecipient));

        NotificationResponse result = notificationService.readNotification(memberId, notificationId);

        assertNotNull(result);
        assertEquals(notificationId, result.notificationId()); // 혹은 NotificationResponse 안 필드 맞게 수정
        verify(notificationRecipientRepository).findByToMemberIdAndNotificationId(memberId, notificationId);
    }


    @Test
    void readNotification_존재하지않을때_예외() {
        when(notificationRecipientRepository.findByToMemberIdAndNotificationId(memberId, notificationId))
                .thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                notificationService.readNotification(memberId, notificationId));

        assertEquals(ErrorCode.NOTIFICATION_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void readAllNotifications_성공() {
        Notification noti1 = TestUtils.createEntity(Notification.class, Map.of(
                "id", 1L,
                "target", NotificationTarget.COMMENT
        ));
        Notification noti2 = TestUtils.createEntity(Notification.class, Map.of(
                "id", 2L,
                "target", NotificationTarget.COMMENT
        ));

        NotificationRecipient mock1 = TestUtils.createEntity(NotificationRecipient.class, Map.of(
                "id", 1L, "notification", noti1));
        NotificationRecipient mock2 = TestUtils.createEntity(NotificationRecipient.class, Map.of(
                "id", 2L, "notification", noti2));
        List<NotificationRecipient> mockList = List.of(mock1, mock2);

        when(notificationRecipientRepository.findAllByToMemberIdOrderByIdDesc(memberId))
                .thenReturn(mockList);

        List<NotificationResponse> responses = notificationService.readAllNotifications(memberId);

        assertEquals(2, responses.size());
    }


    @Test
    void markAsRead_성공() {
        List<Long> ids = List.of(1L, 2L);
        NotificationRecipient mock1 = TestUtils.createEntity(NotificationRecipient.class, Map.of("id", 1L));
        NotificationRecipient mock2 = TestUtils.createEntity(NotificationRecipient.class, Map.of("id", 2L));
        List<NotificationRecipient> mockList = List.of(mock1, mock2);

        when(notificationRecipientRepository.findAllByToMemberIdAndNotification_IdIn(memberId, ids))
                .thenReturn(mockList);

        notificationService.markAsRead(memberId, ids);

        verify(notificationRecipientRepository).findAllByToMemberIdAndNotification_IdIn(memberId, ids);
    }

    @Test
    void markAsRead_일부누락_예외() {
        List<Long> ids = List.of(1L, 2L);
        NotificationRecipient mock1 = TestUtils.createEntity(NotificationRecipient.class, Map.of("id", 1L));
        List<NotificationRecipient> mockList = List.of(mock1); // 하나만 조회됨

        when(notificationRecipientRepository.findAllByToMemberIdAndNotification_IdIn(memberId, ids))
                .thenReturn(mockList);

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                notificationService.markAsRead(memberId, ids));

        assertEquals(ErrorCode.NOTIFICATION_NOT_FOUND, exception.getErrorCode());
    }
}
