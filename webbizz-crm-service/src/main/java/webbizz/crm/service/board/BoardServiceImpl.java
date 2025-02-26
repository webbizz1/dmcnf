package webbizz.crm.service.board;

import lombok.RequiredArgsConstructor;
import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import webbizz.crm.domain.board.BoardRepository;
import webbizz.crm.domain.board.dto.BoardCondition;
import webbizz.crm.domain.board.dto.BoardDto;
import webbizz.crm.domain.board.dto.BoardSaveDto;
import webbizz.crm.domain.board.entity.Board;
import webbizz.crm.domain.boardconfig.BoardConfigRepository;
import webbizz.crm.domain.boardconfig.entity.BoardConfig;
import webbizz.crm.exception.ApiBadRequestException;
import webbizz.crm.exception.ApiNotFoundException;
import webbizz.crm.exception.BusinessException;
import webbizz.crm.util.XssFilterUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("boardService")
@RequiredArgsConstructor
public class BoardServiceImpl extends EgovAbstractServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final BoardConfigRepository boardConfigRepository;

    private final List<BoardDto> cacheBoardCompactList = new ArrayList<>();

    @Override
    @Transactional(readOnly = true)
    public List<BoardDto> getBoardCompactList() {
        if (cacheBoardCompactList.isEmpty()) {
            this.cacheBoardCompactList.addAll(boardRepository.searchAllCompact());
        }

        return this.cacheBoardCompactList;
    }

    @Override
    @Transactional(readOnly = true)
    public BoardDto searchById(final Long id) {
        BoardDto boardDto = boardRepository.searchById(id)
                .orElseThrow(() -> new BusinessException(404, "게시판 정보를 찾을 수 없습니다."));

        // 게시판 설정 값 조회 후 DTO 에 설정
        Map<String, String> configMap = boardConfigRepository.searchByBoardId(id);
        boardDto.getConfig().putAll(configMap);

        return boardDto;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BoardDto> searchAllByCondition(final BoardCondition condition) {
        Page<BoardDto> boardDtos = boardRepository.searchAllByCondition(condition);

        // 쿼리 호출을 줄이기 위해 조회된 게시판 목록에서 게시판 시퀀스만 추출
        List<Long> boardIds = boardDtos.stream()
                .map(BoardDto::getId)
                .collect(Collectors.toList());

        // 게시판 설정 값 조회 후 DTO 에 설정
        Map<Long, Map<String, String>> configMaps = boardConfigRepository.searchAllByBoardIds(boardIds);
        configMaps.forEach((k, v) -> {
            boardDtos.stream()
                    .filter(boardDto -> boardDto.getId().equals(k))
                    .forEach(boardDto -> boardDto.getConfig().putAll(v));
        });

        return boardDtos;
    }

    @Override
    @Transactional
    public Long createBoard(final BoardSaveDto requestDto) {
        // 게시판 이름 중복 확인
        if (boardRepository.countByName(requestDto.getName().trim()) > 0) {
            throw new ApiBadRequestException(String.format("이미 존재하는 게시판 이름입니다: '%s'", requestDto.getName()));
        }

        // 게시판 정보 생성
        Board board = Board.builder()
                .name(requestDto.getName().trim())
                .type(requestDto.getType())
                .description(XssFilterUtils.filter(requestDto.getDescription()))
                .useDescriptionYn(requestDto.getUseDescriptionYn())
                .viewYn(requestDto.getViewYn())
                .build();

        // 게시판 설정 정보 생성
        List<BoardConfig> configs = requestDto.getConfig()
                .entrySet()
                .stream()
                .map(entry -> BoardConfig.builder()
                        .key(entry.getKey())
                        .value(entry.getValue())
                        .build())
                .collect(Collectors.toList());
        board.setConfigs(configs);

        boardRepository.save(board);

        // 게시판 목록 간략 정보 캐시 초기화
        this.cacheBoardCompactList.clear();

        return board.getId();
    }

    @Override
    @Transactional
    public Long updateBoard(final BoardSaveDto requestDto) {
        if (!StringUtils.hasText(requestDto.getName())) {
            throw new ApiBadRequestException("게시판 이름을 입력해주세요.");
        }

        // 게시판 이름 중복 확인
        if (boardRepository.countByName(requestDto.getName().trim()) > 1) {
            throw new ApiBadRequestException(String.format("이미 존재하는 게시판 이름입니다: '%s'", requestDto.getName()));
        }

        // 게시판 정보 조회
        Board board = boardRepository.findById(requestDto.getId())
                .orElseThrow(() -> new ApiNotFoundException(404, "게시판 정보를 찾을 수 없습니다."));

        // 게시판 정보 수정
        board.setName(requestDto.getName().trim());
        board.setType(requestDto.getType());
        board.setDescription(XssFilterUtils.filter(requestDto.getDescription()));
        board.setUseDescriptionYn(requestDto.getUseDescriptionYn());
        board.setViewYn(requestDto.getViewYn());

        // 게시판 설정 정보 삭제 (hard delete)
        boardConfigRepository.deleteByBoardId(board.getId());

        // 게시판 설정 정보 생성
        List<BoardConfig> configs = requestDto.getConfig()
                .entrySet()
                .stream()
                .map(entry -> BoardConfig.builder()
                        .key(entry.getKey())
                        .value(entry.getValue())
                        .build())
                .collect(Collectors.toList());
        board.setConfigs(configs);

        // 게시판 목록 간략 정보 캐시 초기화
        this.cacheBoardCompactList.clear();

        return board.getId();
    }

}
