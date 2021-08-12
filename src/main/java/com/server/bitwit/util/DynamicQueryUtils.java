package com.server.bitwit.util;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.*;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.util.StringUtils.hasText;

@NoArgsConstructor(access = PRIVATE)
public class DynamicQueryUtils {
    
    public static Predicate conditions(BooleanExpression... booleanExpressions) {
        BooleanBuilder condition = new BooleanBuilder( );
        Arrays.stream(booleanExpressions).filter(Objects::nonNull).forEach(condition::and);
        return condition.getValue( );
    }
    
    public static Predicate conditions(Collection<BooleanExpression> booleanExpressions) {
        BooleanBuilder condition = new BooleanBuilder( );
        booleanExpressions.stream( ).filter(Objects::nonNull).forEach(condition::and);
        return condition.getValue( );
    }
    
    public static <T> BooleanExpression eq(SimpleExpression<T> left, T right) {
        return equals(left, right);
    }
    
    public static BooleanExpression eq(SimpleExpression<String> left, String right) {
        return equals(left, right);
    }
    
    public static <T> BooleanExpression ne(SimpleExpression<T> left, T right) {
        return notEquals(left, right);
    }
    
    public static BooleanExpression ne(SimpleExpression<String> left, String right) {
        return notEquals(left, right);
    }
    
    public static <T> BooleanExpression equals(SimpleExpression<T> left, T right) {
        return right == null ? null : left.eq(right);
    }
    
    public static BooleanExpression equals(SimpleExpression<String> left, String right) {
        return hasText(right) ? left.eq(right) : null;
    }
    
    public static <T> BooleanExpression notEquals(SimpleExpression<T> left, T right) {
        return right == null ? null : left.ne(right);
    }
    
    public static BooleanExpression notEquals(SimpleExpression<String> left, String right) {
        return hasText(right) ? left.ne(right) : null;
    }
    
    @SafeVarargs
    public static <T> BooleanExpression in(SimpleExpression<T> left, T... right) {
        return right.length == 0 ? null : left.in(right);
    }
    
    public static <T> BooleanExpression in(SimpleExpression<T> left, Collection<? extends T> right) {
        return right == null ? null : left.in(right);
    }
    
    @SafeVarargs
    public static <T> BooleanExpression notIn(SimpleExpression<T> left, T... right) {
        return right.length == 0 ? null : left.notIn(right);
    }
    
    public static <T> BooleanExpression notIn(SimpleExpression<T> left, Collection<? extends T> right) {
        return right == null ? null : left.notIn(right);
    }
    
    /**
     * Abbreviation for {@link #greaterThan(NumberExpression, Number)}
     */
    public static <T extends Number & Comparable<?>> BooleanExpression gt(NumberExpression<T> left, T right) {
        return greaterThan(left, right);
    }
    
    /**
     * Abbreviation for {@link #greaterThan(ComparableExpression, Comparable)}
     */
    public static <T extends Comparable<?>> BooleanExpression gt(ComparableExpression<T> left, T right) {
        return greaterThan(left, right);
    }
    
    /**
     * Abbreviation for {@link #greaterThanOrEqualTo(NumberExpression, Number)}
     */
    public static <T extends Number & Comparable<?>> BooleanExpression goe(NumberExpression<T> left, T right) {
        return greaterThanOrEqualTo(left, right);
    }
    
    /**
     * Abbreviation for {@link #greaterThanOrEqualTo(ComparableExpression, Comparable)}
     */
    public static <T extends Comparable<?>> BooleanExpression goe(ComparableExpression<T> left, T right) {
        return greaterThanOrEqualTo(left, right);
    }
    
    /**
     * Abbreviation for {@link #lessThen(NumberExpression, Number)}
     */
    public static <T extends Number & Comparable<?>> BooleanExpression lt(NumberExpression<T> left, T right) {
        return lessThen(left, right);
    }
    
    /**
     * Abbreviation for {@link #lessThen(ComparableExpression, Comparable)}
     */
    public static <T extends Comparable<?>> BooleanExpression lt(ComparableExpression<T> left, T right) {
        return lessThen(left, right);
    }
    
    /**
     * Abbreviation for {@link #lessThenOrEqualTo(NumberExpression, Number)}
     */
    public static <T extends Number & Comparable<?>> BooleanExpression loe(NumberExpression<T> left, T right) {
        return lessThenOrEqualTo(left, right);
    }
    
    /**
     * Abbreviation for {@link #lessThenOrEqualTo(ComparableExpression, Comparable)}
     */
    public static <T extends Comparable<?>> BooleanExpression loe(ComparableExpression<T> left, T right) {
        return lessThenOrEqualTo(left, right);
    }
    
