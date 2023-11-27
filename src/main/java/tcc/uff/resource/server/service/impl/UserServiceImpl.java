package tcc.uff.resource.server.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import tcc.uff.resource.server.model.response.entity.UserResponse;
import tcc.uff.resource.server.repository.UserRepository;
import tcc.uff.resource.server.service.UserService;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final ModelMapper mapper;

    @Override
    public UserResponse getInfoUser(String email) {

        var document = userRepository.findById(email).orElseThrow(() -> new RuntimeException("N achou user!"));

        return this.mapper.map(document, UserResponse.class);
    }

}
