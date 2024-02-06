package tcc.uff.resource.server.service;

import tcc.uff.resource.server.model.request.UserPatchInfoRequest;
import tcc.uff.resource.server.model.response.entity.UserResponse;

public interface UserService {

    UserResponse getInfoUser(String email);

    UserResponse updateInfoUser(String email, UserPatchInfoRequest userPatchRequest);

}
