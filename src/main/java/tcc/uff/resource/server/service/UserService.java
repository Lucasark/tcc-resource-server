package tcc.uff.resource.server.service;

import tcc.uff.resource.server.model.response.entity.UserResponse;

public interface UserService {

    UserResponse getInfoUser(String email);

}
