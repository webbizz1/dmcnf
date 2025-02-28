package webbizz.crm.domain.boardarticle.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Getter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Getter
public class ReCaptchaResponseDto {

    private boolean success;

    private LocalDateTime challengeTs;

    private String hostname;

    @JsonProperty("error-codes")
    private List<String> errorCodes;

    @JsonSetter("challenge_ts")
    private void setChallengeTs(Instant challengeTs) {
        this.challengeTs = LocalDateTime.ofInstant(challengeTs, ZoneId.of("Asia/Seoul"));
    }

}
