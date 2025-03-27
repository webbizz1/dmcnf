package webbizz.crm.domain.boardarticle;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import webbizz.crm.domain.YN;
import webbizz.crm.domain.board.enumset.BoardType;
import webbizz.crm.domain.boardarticle.dto.BoardArticleCondition;
import webbizz.crm.domain.boardarticle.dto.BoardArticleDto;
import webbizz.crm.domain.boardarticle.dto.BoardArticleListDto;

import java.util.List;
import java.util.Optional;

import static webbizz.crm.domain.board.entity.QBoard.board;
import static webbizz.crm.domain.boardarticle.entity.QBoardArticle.boardArticle;
import static webbizz.crm.domain.menu.entity.QMenu.menu;
import static webbizz.crm.util.QuerydslUtils.*;

@Repository
@RequiredArgsConstructor
public class BoardArticleRepositoryImpl implements BoardArticleRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    /** {@link BoardArticleDto} SELECT 필드 */
    private final QBean<BoardArticleDto> selectBoardArticleDtoFields = Projections.fields(BoardArticleDto.class,
            boardArticle.id,
            boardArticle.board.id.as("boardId"),
            boardArticle.member.id.as("memberId"),
            boardArticle.writerName,
            boardArticle.title,
            boardArticle.content,
            boardArticle.attachments,
            boardArticle.images,
            boardArticle.pdf,
            boardArticle.readCount,
            boardArticle.secretYn,
            boardArticle.noticeYn,
            boardArticle.viewYn,
            boardArticle.createdAt,
            boardArticle.updatedAt
    );

    @Override
    public Optional<BoardArticleDto> searchById(final Long id) {
        return Optional.ofNullable(queryFactory
                .select(this.selectBoardArticleDtoFields)
                .from(boardArticle)
                .where(
                        condEqOrAllFalse(boardArticle.id, id),
                        condEq(boardArticle.delYn, YN.N)
                )
                .fetchFirst());
    }

    @Override
    public Optional<BoardArticleDto> searchBy(final Long boardId, final Long id) {
        return Optional.ofNullable(queryFactory
                .select(this.selectBoardArticleDtoFields)
                .from(boardArticle)
                .where(
                        condEqOrAllFalse(boardArticle.board.id, boardId),
                        condEqOrAllFalse(boardArticle.id, id),
                        condEq(boardArticle.delYn, YN.N)
                )
                .fetchFirst());
    }

    @Override
    public Page<BoardArticleDto> searchAllByCondition(final BoardArticleCondition condition) {
        List<BoardArticleDto> content = queryFactory
                .select(this.selectBoardArticleDtoFields)
                .from(boardArticle)
                .where(
                        condEqOrAllFalse(boardArticle.board.id, condition.getBoardId()),
                        condMultiFieldLike(condition.getSearchField(), condition.getSearchText()),
                        condEq(boardArticle.viewYn, condition.getViewYn()),
                        condEq(boardArticle.delYn, YN.N)
                )
                .orderBy(
                        boardArticle.noticeYn.desc(),
                        boardArticle.id.desc()
                )
                .offset(condition.pageable().getOffset())
                .limit(condition.pageable().getPageSize())
                .fetch();

        JPQLQuery<Long> countQuery = queryFactory
                .select(Wildcard.count)
                .from(boardArticle)
                .where(
                        condEqOrAllFalse(boardArticle.board.id, condition.getBoardId()),
                        condMultiFieldLike(condition.getSearchField(), condition.getSearchText()),
                        condEq(boardArticle.viewYn, condition.getViewYn()),
                        condEq(boardArticle.delYn, YN.N)
                );

        return PageableExecutionUtils.getPage(content, condition.pageable(), countQuery::fetchCount);
    }

    @Override
    public List<BoardArticleListDto> searchAllByBoardType(final BoardArticleCondition condition) {
        return queryFactory.select(Projections.fields(BoardArticleListDto.class,
                        boardArticle.id,
                        boardArticle.board.name.as("boardName"),
                        boardArticle.title,
                        boardArticle.content,
                        boardArticle.images,
                        boardArticle.secretYn,
                        boardArticle.noticeYn,
                        boardArticle.viewYn,
                        boardArticle.createdAt,
                        boardArticle.updatedAt,
                        menu.id.as("menuId")
                ))
                .from(boardArticle)
                .join(menu).on(menu.board.id.eq(boardArticle.board.id))
                .where(
                        condEq(boardArticle.viewYn, YN.Y),
                        condEq(boardArticle.delYn, YN.N),
                        condEq(menu.viewYn, YN.Y),
                        condEq(menu.delYn, YN.N),
                        condBoardType(condition.getBoardType())
                )
                .groupBy(boardArticle.id)
                .orderBy(
                        boardArticle.noticeYn.desc(),
                        boardArticle.id.desc()
                )
                .limit(condition.getSize())
                .fetch();
    }

    @Override
    public List<BoardArticleDto> searchRecentBy(Long memberId, int count) {
        return queryFactory
                .select(this.selectBoardArticleDtoFields)
                .from(boardArticle)
                .join(boardArticle.board, board)
                .where(
                        condEqOrAllFalse(boardArticle.member.id, memberId),
                        condEq(boardArticle.delYn, YN.N),
                        condEq(board.delYn, YN.N)
                )
                .orderBy(boardArticle.id.desc())
                .limit(count)
                .fetch();
    }

    @Override
    public List<BoardArticleDto> searchRecentByCount(int count) {
        return queryFactory
                .select(this.selectBoardArticleDtoFields)
                .from(boardArticle)
                .join(boardArticle.board, board)
                .where(
                        condEq(boardArticle.delYn, YN.N),
                        condEq(board.delYn, YN.N)
                )
                .orderBy(
                        new CaseBuilder()
                                .when(boardArticle.updatedAt.isNotNull()).then(boardArticle.updatedAt)
                                .otherwise(boardArticle.createdAt).desc()
                )
                .limit(count)
                .fetch();
    }

    @Override
    public Optional<BoardArticleDto> searchRecentNotice() {
        return Optional.ofNullable(
                queryFactory
                        .select(this.selectBoardArticleDtoFields)
                        .from(boardArticle)
                        .join(boardArticle.board, board)
                        .where(
                                condEqOrAllFalse(boardArticle.noticeYn, YN.Y),
                                condEq(boardArticle.delYn, YN.N),
                                condEq(board.delYn, YN.N)
                        )
                        .fetchFirst()
        );
    }

    @Override
    @Transactional
    public void addReadCount(final Long boardId, final Long id) {
        queryFactory
                .update(boardArticle)
                .set(boardArticle.readCount, boardArticle.readCount.add(1))
                .where(
                        condEqOrAllFalse(boardArticle.board.id, boardId),
                        condEqOrAllFalse(boardArticle.id, id),
                        condEq(boardArticle.viewYn, YN.Y),
                        condEq(boardArticle.delYn, YN.N)
                )
                .execute();
    }


    private Predicate condMultiFieldLike(final String searchField, final String searchText) {
        BooleanBuilder builder = new BooleanBuilder();

        if (!StringUtils.hasText(searchField) || !StringUtils.hasText(searchText)) {
            return builder;
        }

        switch (searchField) {
            case "all":
                builder.or(condLike(boardArticle.title, searchText));
                builder.or(condLike(boardArticle.content, searchText));
                break;

            case "title":
                builder.or(condLike(boardArticle.title, searchText));
                break;

            case "content":
                builder.or(condLike(boardArticle.content, searchText));
                break;
        }

        return builder;
    }

    private Predicate condBoardType(String boardType) {
        BooleanBuilder builder = new BooleanBuilder();

        switch (boardType) {
            case "notice":
                builder.or(boardArticle.board.type.eq(BoardType.TEXT));
                break;

            case "news":
                builder.or(boardArticle.board.type.eq(BoardType.GALLERY));
                builder.or(boardArticle.board.type.eq(BoardType.VIEWER));
                break;
        }

        return builder;
    }

}
