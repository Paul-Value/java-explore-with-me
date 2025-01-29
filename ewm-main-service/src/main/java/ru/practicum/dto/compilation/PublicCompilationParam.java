package ru.practicum.dto.compilation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PublicCompilationParam {
    private Boolean pinned;
    @Builder.Default
    private Integer from = 0;
    @Builder.Default
    private Integer size = 10;
}
