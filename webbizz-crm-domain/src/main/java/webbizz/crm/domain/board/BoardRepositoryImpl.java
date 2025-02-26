package webbizz.crm.domain.board;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import webbizz.crm.domain.YN;
import webbizz.crm.domain.board.dto.BoardCondition;
import webbizz.crm.domain.board.dto.BoardDto;

import java.util.List;
import java.util.Optional;

import static webbizz.crm.domain.board.entity.QBoard.board;
import static webbizz.crm.util.QuerydslUtils.*;

@Repository
@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    /** {@link BoardDto} SELECT 필드 */
    private final QBean<BoardDto> selectBoardDtoFields = Projections.fields(BoardDto.class,
            board.id,
            board.type,
            board.name,
            board.description,
            board.useDescriptionYn,
            board.viewYn,
            board.createdId,
            board.createdAt,
            board.updatedAt
    );

    @Override
    public Optional<BoardDto> searchById(final Long id) {
        return Optional.ofNullable(queryFactory
                .select(this.selectBoardDtoFields)
                .from(board)
                .where(
                        condEqOrAllFalse(board.id, id),
                        condEq(board.delYn, YN.N)
                )
                .fetchFirst());
    }

    @Override
    public Page<BoardDto> searchAllByCondition(final BoardCondition condition) {
        List<BoardDto> content = queryFactory
                .select(this.selectBoardDtoFields)
                .from(board)
                .where(
                        condDateBetween(board.createdAt, condition.getStartDate(), condition.getEndDate()),
                        condEq(board.viewYn, condition.getViewYn()),
                        condEq(board.delYn, YN.N)
                )
                .orderBy(board.id.desc())
                .offset(condition.pageable().getOffset())
                .limit(condition.pageable().getPageSize())
                .fetch();

        JPQLQuery<Long> countQuery = queryFactory
                .select(Wildcard.count)
                .from(board)
                .where(
                        condDateBetween(board.createdAt, condition.getStartDate(), condition.getEndDate()),
                        condEq(board.viewYn, condition.getViewYn()),
                        condEq(board.delYn, YN.N)
                );

        return PageableExecutionUtils.getPage(content, condition.pageable(), countQuery::fetchCount);
    }

    @Override
    public List<BoardDto> searchAllCompact() {
        return queryFactory
                .select(Projections.fields(BoardDto.class,
                        board.id,
                        board.name
                ))
                .from(board)
                .where(
                        condEq(board.viewYn, YN.Y),
                        condEq(board.delYn, YN.N)
                )
                .orderBy(board.name.asc())
                .fetch();
    }

}
