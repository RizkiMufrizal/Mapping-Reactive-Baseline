package org.rizki.mufrizal.baseline.mapper.object.server.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Builder
@Getter
@Setter
public class GeneralServerResponse implements Serializable {
    private Object body;
    private Integer httpStatus;
}