package tcc.uff.resource.server.converter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import tcc.uff.resource.server.model.document.CourseDocument;
import tcc.uff.resource.server.model.document.UserAlias;
import tcc.uff.resource.server.model.response.entity.CourseResponse;
import tcc.uff.resource.server.model.response.entity.UserResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourseResponseConverter {

    private final ModelMapper mapper;

    public CourseResponse toCourseResponse(CourseDocument document) {

        var response = this.mapper.map(document, CourseResponse.class);
        response.setOwner(document.getTeacher().getEmail());

        document.getMembers().forEach(memberDocument -> {

                    String alias = memberDocument.getAliases().stream()
                            .filter(f -> f.getCourseId().equals(document.getId()))
                            .map(UserAlias::getName)
                            .findFirst()
                            .orElse("S/A");

                    response.getMembers().add(
                            UserResponse.builder()
                                    .email(memberDocument.getEmail())
                                    .name(memberDocument.getName())
                                    .alias(alias)
                                    .build()
                    );
                }
        );

        return response;
    }
}
