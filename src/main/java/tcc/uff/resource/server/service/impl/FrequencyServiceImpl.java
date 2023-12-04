package tcc.uff.resource.server.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tcc.uff.resource.server.model.document.CourseDocument;
import tcc.uff.resource.server.model.document.FrequencyDocument;
import tcc.uff.resource.server.model.response.entity.FrequencyResponse;
import tcc.uff.resource.server.repository.CourseRepository;
import tcc.uff.resource.server.repository.FrequencyRepository;
import tcc.uff.resource.server.service.mongooperations.MongoOperationsService;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class FrequencyServiceImpl {

    private final CourseRepository courseRepository;

    private final FrequencyRepository frequencyRepository;

    private final MongoOperationsService mongoOperationsService;

    public FrequencyResponse initFrenquency(String course, Instant date) throws RuntimeException {

        var courseDocument = courseRepository.findById(course)
                .orElseThrow(() -> new RuntimeException("N achou Classe!"));

        var frequencyNew = FrequencyDocument.builder()
                .course(courseDocument)
                .date(date)
                .build();

        frequencyRepository.save(frequencyNew);

        mongoOperationsService.addInSet("id", course, "frequencies", frequencyNew.getId(), CourseDocument.class);

        return FrequencyResponse.builder()
                .id(frequencyNew.getId())
                .date(date)
                .course(courseDocument.getId())
                .build();
    }

    public boolean isTeacherInFrequency(String teacher, String frequencyId) {
        var frequecy = frequencyRepository.findById(frequencyId)
                .orElseThrow(() -> new RuntimeException("Frequencia n existe"));

        return frequecy.getCourse().getTeacher().getEmail().equals(teacher);
    }

    public boolean isCourseHasActivedFrequency(CourseDocument courseDocument) {

        var frequencies = frequencyRepository.findAllById(courseDocument.getFrequencies());

        return frequencies.parallelStream().anyMatch(value -> Boolean.FALSE.equals(value.getFinished()));
    }

    public boolean isFrequencyFinishedByDate(String courseId, Instant date) {
        var frequencyOptional = frequencyRepository.findByDateAndCourseId(date, courseId);

        if (frequencyOptional.isEmpty()) {
            return false;
        }

        return frequencyOptional.get().getFinished();
    }
}