    public static <T extends Number & Comparable<?>> BooleanExpression greaterThan(NumberExpression<T> left, T right) {
        return right == null ? null : left.gt(right);
    }
    
    public static <T extends Comparable<?>> BooleanExpression greaterThan(ComparableExpression<T> left, T right) {
        return right == null ? null : left.gt(right);
    }
    
    public static <T extends Number & Comparable<?>> BooleanExpression greaterThanOrEqualTo(NumberExpression<T> left, T right) {
        return right == null ? null : left.goe(right);
    }
    
    public static <T extends Comparable<?>> BooleanExpression greaterThanOrEqualTo(ComparableExpression<T> left, T right) {
        return right == null ? null : left.goe(right);
    }
    
    public static <T extends Number & Comparable<?>> BooleanExpression lessThen(NumberExpression<T> left, T right) {
        return right == null ? null : left.lt(right);
    }
    
    public static <T extends Comparable<?>> BooleanExpression lessThen(ComparableExpression<T> left, T right) {
        return right == null ? null : left.lt(right);
    }
    
    public static <T extends Number & Comparable<?>> BooleanExpression lessThenOrEqualTo(NumberExpression<T> left, T right) {
        return right == null ? null : left.loe(right);
    }
    
    public static <T extends Comparable<?>> BooleanExpression lessThenOrEqualTo(ComparableExpression<T> left, T right) {
        return right == null ? null : left.loe(right);
    }
    
    public static <T extends Number & Comparable<?>> BooleanExpression between(NumberExpression<T> target, T from, T to) {
        return (from == null && to == null) ? null : target.between(from, to);
    }
    
    public static <T extends Comparable<?>> BooleanExpression between(ComparableExpression<T> target, T from, T to) {
        return (from == null && to == null) ? null : target.between(from, to);
    }
    
    public static <T extends Number & Comparable<?>> BooleanExpression notBetween(NumberExpression<T> target, T from, T to) {
        return (from == null && to == null) ? null : target.notBetween(from, to);
    }
    
    public static <T extends Comparable<?>> BooleanExpression notBetween(ComparableExpression<T> target, T from, T to) {
        return (from == null && to == null) ? null : target.notBetween(from, to);
    }
    
    public static <T extends Number & Comparable<?>> BooleanExpression like(NumberExpression<T> target, String str) {
        return hasText(str) ? target.like(str) : null;
    }
    
    public static BooleanExpression like(StringExpression target, String str) {
        return hasText(str) ? target.like(str) : null;
    }
    
    public static BooleanExpression like(StringExpression target, String str, char escape) {
        return hasText(str) ? target.like(str, escape) : null;
    }
    
    public static BooleanExpression notLike(StringExpression target, String str) {
        return hasText(str) ? target.notLike(str) : null;
    }
    
    public static BooleanExpression notLike(StringExpression target, String str, char escape) {
        return hasText(str) ? target.notLike(str, escape) : null;
    }
    
    public static BooleanExpression likeIgnoreCase(StringExpression target, String str) {
        return hasText(str) ? target.likeIgnoreCase(str) : null;
    }
    
    public static BooleanExpression likeIgnoreCase(StringExpression target, String str, char escape) {
        return hasText(str) ? target.likeIgnoreCase(str, escape) : null;
    }
    
    public static BooleanExpression matches(StringExpression target, String regex) {
        return hasText(regex) ? target.matches(regex) : null;
    }
    
    public static <T extends Collection<E>, E> BooleanExpression contains(CollectionExpressionBase<T, E> target, E child) {
        return child == null ? null : target.contains(child);
    }
    
    public static BooleanExpression contains(StringExpression target, String str) {
        return hasText(str) ? target.contains(str) : null;
    }
    
    public static BooleanExpression containsIgnoreCase(StringExpression target, String str) {
        return hasText(str) ? target.containsIgnoreCase(str) : null;
    }
    
    public static BooleanExpression startsWith(StringExpression target, String str) {
        return hasText(str) ? target.startsWith(str) : null;
    }
    
    public static BooleanExpression startsWithIgnoreCase(StringExpression target, String str) {
        return hasText(str) ? target.startsWithIgnoreCase(str) : null;
    }
    
    public static BooleanExpression endsWith(StringExpression target, String str) {
        return hasText(str) ? target.endsWith(str) : null;
    }
    
    public static BooleanExpression endsWithIgnoreCase(StringExpression target, String str) {
        return hasText(str) ? target.endsWithIgnoreCase(str) : null;
    }
    
    public static BooleanExpression equalsIgnoreCase(StringExpression target, String str) {
        return hasText(str) ? target.equalsIgnoreCase(str) : null;
    }
    
    public static BooleanExpression notEqualsIgnoreCase(StringExpression target, String str) {
        return hasText(str) ? target.notEqualsIgnoreCase(str) : null;
    }
}
