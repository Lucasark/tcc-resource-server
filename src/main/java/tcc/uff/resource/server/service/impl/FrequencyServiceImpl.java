package tcc.uff.resource.server.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tcc.uff.resource.server.exceptions.TempException;
import tcc.uff.resource.server.model.document.CourseDocument;
import tcc.uff.resource.server.model.document.FrequencyDocument;
import tcc.uff.resource.server.model.document.UserAlias;
import tcc.uff.resource.server.model.document.UserDocument;
import tcc.uff.resource.server.model.response.FrequencyCreateResponse;
import tcc.uff.resource.server.model.response.entity.FrequencyMapperResponse;
import tcc.uff.resource.server.model.response.entity.FrequencyResponse;
import tcc.uff.resource.server.repository.CourseRepository;
import tcc.uff.resource.server.repository.FrequencyRepository;
import tcc.uff.resource.server.service.mongooperations.MongoOperationsService;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class FrequencyServiceImpl {

    private final CourseRepository courseRepository;

    private final FrequencyRepository frequencyRepository;

    private final MongoOperationsService mongoOperationsService;

    public FrequencyCreateResponse initFrenquency(String course, Instant date) throws RuntimeException {

        var courseDocument = courseRepository.findById(course)
                .orElseThrow(() -> new RuntimeException("N achou Classe!"));

        var frequencyNew = FrequencyDocument.builder()
                .course(courseDocument)
                .date(date)
                .build();

        frequencyRepository.save(frequencyNew);

        mongoOperationsService.addInSet("id", course, "frequencies", frequencyNew.getId(), CourseDocument.class);

        return FrequencyCreateResponse.builder()
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

    public List<FrequencyDocument> allActivedFrequencyByCourse(String courseId) {

        var course = courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Curso n existe!"));

        return frequencyRepository.findAllIdAndFinished(course.getFrequencies(), Boolean.TRUE);

    }

    public boolean isFrequencyFinishedByDate(String courseId, Instant date) {
        var frequencyOptional = frequencyRepository.findByDateAndCourseId(date, courseId);

        if (frequencyOptional.isEmpty()) {
            return false;
        }

        return frequencyOptional.get().getFinished();
    }

    public List<FrequencyMapperResponse> getFrequencies(String courseId, Instant start, Instant end) {
        var course = courseRepository.findById(courseId)
                .orElseThrow(() -> new TempException("Curso n encontrado!"));

        Map<String, FrequencyMapperResponse> mapResponse = new HashMap<>();

        for (UserDocument userDocument : course.getMembers()) {
            var value = FrequencyMapperResponse.builder().id(userDocument.getEmail());

            var alias = userDocument.getAliases().stream()
                    .filter(userAlias -> userAlias.getCourseId().equals(courseId))
                    .map(UserAlias::getName)
                    .findFirst()
                    .orElse("S/A");

            value.alias(alias);

            mapResponse.put(userDocument.getEmail(), value.build());
        }

        //TODO: Pelo Pai, Do Filho e Do Espirito Santo...On^3 tranformar em Mapperes

        frequencyRepository.findByDateBetweenAndCourseId(start, end, courseId).forEach(frequencyDocument -> {
                    frequencyDocument.getAttendances().forEach(attendance -> {
                                mapResponse.get(attendance.getStudent().getEmail()).getFrequencies()
                                        .add(FrequencyResponse.builder()
                                                .id(frequencyDocument.getId())
                                                .date(frequencyDocument.getDate())
                                                .status(attendance.getStatus().getId())
                                                .build());
                            }
                    );
                }
        );

        return new ArrayList<>(mapResponse.values());
    }
}
