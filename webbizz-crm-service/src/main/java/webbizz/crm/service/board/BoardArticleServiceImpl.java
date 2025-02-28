package webbizz.crm.service.board;

import lombok.RequiredArgsConstructor;
import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import webbizz.crm.domain.YN;
import webbizz.crm.domain.board.BoardRepository;
import webbizz.crm.domain.board.entity.Board;
import webbizz.crm.domain.board.enumset.BoardConfigKey;
import webbizz.crm.domain.boardarticle.BoardArticleRepository;
import webbizz.crm.domain.boardarticle.dto.BoardArticleCondition;
import webbizz.crm.domain.boardarticle.dto.BoardArticleDto;
import webbizz.crm.domain.boardarticle.dto.BoardArticleListDto;
import webbizz.crm.domain.boardarticle.dto.BoardArticleSaveDto;
import webbizz.crm.domain.boardarticle.entity.BoardArticle;
import webbizz.crm.domain.member.entity.MemberUserDetails;
import webbizz.crm.exception.ApiBadRequestException;
import webbizz.crm.exception.ApiNotFoundException;
import webbizz.crm.exception.BusinessException;
import webbizz.crm.service.attachment.AttachmentService;
import webbizz.crm.service.captcha.CaptchaService;
import webbizz.crm.util.XssFilterUtils;

import java.util.List;

@Service("boardArticleService")
@RequiredArgsConstructor
public class BoardArticleServiceImpl extends EgovAbstractServiceImpl implements BoardArticleService {

    private final BoardArticleRepository boardArticleRepository;
    private final BoardRepository boardRepository;

    private final AttachmentService attachmentService;
    private final CaptchaService captchaService;


    @Override
    @Transactional(readOnly = true)
    public BoardArticleDto searchById(final Long id) {
        return boardArticleRepository.searchById(id)
                .orElseThrow(() -> new BusinessException(404, "게시물 정보를 찾을 수 없습니다."));
    }

    @Override
    @Transactional(readOnly = true)
    public BoardArticleDto searchBy(final Long boardId, final Long id) {
        return boardArticleRepository.searchBy(boardId, id)
                .orElseThrow(() -> new BusinessException(404, "게시물 정보를 찾을 수 없습니다."));
    }

