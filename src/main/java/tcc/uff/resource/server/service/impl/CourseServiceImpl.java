package tcc.uff.resource.server.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import tcc.uff.resource.server.model.document.CourseDocument;
import tcc.uff.resource.server.model.request.CourseRequest;
import tcc.uff.resource.server.model.response.entity.CourseResponse;
import tcc.uff.resource.server.repository.CourseRepository;
import tcc.uff.resource.server.repository.UserRepository;
import tcc.uff.resource.server.service.CourseService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final ModelMapper mapper;

    private final CourseRepository courseRepository;

    private final UserRepository userRepository;

    @Override
    public List<CourseResponse> getAllCourserOwnerByUser(String username) {
        var allDocuments = courseRepository.findByTeacherEmail(username);
        return allDocuments.stream().map(element -> {
            var mapped = mapper.map(element, CourseResponse.class);
            mapped.setOwner(element.getTeacher().getEmail());
            return mapped;
        }).collect(Collectors.toList());
    }

    @Override
    public List<CourseResponse> getAllCourserMemberByUser(String username) {
        var allDocuments = courseRepository.findByMembersEmail(username);
        return allDocuments.stream().map(element -> {
            var mapped = mapper.map(element, CourseResponse.class);
            mapped.setOwner(element.getTeacher().getEmail());
            return mapped;
        }).collect(Collectors.toList());
    }

    @Override
    public boolean isOwnerByUser(String username, String course) {
        var list = getAllCourserOwnerByUser(username);
        return list.stream().anyMatch(c -> c.getId().equals(course));
    }

    @Override
    public boolean isMemberByUser(String username, String course) {
        var list = getAllCourserMemberByUser(username);
        return list.stream().anyMatch(c -> c.getId().equals(course));    }

    @Override
    public CourseResponse createCourse(CourseRequest courseRequest, String owner) {
        var curse = this.mapper.map(courseRequest, CourseDocument.class);
        var user = userRepository.findById(owner);
        if (user.isEmpty()) throw new RuntimeException("N achou!");
        curse.setTeacher(user.get());
        var courseNew = courseRepository.save(curse);
        var response = this.mapper.map(courseNew, CourseResponse.class);
        response.setOwner(courseNew.getTeacher().getEmail());
        return response;
    }

}
