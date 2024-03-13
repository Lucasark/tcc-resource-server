package tcc.uff.resource.server.configuration;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tcc.uff.resource.server.model.document.UserContact;
import tcc.uff.resource.server.model.document.UserDocument;
import tcc.uff.resource.server.model.document.UserInfo;
import tcc.uff.resource.server.model.enums.UserContactEnum;
import tcc.uff.resource.server.model.request.UserPatchInfoRequest;
import tcc.uff.resource.server.model.response.entity.UserContactResponse;
import tcc.uff.resource.server.model.response.entity.UserInfoResponse;
import tcc.uff.resource.server.model.response.entity.UserResponse;

import java.util.Objects;
import java.util.stream.Collectors;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        var modelmapper = new ModelMapper();
        modelmapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);


        Converter<UserPatchInfoRequest, UserInfo> converterUserPatchInfoRequestToUserInfo = context -> {

            context.getDestination().getContacts().clear();

            context.getSource().getContacts().forEach(contact ->
                    context.getDestination().getContacts().add(UserContact.builder()
                            .id(UserContactEnum.fromId(contact.getId()))
                            .value(contact.getValue())
                            .build())
            );

            return context.getDestination();
        };

        modelmapper.typeMap(UserPatchInfoRequest.class, UserInfo.class)
                .setPostConverter(converterUserPatchInfoRequestToUserInfo);


        modelmapper.typeMap(UserDocument.class, UserResponse.class)
                .addMappings(mapping -> mapping.skip((u, x) -> u.getInfo().setContacts(null)))
                .setPostConverter(context -> {

                    if (Objects.isNull(context.getDestination().getInfo())) return context.getDestination();

                    context.getDestination().setInfo(UserInfoResponse.builder()
                            .about(context.getSource().getInfo().getAbout())
                            .contacts(context.getSource().getInfo().getContacts().stream()
                                    .map(contact -> UserContactResponse.builder()
                                            .id(contact.getId().getId())
                                            .type(contact.getId().getName())
                                            .value(contact.getValue())
                                            .build())
                                    .collect(Collectors.toSet()))
                            .build());

                    return context.getDestination();
                });

        return modelmapper;
    }

}
