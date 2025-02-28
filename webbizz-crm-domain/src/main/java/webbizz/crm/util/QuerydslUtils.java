package webbizz.crm.util;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.*;
import org.springframework.util.StringUtils;
import webbizz.crm.domain.BaseEnumSet;
import webbizz.crm.domain.YN;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class QuerydslUtils {

    /**
     * 숫자 일치 조건 (일치하지 않으면 조건 무시)
     *
     * @param path Querydsl Path
     * @param value 비교 값
     * @return Predicate 조건
     * @param <T> Number & Comparable (Integer, Long, ...)
     * @author TAEROK HWANG
     */
    public static <T extends Number & Comparable<?>> Predicate condEq(final NumberPath<T> path, final T value) {
        return value != null ? path.eq(value) : null;
    }

    /**
     * 문자열 일치 조건 (일치하지 않으면 조건 무시)
     *
     * @param path Querydsl Path
     * @param value 비교 값
     * @return Predicate 조건
     * @author TAEROK HWANG
     */
    public static Predicate condEq(final StringPath path, final String value) {
        return StringUtils.hasText(value) ? path.eq(value) : null;
    }

    /**
     * YN 일치 조건 (일치하지 않으면 조건 무시)
     *
     * @param path Querydsl Path
     * @param yn 비교 값 (Y, N)
     * @return Predicate 조건
     * @author TAEROK HWANG
     */
    public static Predicate condEq(final EnumPath<YN> path, final YN yn) {
        return yn != null ? path.eq(yn) : null;
    }

    /**
     * EnumSet 일치 조건 (일치하지 않으면 조건 무시)
     *
     * @param path Querydsl Path
     * @param value 비교 값
     * @return Predicate 조건
     * @param <T> EnumSet 을 구현한 Enum <br />
     *          (<code>public enum XXX implements {@link BaseEnumSet}</code>)
     * @author TAEROK HWANG
     */
    public static <T extends Enum<T> & BaseEnumSet> Predicate condEq(final EnumPath<T> path, final T value) {
        return value != null ? path.eq(value) : null;
    }

    /**
     * 숫자 일치 조건 (일치하지 않으면 전체 불일치)
     *
     * @param path Querydsl Path
     * @param value 비교 값
     * @return Predicate 조건
     * @param <T> Number & Comparable (Integer, Long, ...)
     * @author TAEROK HWANG
     */
    public static <T extends Number & Comparable<?>> Predicate condEqOrAllFalse(final NumberPath<T> path,
                                                                                final T value) {

        return value != null ? path.eq(value) : Expressions.FALSE.isTrue();
    }

    public static <T extends Enum<T>> Predicate condEqOrAllFalse(final EnumPath<T> path, final T value) {
        return value != null ? path.eq(value) : Expressions.FALSE.isTrue();
    }

    /**
     * 숫자 리스트 일치 조건 (일치하지 않으면 조건 무시)
     *
     * @param path Querydsl Path
     * @param values 찾을 값 리스트
     * @return Predicate 조건
     * @param <T> Number & Comparable (Integer, Long, ...)
     * @author TAEROK HWANG
     */
    public static <T extends Number & Comparable<?>> Predicate condIn(final NumberPath<T> path, final List<T> values) {
        return values != null ? path.in(values) : null;
    }

    /**
     * 문자열 LIKE 조건 (일치하지 않으면 조건 무시)
     *
     * @param path Querydsl Path
     * @param value 찾을 값
     * @return Predicate 조건
     * @author TAEROK HWANG
     */
    public static Predicate condLike(final StringPath path, final String value) {
        return StringUtils.hasText(value) ? path.contains(value) : null;
    }

    /**
     * 날짜 범위 조건 (일치하지 않으면 조건 무시)
     *
     * @param path Querydsl Path
     * @param startDate 시작일
     * @param endDate 종료일
     * @return Predicate 조건
     * @param <T> Comparable (LocalDate, LocalDateTime, ...)
     * @author TAEROK HWANG
     */
    @SuppressWarnings("unchecked")
    public static <T extends Comparable<?>> Predicate condDateBetween(final TemporalExpression<T> path,
                                                                      final LocalDate startDate,
                                                                      final LocalDate endDate) {

        BooleanBuilder builder = new BooleanBuilder();

        if (path instanceof DateTimePath) {
            DateTimePath<LocalDateTime> dateTimePath = (DateTimePath<LocalDateTime>) path;

            if (startDate != null) {
                builder.and(dateTimePath.goe(startDate.atStartOfDay()));
            }
            if (endDate != null) {
                builder.and(dateTimePath.loe(endDate.atTime(LocalTime.MAX)));
            }
        }
        else if (path instanceof DatePath) {
            DatePath<LocalDate> datePath = (DatePath<LocalDate>) path;

            if (startDate != null) {
                builder.and(datePath.goe(startDate));
            }
            if (endDate != null) {
                builder.and(datePath.loe(endDate));
            }
        }

        return builder;
    }

}
