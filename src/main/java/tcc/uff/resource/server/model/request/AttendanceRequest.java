package tcc.uff.resource.server.model.request;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.group.GroupSequenceProvider;
import tcc.uff.resource.server.converter.Instant2StringConverter;
import tcc.uff.resource.server.handler.provider.LocationGroup;
import tcc.uff.resource.server.handler.provider.LocationProvider;
import tcc.uff.resource.server.model.enums.CommandRequestEnum;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@GroupSequenceProvider(LocationProvider.class)
public class AttendanceRequest {

    @JsonSerialize(converter = Instant2StringConverter.class)
    private Instant date;

    @NotNull
    private CommandRequestEnum type;

    @NotNull(groups = LocationGroup.ShouldExist.class)
    @Null(groups = LocationGroup.ShouldNotExist.class)
    private String latitude;

    @NotNull(groups = LocationGroup.ShouldExist.class)
    @Null(groups = LocationGroup.ShouldNotExist.class)
    private String longitude;
}
