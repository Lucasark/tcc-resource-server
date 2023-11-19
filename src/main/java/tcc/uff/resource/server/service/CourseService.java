package tcc.uff.resource.server.service;

import tcc.uff.resource.server.model.request.CourseRequest;
import tcc.uff.resource.server.model.response.entity.CourseResponse;

import java.util.List;

public interface CourseService {

    List<CourseResponse> getAllCourserOwnerByUser(String username);

    List<CourseResponse> getAllCourserMemberByUser(String username);

    boolean isOwnerByUser(String username, String course);
    boolean isMemberByUser(String username, String course);

    CourseResponse createCourse(CourseRequest courseRequest, String owner);

    CourseResponse patchCourse(CourseRequest courseRequest, String courseId);

    void deleteCourse(String courseId);

    CourseResponse getCourse(String courseId);

    CourseResponse addMember(String courseId, String memberId, String memberAlias, String memberRegistration);

}
