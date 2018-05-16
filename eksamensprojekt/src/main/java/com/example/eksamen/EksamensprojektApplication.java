package com.example.eksamen;

import com.example.eksamen.Repository.RestordreRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EksamensprojektApplication {

	public static void main(String[] args) {
		RestordreRepository repository = RestordreRepository.getInstance();
		repository.table();
		SpringApplication.run(EksamensprojektApplication.class, args);
	}
}
