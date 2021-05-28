package br.com.abce.sai;

import br.com.abce.sai.config.AppProperties;
import br.com.abce.sai.persistence.model.AuthProvider;
import br.com.abce.sai.persistence.model.Imovel;
import br.com.abce.sai.persistence.model.Usuario;
import br.com.abce.sai.persistence.repo.ImovelRepository;
import br.com.abce.sai.persistence.repo.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.stream.Stream;

@EnableJpaRepositories("br.com.abce.sai.persistence.repo") 
@EntityScan("br.com.abce.sai.persistence.model")
@EnableConfigurationProperties(AppProperties.class)
@SpringBootApplication
public class SaiApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(SaiApplication.class, args);
	}

//	@Bean
//	CommandLineRunner init(ImovelRepository imovelRepository, UsuarioRepository usuarioRepository) {
//		return args -> {
//			Stream.of(new Imovel(null, "Casa 3Q c/ suíte", 10000000.00),
//					new Imovel(null,  "Sobrado 4Q e 3 suítes C/2 vagas garagem", 30000000.00),
//					new Imovel(null,  "Apto 2Q c/suíte 2 Vagas de Garagem",  5000000.00)).forEach(
//							item -> {
//								imovelRepository.save(item);
//							});
//
//			imovelRepository.findAll().forEach(System.out::println);
//
//			Stream.of(new Usuario(null, "Usuário Teste","login.teste", "teste@gmai.com", "http://image.com", false, "AJF#JF#JL#JL", AuthProvider.local, AuthProvider.local.name(), null),
//					new Usuario(null, "Teste 2 user", "login2.tes", "teste@uol.com", "http://imagem2.com", false, "651sdf165", AuthProvider.local, AuthProvider.local.name(),null)).forEach(
//					item -> {
//						usuarioRepository.save(item);
//					});
//
//			usuarioRepository.findAll().forEach(System.out::println);
//
//		};
//	}

}
