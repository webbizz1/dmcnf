package webbizz.crm.domain.boardconfig;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import webbizz.crm.domain.boardconfig.dto.BoardConfigDto;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static webbizz.crm.domain.boardconfig.entity.QBoardConfig.boardConfig;
import static webbizz.crm.util.QuerydslUtils.condEqOrAllFalse;
import static webbizz.crm.util.QuerydslUtils.condIn;

@Repository
@RequiredArgsConstructor
public class BoardConfigRepositoryImpl implements BoardConfigRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Map<String, String> searchByBoardId(final Long boardId) {
        List<BoardConfigDto> content = queryFactory
                .select(Projections.fields(BoardConfigDto.class,
                        boardConfig.board.id,
                        boardConfig.key,
                        boardConfig.value
                ))
                .from(boardConfig)
                .where(condEqOrAllFalse(boardConfig.board.id, boardId))
                .fetch();

        return content.stream()
                .collect(Collectors.toMap(BoardConfigDto::getKey, BoardConfigDto::getValue));
    }

    @Override
    public Map<Long, Map<String, String>> searchAllByBoardIds(final List<Long> boardIds) {
        List<BoardConfigDto> content = queryFactory
                .select(Projections.fields(BoardConfigDto.class,
                        boardConfig.board.id,
                        boardConfig.key,
                        boardConfig.value
                ))
                .from(boardConfig)
                .where(condIn(boardConfig.board.id, boardIds))
                .fetch();

        return content.stream()
                .collect(Collectors.groupingBy(
                        BoardConfigDto::getId,
                        Collectors.toMap(BoardConfigDto::getKey, BoardConfigDto::getValue)));
    }

    @Override
    public void deleteByBoardId(final Long boardId) {
        queryFactory
                .delete(boardConfig)
                .where(condEqOrAllFalse(boardConfig.board.id, boardId))
                .execute();
    }

}
