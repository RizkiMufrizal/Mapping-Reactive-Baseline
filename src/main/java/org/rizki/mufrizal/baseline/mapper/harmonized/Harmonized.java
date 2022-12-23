package org.rizki.mufrizal.baseline.mapper.harmonized;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Builder
@Getter
@Setter
public class Harmonized implements Serializable {

    private String backend;
    private String backendCode;
    private Integer httpStatus;
    private String message;
    private String code;
    private Boolean isError;

}