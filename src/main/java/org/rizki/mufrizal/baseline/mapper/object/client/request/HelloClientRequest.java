package org.rizki.mufrizal.baseline.mapper.object.client.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HelloClientRequest implements Serializable {

    @JsonProperty("referce_number")
    private String referceNumber;

    @JsonProperty("message")
    private String message;

    @JsonProperty("hash")
    private String hash;
}