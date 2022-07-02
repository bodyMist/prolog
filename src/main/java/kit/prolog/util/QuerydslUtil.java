package kit.prolog.util;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.Expressions;
import org.springframework.data.domain.Sort;

public abstract class QuerydslUtil {
    public static OrderSpecifier[] ordersFromPageable(Sort sort) {
        return sort.stream()
                .map(order -> new OrderSpecifier(
                        order.isAscending() ? Order.ASC : Order.DESC,
                        Expressions.path(Object.class, order.getProperty())))
                .toArray(OrderSpecifier[]::new);
    }
}
