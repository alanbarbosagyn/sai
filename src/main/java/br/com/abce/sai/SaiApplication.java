package br.com.abce.sai;

import br.com.abce.sai.config.AppProperties;
import br.com.abce.sai.converter.ImageConverter;
import br.com.abce.sai.converter.impl.ImageConverterGraphics2D;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/api/*")
						.allowedOrigins(
								"http://localhost:4200",
								"https://mvzrxz.hospedagemelastica.com.br",
								"https://getimoveisgo.com.br",
								"https://feedimoveis.com.br");
			}
		};
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
