package com.lolduo.duo.dto.timeline;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class FrameDto {
    List<EventDto> events;
}