    @Override
    @Transactional
    public BoardArticleDto searchByWithReadCount(final Long boardId, final Long id) {
        boardArticleRepository.addReadCount(boardId, id);

        return this.searchBy(boardId, id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BoardArticleDto> searchAllByCondition(final BoardArticleCondition condition) {
        return boardArticleRepository.searchAllByCondition(condition);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BoardArticleDto> searchRecentBy(Long memberId, int count) {
        return boardArticleRepository.searchRecentBy(memberId, count);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BoardArticleDto> searchRecentByCount(int count) {
        return boardArticleRepository.searchRecentByCount(count);
    }

    @Override
    @Transactional(readOnly = true)
    public BoardArticleDto searchRecentNotice() {
        // 메인에서 사용하므로 오류 던지지 않음
        return boardArticleRepository.searchRecentNotice().orElse(null);
    }

    @Override
    @Transactional
    public Long saveArticle(final MemberUserDetails memberUserDetails,
                            final BoardArticleSaveDto requestDto,
                            final HttpMethod httpMethod) {

        BoardArticle boardArticle = BoardArticle.builder().build();
        Board board;

        if (httpMethod == HttpMethod.POST) {
            board = boardRepository.findById(requestDto.getBoardId())
                    .orElseThrow(() -> new ApiNotFoundException(404, "게시판 정보를 찾을 수 없습니다."));

            // 회원 정보, 작성자 설정
            if (memberUserDetails != null) {
                boardArticle.setMember(memberUserDetails.getMember());
                boardArticle.setWriterName(memberUserDetails.getMember().getRealName());
            } else {
                boardArticle.setWriterName(requestDto.getWriterName());
            }
        }
        else if (httpMethod == HttpMethod.PUT) {
            boardArticle = boardArticleRepository.findById(requestDto.getId())
                    .orElseThrow(() -> new ApiNotFoundException(404, "게시물 정보를 찾을 수 없습니다."));
            board = boardArticle.getBoard();
        }
        else throw new ApiBadRequestException("게시판 정보가 없습니다.");

        if (requestDto.getBoardId() == null
                || board == null
                || !ObjectUtils.nullSafeEquals(board.getId(), requestDto.getBoardId())) {
            throw new ApiBadRequestException("게시판 정보를 찾을 수 없습니다.");
        }

        // 게시판 유형에 따른 필수 입력값 확인
        switch (board.getType()) {
            case GALLERY:
                if (requestDto.getImageUUIDs().isEmpty()) {
                    throw new ApiBadRequestException("이미지를 업로드해 주세요.");
                }
                break;

            case VIEWER:
                if (!StringUtils.hasText(requestDto.getPdfUUID())) {
                    throw new ApiBadRequestException("PDF 파일을 업로드해 주세요.");
                }
                break;
        }

        // 게시판 설정에 따른 권한 확인
        if (!BoardConfigKey.isUseConfig(board.getConfigs(), BoardConfigKey.USE_ARTICLE_YN)) {
            throw new ApiBadRequestException(BoardConfigKey.USE_ARTICLE_YN.getDetail() +  " 기능을 사용할 수 없습니다.");
        }
        if (!BoardConfigKey.isUseConfig(board.getConfigs(), BoardConfigKey.USE_ATTACHMENT_YN)) {
            if (!requestDto.getAttachmentUUIDs().isEmpty()) {
                throw new ApiBadRequestException(BoardConfigKey.USE_ATTACHMENT_YN.getDetail() + " 기능을 사용할 수 없습니다.");
            }
        }
        if (!BoardConfigKey.isUseConfig(board.getConfigs(), BoardConfigKey.USE_NOTICE_YN)) {
            if (requestDto.getNoticeYn().isBool()) {
                throw new ApiBadRequestException(BoardConfigKey.USE_NOTICE_YN.getDetail() + " 기능을 사용할 수 없습니다.");
            }
        }
        if (BoardConfigKey.isUseConfig(board.getConfigs(), BoardConfigKey.USE_CAPTCHA_YN)) { // TODO 관리자는 CAPTCHA 인증 무시
            if (!StringUtils.hasText(requestDto.getCaptchaResponse())) {
                throw new ApiBadRequestException("CAPTCHA 인증을 완료해 주세요.");
            }

            // reCAPTCHA 인증 처리
            captchaService.validate(requestDto.getCaptchaResponse());
        }

        // 게시물 정보 생성·수정
        boardArticle.setBoard(board);
        boardArticle.setTitle(requestDto.getTitle());
        boardArticle.setContent(XssFilterUtils.filter(requestDto.getContent()));
        boardArticle.setAttachments(attachmentService.findAllAttachmentDtoByUUID(requestDto.getAttachmentUUIDs()));
        boardArticle.setImages(attachmentService.findAllAttachmentDtoByUUID(requestDto.getImageUUIDs()));
        boardArticle.setPdf(attachmentService.findAttachmentDtoByUUID(requestDto.getPdfUUID()));
//        boardArticle.setSecretYn(requestDto.getSecretYn());
        boardArticle.setNoticeYn(requestDto.getNoticeYn());
        boardArticle.setViewYn(requestDto.getViewYn());

        boardArticleRepository.save(boardArticle);

        return boardArticle.getId();
    }

    @Override
    @Transactional
    public void deleteArticle(final Long boardId, final Long id) {
        BoardArticle boardArticle = boardArticleRepository.findById(id)
                .orElseThrow(() -> new ApiNotFoundException(404, "게시물 정보를 찾을 수 없습니다."));

        if (boardArticle.getBoard() == null || !ObjectUtils.nullSafeEquals(boardArticle.getBoard().getId(), boardId)) {
            throw new ApiBadRequestException("게시판 정보를 찾을 수 없습니다.");
        }

        // TODO 게시물 댓글 삭제 처리

        boardArticle.setDelYn(YN.Y);
    }

    @Override
    public List<BoardArticleListDto> searchAllByBoardType(BoardArticleCondition condition){
        return boardArticleRepository.searchAllByBoardType(condition);
    }

}
