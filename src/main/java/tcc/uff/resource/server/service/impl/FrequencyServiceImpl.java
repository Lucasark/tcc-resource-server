package tcc.uff.resource.server.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tcc.uff.resource.server.model.document.FrequencyDocument;
import tcc.uff.resource.server.model.handler.AttendenceHandler;
import tcc.uff.resource.server.model.response.entity.FrequencyResponse;
import tcc.uff.resource.server.repository.CourseRepository;
import tcc.uff.resource.server.repository.FrequencyRepository;
import tcc.uff.resource.server.utils.GenerateString;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class FrequencyServiceImpl {

    @Autowired
    private Map<String, AttendenceHandler> attendences = new HashMap<>();

    private final CourseRepository courseRepository;
    private final FrequencyRepository frequencyRepository;

    public FrequencyResponse initFrenquency(String course, OffsetDateTime date) {

        var code = GenerateString.generateRandomString(10);

        var courseDocument = courseRepository.findById(course)
                .orElseThrow(() -> new RuntimeException("N achou Classe!"));

        var frequencyNew = FrequencyDocument.builder()
                .course(courseDocument)
                .date(date.toInstant())
                .build();

        frequencyRepository.save(frequencyNew);

        var handler = AttendenceHandler.builder()
                .id(new ObjectId().toString())
                .date(date.toInstant())
                .code(code)
                .build();

        attendences.put(courseDocument.getId(), handler);

        return FrequencyResponse.builder()
                .id(handler.getId())
                .date(date)
                .code(handler.getCode())
                .course(courseDocument.getId())
                .build();
    }

    public boolean isTeacherInFrequency(String teacher, String frequencyId) {
        var frequecy = frequencyRepository.findById(frequencyId)
                .orElseThrow(() -> new RuntimeException("Frequencia n existe"));

        return frequecy.getCourse().getTeacher().getEmail().equals(teacher);
    }
}
