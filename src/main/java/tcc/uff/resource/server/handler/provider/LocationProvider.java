package tcc.uff.resource.server.handler.provider;


import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;
import tcc.uff.resource.server.model.request.AttendanceRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LocationProvider implements DefaultGroupSequenceProvider<AttendanceRequest> {
    @Override
    public List<Class<?>> getValidationGroups(AttendanceRequest attendanceRequest) {

        List<Class<?>> groups = new ArrayList<>();
        groups.add(AttendanceRequest.class);

        if (Objects.nonNull(attendanceRequest.getLatitude()) || Objects.nonNull(attendanceRequest.getLongitude())) {
            groups.add(LocationGroup.ShouldExist.class);
        } else {
            groups.add(LocationGroup.ShouldNotExist.class);
        }

        return groups;
    }
}
