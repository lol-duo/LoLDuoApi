package com.lolduo.duo.dto.timeline;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Getter
@NoArgsConstructor
public class EventDto {
    private Long itemId;
    private Long participantId;
    private String type;
}
