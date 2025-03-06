package com.gestionEnseignement.repartition;

import com.gestionEnseignement.repartition.model.Enseignant;
import com.gestionEnseignement.repartition.model.Enseignement;
import com.gestionEnseignement.repartition.service.EnseignantService;
import com.gestionEnseignement.repartition.service.EnseignementService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@EnableFeignClients
@SpringBootApplication
public class RepartitionApplication {

	public static void main(String[] args) {
		SpringApplication.run(RepartitionApplication.class, args);
	}

	@Bean
	public CommandLineRunner start(EnseignantService enseignantService, EnseignementService enseignementService) {
		return args -> {
			for (int i = 0; i < 5; i++) {
				enseignantService.save(new Enseignant());
				enseignementService.save(new Enseignement());
			}
		};
	}

}
