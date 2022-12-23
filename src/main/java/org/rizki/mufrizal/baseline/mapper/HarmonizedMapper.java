package org.rizki.mufrizal.baseline.mapper;

import org.rizki.mufrizal.baseline.mapper.object.GeneralHarmonizedResponse;
import org.rizki.mufrizal.baseline.mapper.object.Harmonized;
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

    public Mono<Harmonized> getHarmonized(String backendCode) {
        Optional<Harmonized> harmonized = initHarmonized().stream().filter(h -> h.getBackendCode().equals(backendCode)).findAny();
        return harmonized.map(Mono::just).orElseGet(Mono::empty);
    }

    public GeneralHarmonizedResponse defaultHarmonized() {
        return GeneralHarmonizedResponse.builder()
                .code("500")
                .description("General Error")
                .build();
    }

    public GeneralHarmonizedResponse errorHarmonized(Harmonized harmonized) {
        return GeneralHarmonizedResponse.builder()
                .code(harmonized.getCode())
                .description(harmonized.getMessage())
                .build();
    }
}
