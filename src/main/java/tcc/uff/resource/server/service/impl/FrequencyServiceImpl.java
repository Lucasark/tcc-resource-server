package tcc.uff.resource.server.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tcc.uff.resource.server.model.document.Attendance;
import tcc.uff.resource.server.model.document.CourseDocument;
import tcc.uff.resource.server.model.document.FrequencyDocument;
import tcc.uff.resource.server.model.enums.AttendanceEnum;
import tcc.uff.resource.server.model.response.entity.FrequencyMapperResponse;
import tcc.uff.resource.server.model.response.entity.FrequencyResponse;
import tcc.uff.resource.server.repository.CourseRepository;
import tcc.uff.resource.server.repository.FrequencyRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class FrequencyServiceImpl {

    private final CourseRepository courseRepository;

    private final FrequencyRepository frequencyRepository;

    public void initFrenquency(String courseId, Instant date) throws RuntimeException {

        var courseDocument = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("N achou Classe!"));

        initFrenquency(courseDocument, date);
    }

    public void initFrenquency(CourseDocument courseDocument, Instant date) throws RuntimeException {

        Set<Attendance> attendances = new HashSet<>();

        courseDocument.getMembers().forEach(member ->
                attendances.add(Attendance.builder()
                        .student(member)
                        .status(AttendanceEnum.MISS)
                        .build())
        );

        var frequencyNew = FrequencyDocument.builder()
                .course(courseDocument)
                .attendances(attendances)
                .date(date)
                .build();

        frequencyRepository.save(frequencyNew);

        courseRepository.addInSet(courseDocument.getId(), "frequencies", frequencyNew.getId());
    }


    public void endFrenquecyByCourse(FrequencyDocument frequencyDocument) {
        frequencyDocument.setFinished(Boolean.TRUE);
        frequencyDocument.setFinishedAt(LocalDateTime.now());
        frequencyRepository.save(frequencyDocument);
    }

    public void endLastFrequencyOfCourse(String courseId) {
        getLastStartedFrequencyByCourse(courseId).ifPresent(this::endFrenquecyByCourse);
    }


    public boolean isTeacherInFrequency(String teacher, String frequencyId) {
        var count = frequencyRepository.countIdAndCourseTeacherEmail(frequencyId, teacher);
        return count.getTotal() > 0;
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
        return courseRepository.getViewFrequencyByCourseIdAndDate(courseId, start, end);
    }

    public List<FrequencyMapperResponse> getAllFrequencies(String courseId) {
        return courseRepository.getViewFrequencyByCourseId(courseId);
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

    private void finish() {

    }
}
