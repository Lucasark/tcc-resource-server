package tcc.uff.resource.server.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum AttendanceEnum {

    PRESENT(1, "P", "Presente"),
    MISS(2, "F", "Faltou"),
    UNDEFINED(3, "I", "Indefinido"),
    JUSTIFY(4, "J", "Justificado");

    private final Integer id;

    private final String name;

    private final String description;

    public static List<AttendanceEnum> getAllAttrbsSortedById() {

        return Arrays.stream(AttendanceEnum.values())
                .collect(Collectors.toSet())
                .stream()
                .sorted(Comparator.comparing(AttendanceEnum::getId))
                .collect(Collectors.toList());
    }

    public static AttendanceEnum fromId(Integer type) {
        return Arrays.stream(values())
                .filter(file -> Objects.equals(file.getId(), type))
                .findFirst()
                .orElse(UNDEFINED);
    }

}
