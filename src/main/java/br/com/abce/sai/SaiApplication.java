package br.com.abce.sai;

import br.com.abce.sai.config.AppProperties;
import br.com.abce.sai.converter.ImageConverter;
import br.com.abce.sai.converter.impl.ImageConverterGraphics2D;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

@EnableJpaRepositories("br.com.abce.sai.persistence.repo")
@EntityScan("br.com.abce.sai.persistence.model")
@EnableConfigurationProperties(AppProperties.class)
@SpringBootApplication
public class SaiApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(SaiApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	public ImageConverter imageConverter() {
		return new ImageConverterGraphics2D();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

//	@Bean
//	public DataSource getDataSource() {
//		DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
//		dataSourceBuilder.driverClassName("com.mysql.cj.jdbc.Driver");
//		dataSourceBuilder.url("jdbc:mysql://localhost:3306/sai?useTimezone=true&serverTimezone=UTC");
//		dataSourceBuilder.username("sai");
//		dataSourceBuilder.password("sai@2021");
//		return dataSourceBuilder.build();
//	}

//	@Bean
//	public DataSource dataSource() throws NamingException {
//		return (DataSource) new JndiTemplate().lookup("jdbc/sai");
//	}
}
