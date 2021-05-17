package br.com.abce.sai;

import br.com.abce.sai.persistence.model.Imovel;
import br.com.abce.sai.persistence.repo.ImovelRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.stream.Stream;

@EnableJpaRepositories("br.com.abce.sai.persistence.repo") 
@EntityScan("br.com.abce.sai.persistence.model")
@SpringBootApplication
public class SaiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SaiApplication.class, args);
	}

	@Bean
	CommandLineRunner init(ImovelRepository imovelRepository) {
		return args -> {
			Stream.of("Casa 3Q c/ suíte", "Sobrado 4Q e 3 suítes C/2 vagas garagem",
					"Apto 2Q c/suíte 2 Vagas de Garagem").forEach(
							name -> {
								Imovel imovel = new Imovel();
								imovel.setNome(name);
								imovel.setValor(10000000.00);
								imovelRepository.save(imovel);
							});

			imovelRepository.findAll().forEach(System.out::println);
		};
	}

}
