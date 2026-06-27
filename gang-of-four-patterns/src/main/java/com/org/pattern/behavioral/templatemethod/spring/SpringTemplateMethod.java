package com.org.pattern.behavioral.templatemethod.spring;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

/**
 * Spring Boot — Template Method Pattern
 *
 * Spring's *Template classes ARE the Template Method pattern.
 * They define the fixed algorithm skeleton (connection handling, error translation,
 * resource cleanup) and expose callback hooks for the variable parts (SQL, mapping).
 *
 * Template classes in Spring:
 *   JdbcTemplate       → fixed: get connection, execute, close, translate exceptions
 *                        variable: SQL string, RowMapper callback
 *   RestTemplate       → fixed: open connection, serialize, execute, close, handle errors
 *                        variable: URL, method, response type
 *   TransactionTemplate→ fixed: begin, commit/rollback, close
 *                        variable: TransactionCallback lambda
 *   JmsTemplate        → fixed: get session, send/receive, close
 *                        variable: destination, MessageCreator callback
 *   MongoTemplate      → fixed: get collection, execute query, close cursor
 *                        variable: Query, entity type
 *   RedisTemplate      → fixed: get connection, serialize, execute, close
 *                        variable: key, value operations
 */

// ── Simplified replication of how JdbcTemplate implements Template Method ─────
abstract class DatabaseTemplate<T> {

    // Template method — fixed skeleton, cannot be overridden
    public final List<T> executeQuery(String sql, Object... params) {
        System.out.println("[Template] acquiring connection...");
        System.out.println("[Template] executing: " + sql);

        List<T> results = mapResults(executeRaw(sql, params));  // variable step

        System.out.println("[Template] closing connection");
        return results;
    }

    // Fixed step — framework handles raw execution
    private String[] executeRaw(String sql, Object[] params) {
        return new String[]{"row1", "row2"};  // simulated resultset
    }

    // Variable step — subclass provides the mapping logic
    protected abstract List<T> mapResults(String[] rawRows);
}

// ── Subclass provides only what varies ───────────────────────────────────────
class OrderDatabaseTemplate extends DatabaseTemplate<String> {

    @Override
    protected List<String> mapResults(String[] rawRows) {
        return List.of(rawRows).stream()
                .map(r -> "Order{" + r + "}")
                .toList();
    }
}

// ── Spring's JdbcTemplate callback style (RowMapper as lambda) ───────────────
class SpringJdbcTemplateExample {

    void demo(JdbcTemplate jdbc) {
        // RowMapper is the "variable step" callback — everything else is fixed by JdbcTemplate
        RowMapper<String> mapper = (rs, rowNum) -> rs.getString("order_id");

        List<String> orders = jdbc.query(
                "SELECT order_id FROM orders WHERE status = ?",
                mapper,
                "ACTIVE"
        );
        System.out.println("Found: " + orders);
    }
}

public class SpringTemplateMethod {

    public static void demo() {
        System.out.println("=== Spring Template Method Pattern Demo ===");

        // Simplified template demo
        DatabaseTemplate<String> template = new OrderDatabaseTemplate();
        List<String> results = template.executeQuery("SELECT * FROM orders WHERE status = ?", "ACTIVE");
        System.out.println("Results: " + results);

        System.out.println("""

            Spring JdbcTemplate — real usage:
              // JdbcTemplate handles: connection, statement, params, ResultSet, exception translation
              // You only provide: SQL + RowMapper callback
              List<Order> orders = jdbcTemplate.query(
                  "SELECT * FROM orders WHERE status = ?",
                  (rs, row) -> new Order(rs.getLong("id"), rs.getString("status")),
                  "ACTIVE"
              );

            TransactionTemplate (template method for transactions):
              transactionTemplate.execute(status -> {
                  orderRepo.save(order);          // variable step
                  inventoryRepo.reduce(order);    // variable step
                  return order.getId();
                  // framework handles: begin, commit, rollback, connection cleanup
              });

            RestTemplate (template method for HTTP):
              String response = restTemplate.getForObject(
                  "https://api.example.com/orders/{id}",
                  String.class, orderId
              );
              // framework handles: URL resolution, serialization, error handling, connection cleanup
              // you provide: URL + response type

            Spring Cloud OpenFeign wraps the same template approach declaratively:
              @FeignClient(name="order-service")
              interface OrderClient {
                  @GetMapping("/orders/{id}")
                  Order findById(@PathVariable String id);
              }
            """);
    }
}
