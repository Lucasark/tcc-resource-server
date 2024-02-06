package tcc.uff.resource.server.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum UserContactEnum {

    EMAIL(1, "Email"),
    PHONE(2, "Phone"),
    CELLPHONE(3, "Cellphone"),
    LINKEDIN(4, "LinkedIn"),
    GITHUB(5, "GitHub"),
    TWITTER(6, "Twitter"),
    FACEBOOK(7, "Facebook"),
    INSTAGRAM(8, "Instagram"),
    TIKTOK(9, "TikTok"),
    SNAPCHAT(10, "Snapchat"),
    WHATSAPP(11, "WhatsApp"),
    TELEGRAM(12, "Telegram"),
    DISCORD(13, "Discord"),
    OTHER(0, "Other");

    private final Integer id;

    private final String name;

    public static List<UserContactEnum> getAllAttrbsSortedById() {

        return Arrays.stream(UserContactEnum.values())
                .collect(Collectors.toSet())
                .stream()
                .sorted(Comparator.comparing(UserContactEnum::getId))
                .collect(Collectors.toList());
    }

    public static UserContactEnum fromId(Integer type) {
        return Arrays.stream(values())
                .filter(file -> Objects.equals(file.getId(), type))
                .findFirst()
                .orElse(OTHER);
    }

}
