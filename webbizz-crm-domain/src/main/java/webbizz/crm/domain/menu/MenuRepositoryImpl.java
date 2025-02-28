package webbizz.crm.domain.menu;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.EnumPath;
import com.querydsl.core.types.dsl.Expressions;
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
import webbizz.crm.domain.board.dto.InnerBoardDto;
import webbizz.crm.domain.menu.dto.MenuDto;
import webbizz.crm.domain.menu.enumset.MenuType;
import webbizz.crm.domain.search.dto.SearchBoardDto;
import webbizz.crm.domain.search.dto.SearchCondition;
import webbizz.crm.domain.search.dto.SearchDto;
import webbizz.crm.domain.search.dto.SearchMenuDto;

import java.util.List;
import java.util.Optional;

import static webbizz.crm.domain.board.entity.QBoard.board;
import static webbizz.crm.domain.boardarticle.entity.QBoardArticle.boardArticle;
import static webbizz.crm.domain.menu.entity.QMenu.menu;
import static webbizz.crm.util.QuerydslUtils.*;
import static webbizz.crm.util.QuerydslUtils.condDateBetween;

@Repository
@RequiredArgsConstructor
public class MenuRepositoryImpl implements MenuRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    /**
     * {@link MenuDto} SELECT 필드
     */
    private final QBean<MenuDto> selectMenuDtoFields = Projections.fields(MenuDto.class,
            menu.id,
            menu.parentId,
            menu.groupId,
            menu.depth,
            menu.sortOrder,
            menu.name,
            menu.type,
            Projections.fields(InnerBoardDto.class, menu.board.id.as("id")).as("board"),
            menu.content,
            menu.linkUrl,
            menu.linkTarget,
            menu.seoKeyword,
            menu.seoDescription,
            menu.managerDepartment,
            menu.managerTelephone,
            menu.managerEmail,
            menu.publicUseYn,
            menu.viewYn
    );

    private final QBean<SearchDto> searchCountDtoField = Projections.fields(SearchDto.class,
            menu.count().as("totalCount")
    );
    private final QBean<SearchMenuDto> searchMenuDtoFields = Projections.fields(SearchMenuDto.class,
            menu.id,
            menu.name,
            menu.count().as("menuCount")
    );
    private final QBean<SearchBoardDto> searchBoardDtoFields = Projections.fields(SearchBoardDto.class,
            boardArticle.id,
            boardArticle.title,
            boardArticle.createdAt,
            boardArticle.content
    );

    @Override
    public Optional<MenuDto> searchByIdForUser(final Long id) {
        return Optional.ofNullable(queryFactory
                .select(selectMenuDtoFields)
                .from(menu)
                .where(
                        condEqOrAllFalse(menu.id, id),
                        condEq(menu.viewYn, YN.Y),
                        condEq(menu.delYn, YN.N)
                )
                .fetchFirst());
    }

    @Override
    public List<MenuDto> searchAllByGroupId(final Long groupId) {
        return queryFactory
                .select(selectMenuDtoFields)
                .from(menu)
                .where(
                        condEqOrAllFalse(menu.groupId, groupId),
                        condEq(menu.delYn, YN.N)
                )
                .orderBy(menu.depth.asc(), menu.sortOrder.asc())
                .fetch();
    }

    @Override
    public SearchDto searchBySearchText(String text) {
          SearchDto dto=queryFactory
                .select(searchCountDtoField)
                .from(menu)
                .leftJoin(boardArticle).on(menu.board.id.eq(boardArticle.board.id).and(boardArticle.delYn.eq(YN.N)).and(boardArticle.viewYn.eq(YN.Y)))
                .where(
                        condSearchEq(text),
                        condEq(menu.viewYn, YN.Y),
                        condEq(menu.delYn, YN.N)
                )
                .fetchFirst();
        dto.setMenuDtoList(queryFactory
                .select(searchMenuDtoFields)
                .from(menu)
                .leftJoin(boardArticle).on(menu.board.id.eq(boardArticle.board.id).and(boardArticle.delYn.eq(YN.N)).and(boardArticle.viewYn.eq(YN.Y)))
                .where(
                        condSearchEq(text),
                        condEq(menu.viewYn, YN.Y),
                        condEq(menu.delYn, YN.N)
                ).groupBy(menu.id)
                .fetch());
        dto.getMenuDtoList().forEach((d)->{
            d.setSearchBoardDtoList( queryFactory
                    .select(searchBoardDtoFields)
                    .from(menu)
                    .leftJoin(boardArticle).on(menu.board.id.eq(boardArticle.board.id).and(boardArticle.delYn.eq(YN.N)).and(boardArticle.viewYn.eq(YN.Y)))
                    .where(
                            condEqOrAllFalse(menu.id,d.getId()),
                            condSearchEq(text),
                            condEq(menu.viewYn, YN.Y),
                            condEq(menu.delYn, YN.N)
                    ).limit(3)
                    .fetch());
        });

        return dto;
    }

    @Override
    public Page<SearchBoardDto> searchBoardByCondition(final SearchCondition condition) {
        List<SearchBoardDto> content= queryFactory
                .select(searchBoardDtoFields)
                .from(menu)
                .leftJoin(boardArticle).on(menu.board.id.eq(boardArticle.board.id).and(boardArticle.delYn.eq(YN.N)).and(boardArticle.viewYn.eq(YN.Y)))
                .where(
                        condEqOrAllFalse(menu.id,condition.getMenuId()),
                        condSearchEq(condition.getSearchText()),
                        condEq(menu.viewYn, YN.Y),
                        condEq(menu.delYn, YN.N)
                )  .orderBy(menu.id.desc())
                .offset(condition.pageable().getOffset())
                .limit(condition.pageable().getPageSize())
                .fetch();


        JPQLQuery<Long> countQuery = queryFactory
                .select(Wildcard.count)
                .from(menu)
                .leftJoin(boardArticle).on(menu.board.id.eq(boardArticle.board.id).and(boardArticle.delYn.eq(YN.N)).and(boardArticle.viewYn.eq(YN.Y)))
                .where(
                        condEqOrAllFalse(menu.id,condition.getMenuId()),
                        condSearchEq(condition.getSearchText()),
                        condEq(menu.viewYn, YN.Y),
                        condEq(menu.delYn, YN.N)
                );

        return PageableExecutionUtils.getPage(content, condition.pageable(), countQuery::fetchCount);
    }


    public static Predicate condSearchEq(final String text) {
            return new BooleanBuilder()
                    .and(menu.type.eq(MenuType.BOARD)
                            .and(boardArticle.content.contains(text)
                                    .or(boardArticle.title.contains(text))));
        }

}
