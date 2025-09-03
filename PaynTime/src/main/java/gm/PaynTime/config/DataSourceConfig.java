package gm.PaynTime.config;



import javax.sql.DataSource;

import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;


@Configuration
public class DataSourceConfig {

	public DataSource datasource(){
		DriverManagerDataSource datasource = new DriverManagerDataSource();
		datasource.setDriverClassName("org.sqlite.JDBC");
		datasource.setUrl("jdbc:sqlite:db/mydatabase.db");
		return datasource;
	}
}
