package tcc.uff.resource.server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import tcc.uff.resource.server.model.enums.DayOfWeek;
import tcc.uff.resource.server.model.enums.Frequency;
import tcc.uff.resource.server.model.response.GenericOption;
import tcc.uff.resource.server.model.response.SelectOption;
import tcc.uff.resource.server.model.response.models.DaysOfWeeks;
import tcc.uff.resource.server.model.response.models.Frequencies;
import tcc.uff.resource.server.model.response.models.Periods;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

@RestController
@RequestMapping("/models")
public class ModelsController {

    @GetMapping("/daysOfWeek")
    public @ResponseBody DaysOfWeeks getDaysOfWeek() {

        var days = DayOfWeek.getAllAttrbsSortedById();

        var daysResponse = new ArrayList<GenericOption>();

        days.forEach(day ->
                daysResponse.add(GenericOption.builder()
                        .id(day.getId())
                        .name(day.getName())
                        .build())
        );

        return DaysOfWeeks.builder()
                .daysOfWeek(daysResponse)
                .build();
    }

    @GetMapping("/periods")
    public @ResponseBody Periods getPeriods() {

        var format = "%s.%s";

        var year = LocalDate.now().getYear();

        return Periods.builder()
                .periods(Arrays.asList(
                        SelectOption.builder().option(String.format(format, year, 1)).build(),
                        SelectOption.builder().option(String.format(format, year, 2)).build()
                )).build();

    }

    @GetMapping("/frequencies")
    public @ResponseBody Frequencies getFrequencies() {

        var frequencies = Frequency.getAllAttrbsSortedById();

        var frequenciesResponse = new ArrayList<GenericOption>();

        frequencies.forEach(frequency ->
                frequenciesResponse.add(GenericOption.builder()
                        .id(frequency.getId())
                        .name(frequency.getName())
                        .description(frequency.getDescription())
                        .build())
        );

        return Frequencies.builder()
                .frequencies(frequenciesResponse)
                .build();
    }
}
