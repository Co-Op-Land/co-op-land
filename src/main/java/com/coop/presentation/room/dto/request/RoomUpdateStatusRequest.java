package com.coop.presentation.room.dto.request;

import com.coop.domain.room.enums.Status;
import jakarta.validation.constraints.NotNull;

public record RoomUpdateStatusRequest(
        @NotNull
        Status status
) {
}
