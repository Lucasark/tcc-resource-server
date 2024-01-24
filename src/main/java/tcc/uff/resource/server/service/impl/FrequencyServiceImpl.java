package tcc.uff.resource.server.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tcc.uff.resource.server.exceptions.TempException;
import tcc.uff.resource.server.model.document.Attendance;
import tcc.uff.resource.server.model.document.FrequencyDocument;
import tcc.uff.resource.server.model.document.UserAlias;
import tcc.uff.resource.server.model.document.UserDocument;
import tcc.uff.resource.server.model.enums.AttendanceEnum;
import tcc.uff.resource.server.model.response.FrequencyCreateResponse;
import tcc.uff.resource.server.model.response.entity.FrequencyMapperResponse;
import tcc.uff.resource.server.model.response.entity.FrequencyResponse;
import tcc.uff.resource.server.repository.CourseRepository;
import tcc.uff.resource.server.repository.FrequencyRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class FrequencyServiceImpl {

    private final CourseRepository courseRepository;

    private final FrequencyRepository frequencyRepository;

    public FrequencyCreateResponse initFrenquency(String courseId, Instant date) throws RuntimeException {

        var courseDocument = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("N achou Classe!"));

        Set<Attendance> attendances = new HashSet<>();

        courseDocument.getMembers().forEach(member ->
                attendances.add(Attendance.builder()
                        .student(member)
                        .build())
        );

        var frequencyNew = FrequencyDocument.builder()
                .course(courseDocument)
                .attendances(attendances)
                .date(date)
                .build();

        frequencyRepository.save(frequencyNew);

        courseRepository.addInSet(courseId, "frequencies", frequencyNew.getId());

        return FrequencyCreateResponse.builder()
                .id(frequencyNew.getId())
                .date(date)
                .course(courseDocument.getId())
                .build();
    }

    public void endFrenquecyByCourse(FrequencyDocument frequencyDocument) {
        frequencyDocument.setFinished(Boolean.TRUE);
        frequencyDocument.setFinishedAt(LocalDateTime.now());
        frequencyRepository.save(frequencyDocument);

        var attendances = frequencyDocument.getAttendances().stream().collect(toMap(a -> a.getStudent().getEmail(), Attendance::getStudent));

        frequencyDocument.getCourse().getMembers().forEach(member -> {
            if (!attendances.containsKey(member.getEmail())) {
                frequencyRepository.addInSet(frequencyDocument.getId(), "attendances", Attendance.builder().student(member).build());
            }
        });
    }

    public void endLastFrequencyOfCourse(String courseId) {
        getLastStartedFrequencyByCourse(courseId).ifPresent(this::endFrenquecyByCourse);
    }

    public boolean isTeacherInFrequency(String teacher, String frequencyId) {
        var frequecy = frequencyRepository.findById(frequencyId)
                .orElseThrow(() -> new RuntimeException("Frequencia n existe"));

        return frequecy.getCourse().getTeacher().getEmail().equals(teacher);
    }

    public List<FrequencyDocument> allFinishedFrequencyByCourse(String courseId) {

        var course = courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Curso n existe!"));

        return frequencyRepository.findByIdInAndFinished(course.getFrequencies(), Boolean.TRUE);

    }

    public Optional<FrequencyDocument> getFrequencyByCourseAndDate(String courseId, Instant date) {

        return frequencyRepository.findByCourseIdAndDate(courseId, date);

    }

    public Optional<FrequencyDocument> getLastStartedFrequencyByCourse(String courseId) {

        var course = courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Curso n existe!"));

        var frequencies = frequencyRepository.findFirst1ByIdInAndFinishedOrderByDate(course.getFrequencies(), Boolean.FALSE);

        return frequencies.isEmpty() ? Optional.empty() : Optional.of(frequencies.get(0));

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

    public List<FrequencyMapperResponse> getAllFrequencies(String courseId) {
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

        frequencyRepository.findAllByCourseId(courseId).forEach(frequencyDocument -> {
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

    public List<FrequencyResponse> getAllFrequenciesOfMember(String courseId, String memberId) {
        var frequecies = frequencyRepository.findAllByCourseId(courseId);

        List<FrequencyResponse> frequencyResponses = new ArrayList<>();

        frequecies.forEach(frequency ->
                frequency.getAttendances().stream()
                        .filter(attendance -> attendance.getStudent().getEmail().equals(memberId))
                        .findFirst().ifPresentOrElse(
                                attendance -> frequencyResponses.add(
                                        FrequencyResponse.builder()
                                                .id(frequency.getId())
                                                .date(frequency.getDate())
                                                .status(attendance.getStatus().getId())
                                                .build()
                                ),
                                () -> frequencyResponses.add(
                                        FrequencyResponse.builder()
                                                .id(frequency.getId())
                                                .date(frequency.getDate())
                                                .status(AttendanceEnum.UNDEFINED.getId())
                                                .build()
                                )
                        )
        );

        return frequencyResponses;

    }
}
