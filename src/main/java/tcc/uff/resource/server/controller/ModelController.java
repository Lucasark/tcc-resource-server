package tcc.uff.resource.server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tcc.uff.resource.server.model.enums.AttendanceEnum;
import tcc.uff.resource.server.model.response.GenericOption;
import tcc.uff.resource.server.model.response.SelectOption;
import tcc.uff.resource.server.model.response.models.Frequencies;
import tcc.uff.resource.server.model.response.models.Periods;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

@RestController
@RequestMapping("/models")
public class ModelController {

    @GetMapping("/periods")
    public Periods getPeriods() {

        var format = "%s.%s";

        var year = LocalDate.now().getYear();

        return Periods.builder()
                .periods(Arrays.asList(
                        SelectOption.builder().option(String.format(format, year, 1)).build(),
                        SelectOption.builder().option(String.format(format, year, 2)).build()
                )).build();

    }

    @GetMapping("/frequencies")
    public Frequencies getFrequencies() {

        var frequencies = AttendanceEnum.getAllAttrbsSortedById();

        var frequenciesResponse = new ArrayList<GenericOption>();

        frequencies.forEach(attendanceEnum ->
                frequenciesResponse.add(GenericOption.builder()
                        .id(attendanceEnum.getId())
                        .name(attendanceEnum.getName())
                        .description(attendanceEnum.getDescription())
                        .build())
        );

        return Frequencies.builder()
                .frequencies(frequenciesResponse)
                .build();
    }
}
