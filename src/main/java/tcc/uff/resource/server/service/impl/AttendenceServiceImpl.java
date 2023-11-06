package tcc.uff.resource.server.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tcc.uff.resource.server.model.document.AttendenceDocument;
import tcc.uff.resource.server.model.document.FrequencyDocument;
import tcc.uff.resource.server.model.response.entity.AttendenceResponse;
import tcc.uff.resource.server.repository.AttendenceRepository;
import tcc.uff.resource.server.repository.CourseRepository;
import tcc.uff.resource.server.repository.FrequencyRepository;
import tcc.uff.resource.server.service.AttendenceService;
import tcc.uff.resource.server.utils.GenerateString;

import java.lang.module.ResolutionException;
import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class AttendenceServiceImpl implements AttendenceService {

    private final AttendenceRepository attendenceRepository;
    private final CourseRepository courseRepository;
    private final FrequencyRepository frequencyRepository;

    public AttendenceResponse createAttendence(String course, OffsetDateTime date) {

        var code = GenerateString.generateRandomString(10);

        var courseDocument = courseRepository.findById(course).orElseThrow(
                ResolutionException::new
        );

        var frequencyNew = FrequencyDocument.builder()
                .course(courseDocument)
                .date(date.toInstant())
                .build();

        frequencyRepository.save(frequencyNew);

        courseDocument.getFrequencies().add(frequencyNew);

        courseRepository.save(courseDocument);

        var attendenceDocument = attendenceRepository.save(AttendenceDocument.builder()
                .course(courseDocument)
                .date(date.toInstant())
                .code(code)
                .build());

        return AttendenceResponse.builder()
                .id(attendenceDocument.getId())
                .date(date)
                .code(attendenceDocument.getCode())
                .course(attendenceDocument.getCourse().getId())
                .build();

    }
}
