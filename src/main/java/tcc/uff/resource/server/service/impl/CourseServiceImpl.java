package tcc.uff.resource.server.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import tcc.uff.resource.server.converter.CourseResponseConverter;
import tcc.uff.resource.server.model.document.CourseDocument;
import tcc.uff.resource.server.model.document.DaysOfWeek;
import tcc.uff.resource.server.model.document.UserAlias;
import tcc.uff.resource.server.model.document.UserDocument;
import tcc.uff.resource.server.model.request.CourseRequest;
import tcc.uff.resource.server.model.request.UserAddInBatchRequest;
import tcc.uff.resource.server.model.request.UserAddRequest;
import tcc.uff.resource.server.model.response.CourseBatchMemberResponse;
import tcc.uff.resource.server.model.response.UserAddResponse;
import tcc.uff.resource.server.model.response.entity.CourseResponse;
import tcc.uff.resource.server.repository.CourseRepository;
import tcc.uff.resource.server.repository.UserRepository;
import tcc.uff.resource.server.service.CourseService;
import tcc.uff.resource.server.service.mongooperations.MongoOperationsService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final ModelMapper mapper;

    private final CourseRepository courseRepository;

    private final CourseResponseConverter courseResponseConverter;

    private final UserRepository userRepository;

    private final MongoOperationsService mongoOperations;

    @Override
    public List<CourseResponse> getAllCourserOwnerByUser(String username) {
        var allDocuments = courseRepository.findByTeacherEmail(username);
        return allDocuments.stream().map(courseResponseConverter::toCourseResponse).toList();
    }

    @Override
    public List<CourseResponse> getAllCourserMemberByUser(String username) {
        var allDocuments = courseRepository.findByMembersEmail(username);
        return allDocuments.stream().map(courseResponseConverter::toCourseResponse).toList();
    }

    @Override
    public boolean isOwnerByUser(String username, String course) {
        var list = getAllCourserOwnerByUser(username);
        return list.stream().anyMatch(c -> c.getId().equals(course));
    }

    @Override
    public boolean isMemberByUser(String username, String course) {
        var list = getAllCourserMemberByUser(username);
        return list.stream().anyMatch(c -> c.getId().equals(course));
    }

    @Override
    public CourseResponse createCourse(CourseRequest courseRequest, String owner) {
        var curse = this.mapper.map(courseRequest, CourseDocument.class);
        var user = userRepository.findById(owner).orElseThrow(() -> new RuntimeException("N achou o User!"));
        curse.setTeacher(user);
        courseRepository.save(curse);
        return courseResponseConverter.toCourseResponse(curse);
    }

    @Override
    public CourseResponse putCourse(CourseRequest courseRequest, String courseId) {
        var document = courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("N achou o curso!"));

        document.setName(courseRequest.getName());
        document.setPeriod(courseRequest.getPeriod());
        document.setAbout(courseRequest.getAbout());
        document.getDaysOfWeek().clear();
        courseRequest.getDaysOfWeek().forEach(days -> document.getDaysOfWeek().add(this.mapper.map(days, DaysOfWeek.class)));

        courseRepository.save(document);

        return courseResponseConverter.toCourseResponse(document);
    }

    @Override
    public void deleteCourse(String courseId) {
        var document = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("N achou o curso!"));

        document.getMembers().forEach(user -> {
            user.getAliases().removeIf(userPredicate -> userPredicate.getCourseId().equals(courseId));
            userRepository.save(user);
        });

        courseRepository.deleteById(courseId);
    }

    @Override
    public CourseResponse getCourse(String courseId) {
        return courseResponseConverter.toCourseResponse(courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("N achou o curso!")));
    }


    @Override
    public CourseResponse addMember(String courseId, String memberId, String memberAlias, String memberRegistration) {
        var document = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("N achou o curso!"));

        if (document.getMembers().stream().anyMatch(user -> user.getEmail().equals(memberId)))
            throw new RuntimeException("Já está incluso!");

        if (document.getTeacher().getEmail().equals(memberId))
            throw new RuntimeException("Voce n pode se incluir como membro sendo dono!");

        var user = userRepository.findById(memberId)
                .orElseGet(() -> {
                    var passiveUser = UserDocument.builder()
                            .email(memberId)
                            .registration(memberRegistration)
                            .build();

                    return userRepository.save(passiveUser);
                });

        user.getAliases().add(UserAlias.builder()
                .name(memberAlias)
                .courseId(courseId)
                .build());

        mongoOperations.addInSet("email", user.getEmail(), "aliases", UserAlias.builder().name(memberAlias).courseId(courseId).build(), UserDocument.class);

        mongoOperations.addInSet("id", courseId, "members", user, CourseDocument.class);

        return courseResponseConverter.toCourseResponse(courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Erro ao recuperar"))
        );
    }

    @Override
    public CourseBatchMemberResponse addMembers(UserAddInBatchRequest userAddInBatchRequest, String courseId) {
        CourseBatchMemberResponse response = CourseBatchMemberResponse.builder().build();

        for (UserAddRequest userAddRequest : userAddInBatchRequest.getMembers()) {
            try {
                addMember(courseId, userAddRequest.getEmail(), userAddRequest.getAlias(), userAddRequest.getRegistration());
            } catch (Exception e) {
                var userError = mapper.map(userAddRequest, UserAddResponse.class);
                userError.setReason(e.getMessage());
                response.getFailed().add(userError);
            }
        }

        return response;
    }

}
