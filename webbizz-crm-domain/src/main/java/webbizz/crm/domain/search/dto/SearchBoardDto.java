package webbizz.crm.domain.search.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SearchBoardDto {

    /**
     * 제목
     */
    private String title;

    /**
     * 게시판 시퀀스
     */
    private Long id;

    /**
     * 내용
     */
    private String content;

    /**
     * 위치
     */
    private  String navigator;

    /**
     * 등록일
     */
    private LocalDateTime createdAt;

}
