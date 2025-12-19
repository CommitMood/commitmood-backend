package com.ssafy.commitmood.domain.common.entity;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NonNull;

@Getter
public abstract class BaseTimeEntity {

    @NonNull
    private LocalDateTime createdAt;
    @NonNull
    private LocalDateTime updatedAt;
}
