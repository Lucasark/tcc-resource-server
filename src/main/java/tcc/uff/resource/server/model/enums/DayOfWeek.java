package tcc.uff.resource.server.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum DayOfWeek {

    MONDAY(1, "Segunda-feira"),

    TUESDAY(2, "Terça-feira"),

    WEDNESDAY(3, "Quarta-Feira"),

    THURSDAY(4, "Quinta-Feira"),

    FRIDAY(5, "Sexta-Feira"),

    SATURDAY(6, "Sábado"),

    SUNDAY(7, "Domingo");

    private final Integer id;

    private final String name;

    public static List<DayOfWeek> getAllAttrbsSortedById() {

        return Arrays.stream(DayOfWeek.values())
                .collect(Collectors.toSet())
                .stream()
                .sorted(Comparator.comparing(DayOfWeek::getId))
                .collect(Collectors.toList());


    }

}
