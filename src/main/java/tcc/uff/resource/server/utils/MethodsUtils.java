package tcc.uff.resource.server.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MethodsUtils {

    public static String convertObjectToJson(Object object) throws JsonProcessingException {
        Assert.notNull(object, "Objeto a ser transformado nao deve ser null!");
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
}
