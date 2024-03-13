package tcc.uff.resource.server.model.response.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserContactResponse implements Serializable {

    private Integer id;

    private String type;

    private String value;

}
