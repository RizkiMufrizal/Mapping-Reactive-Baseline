package org.rizki.mufrizal.baseline.mapper.harmonized;

import org.rizki.mufrizal.baseline.mapper.object.server.response.GeneralServerResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class HarmonizedMapper {

    List<Harmonized> initHarmonized() {
        List<Harmonized> harmonizeds = new ArrayList<>();
        harmonizeds.add(Harmonized.builder()
                .backend("BE")
                .backendCode("200")
                .message("Sukses")
                .code("00")
                .httpStatus(200)
                .isError(false)
                .build());
        harmonizeds.add(Harmonized.builder()
                .backend("BE")
                .backendCode("404")
                .message("Invalid Account Id")
                .code("404")
                .httpStatus(404)
                .isError(true)
                .build());
        return harmonizeds;
    }

    public Mono<?> getHarmonized(String backend, String backendCode) {
        Optional<Harmonized> harmonized = initHarmonized().stream().parallel().filter(h -> h.getBackend().equals(backend) && h.getBackendCode().equals(backendCode)).findAny();
        if (harmonized.isPresent()) {
            if (!harmonized.get().getIsError()) {
                return Mono.just(harmonized.get());
            }
            return Mono.just(this.errorHarmonized(harmonized.get()));
        }
        return Mono.just(this.defaultHarmonized());
    }

    private GeneralServerResponse defaultHarmonized() {
        return GeneralServerResponse.builder()
                .body(GeneralHarmonizedResponse.builder()
                        .code("500")
                        .description("General Error")
                        .build()
                )
                .httpStatus(500)
                .build();
    }

    private GeneralServerResponse errorHarmonized(Harmonized harmonized) {
        return GeneralServerResponse.builder()
                .body(GeneralHarmonizedResponse.builder()
                        .code(harmonized.getCode())
                        .description(harmonized.getMessage())
                        .build()
                )
                .httpStatus(harmonized.getHttpStatus())
                .build();
    }

    public GeneralServerResponse successHarmonized(Object object, Integer httpStatus) {
        return GeneralServerResponse.builder()
                .body(object)
                .httpStatus(httpStatus)
                .build();
    }
}
