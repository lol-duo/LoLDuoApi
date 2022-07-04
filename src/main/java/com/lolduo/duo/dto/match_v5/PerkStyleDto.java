package com.lolduo.duo.dto.match_v5;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PerkStyleDto {
    private String description;
    private List<PerkStyleSelectionDto> selections;
    private Long style;

    public PerkStyleDto(String description, List<PerkStyleSelectionDto> selections, Long style) {
        this.description = description;
        this.selections = selections;
        this.style = style;
    }
}
