package org.rizki.mufrizal.baseline.mapper.harmonized;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GeneralHarmonizedResponse implements Serializable {
    @JsonProperty("Code")
    private String code;
    @JsonProperty("Description")
    private String description;
}