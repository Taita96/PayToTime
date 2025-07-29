package gm.PaynTime.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.springframework.stereotype.Component;


import org.springframework.core.env.Environment;

@Component
public class SQLiteService {

	private final String jdbcUrl;

    public SQLiteService(Environment env) {
        this.jdbcUrl = env.getProperty("spring.datasource.url");
    }

    public Connection connect() throws SQLException {
        return DriverManager.getConnection(jdbcUrl);
    }
}
