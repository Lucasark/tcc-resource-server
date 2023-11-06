package tcc.uff.resource.server.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum FrequencyEnum {

    PRESENT(1, "P", "Presente"),
    MISS(2, "F", "Faltou"),
    UNDEFINED(3, "I", "Indefinido"),
    JUSTIFY(4, "J", "Justificado");

    private final Integer id;

    private final String name;

    private final String description;

    public static List<FrequencyEnum> getAllAttrbsSortedById() {

        return Arrays.stream(FrequencyEnum.values())
                .collect(Collectors.toSet())
                .stream()
                .sorted(Comparator.comparing(FrequencyEnum::getId))
                .collect(Collectors.toList());
    }

}
