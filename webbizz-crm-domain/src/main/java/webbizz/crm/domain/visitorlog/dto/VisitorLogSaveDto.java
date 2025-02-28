package webbizz.crm.domain.visitorlog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class VisitorLogSaveDto {

    private String applicationName;

    private Long memberId;

    private String remoteAddr;

    private String requestUri;

}
