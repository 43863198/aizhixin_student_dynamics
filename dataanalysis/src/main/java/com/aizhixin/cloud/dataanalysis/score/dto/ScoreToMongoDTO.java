package com.aizhixin.cloud.dataanalysis.score.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@ToString
public class ScoreToMongoDTO {
    @Getter @Setter private int count = 0;
    @Getter @Setter private int bkcount = 0;
    @Getter @Setter private double totalXF = 0.0;
    @Getter @Setter private Set<String> kchs = new HashSet<>();
    @Getter @Setter private StringBuilder source = new StringBuilder();
    @Getter @Setter private StringBuilder source2 = new StringBuilder();
}
