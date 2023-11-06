package tcc.uff.resource.server.service;

import tcc.uff.resource.server.model.request.CourseRequest;
import tcc.uff.resource.server.model.response.entity.CourseResponse;

import java.util.List;

public interface CourseService {

    List<CourseResponse> getAllCourserOwnerByUser(String username);

    List<CourseResponse> getAllCourserMemberByUser(String username);

    boolean isOwnerByUser(String username, String course);

    CourseResponse createCourse(CourseRequest courseRequest, String owner);
}
