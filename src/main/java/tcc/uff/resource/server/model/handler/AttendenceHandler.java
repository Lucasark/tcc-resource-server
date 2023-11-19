package tcc.uff.resource.server.model.handler;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Builder
public class AttendenceHandler {

    @NonNull
    private String id;

    @NonNull
    private String code;

    @NonNull
    private Instant date;

    @Builder.Default
    private Integer repeat = 0;

    @Builder.Default
    private Boolean joined = Boolean.FALSE;


}
