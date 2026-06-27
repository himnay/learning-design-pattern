package com.org.pattern.structural.adapter.spring;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

/**
 * Spring Boot — Adapter Pattern
 *
 * JdbcTemplate IS the Adapter pattern.
 * - Target interface : the clean API your code calls (query, update, execute)
 * - Adaptee         : raw JDBC (Connection, PreparedStatement, ResultSet)
 * - Adapter         : JdbcTemplate — translates your clean calls into JDBC ceremony
 *
 * Without the adapter, every query needs 10–15 lines of boilerplate:
 *   open connection → prepare statement → set params → execute → map rows →
 *   close result set → close statement → close connection → handle exceptions.
 *
 * JdbcTemplate wraps all that and exposes a 1-line query API.
 *
 * Same pattern appears in:
 *   JdbcTemplate         → adapts raw JDBC
 *   MongoTemplate        → adapts MongoDB Java driver
 *   RedisTemplate        → adapts Jedis / Lettuce
 *   KafkaTemplate        → adapts Kafka Producer API
 *   AmqpTemplate         → adapts RabbitMQ Channel API
 *   RestTemplate         → adapts Apache HttpClient / OkHttp
 */

// ── Domain model ──────────────────────────────────────────────────────────────
record Product(Long id, String name, double price) {}

// ── Repository using JdbcTemplate (the adapter) ──────────────────────────────
@Repository
class ProductRepository {

    private final JdbcTemplate jdbc; // <-- the adapter

    public ProductRepository(DataSource dataSource) {
        this.jdbc = new JdbcTemplate(dataSource);
    }

    // Clean 1-line API — JdbcTemplate adapts this to full JDBC lifecycle
    public List<Product> findByPriceBelow(double maxPrice) {
        RowMapper<Product> mapper = (rs, row) ->
                new Product(rs.getLong("id"), rs.getString("name"), rs.getDouble("price"));

        return jdbc.query("SELECT id, name, price FROM product WHERE price < ?", mapper, maxPrice);
    }

    public int save(Product p) {
        return jdbc.update("INSERT INTO product (name, price) VALUES (?, ?)", p.name(), p.price());
    }
}

public class SpringJdbcAdapter {

    public static void demo() {
        System.out.println("=== Spring Adapter Pattern — JdbcTemplate Demo ===");
        System.out.println("""
            Without adapter (raw JDBC — 15 lines per query):
              Connection conn = dataSource.getConnection();
              PreparedStatement ps = conn.prepareStatement("SELECT ...");
              ps.setDouble(1, maxPrice);
              ResultSet rs = ps.executeQuery();
              while (rs.next()) { ... }
              rs.close(); ps.close(); conn.close(); // + try/catch/finally

            With JdbcTemplate adapter (1 line):
              return jdbc.query("SELECT * FROM product WHERE price < ?", mapper, maxPrice);

            Other Spring Template Adapters:
              MongoTemplate  → adapts MongoDB driver   → mongoTemplate.find(query, Product.class)
              RedisTemplate  → adapts Lettuce/Jedis    → redisTemplate.opsForValue().set(k, v)
              KafkaTemplate  → adapts Kafka Producer   → kafkaTemplate.send(topic, key, payload)
              AmqpTemplate   → adapts RabbitMQ Channel → amqpTemplate.convertAndSend(exchange, key, msg)
              RestTemplate   → adapts HttpClient       → restTemplate.getForObject(url, Product.class)
            """);
    }
}
