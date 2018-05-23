package com.example.eksamen;

import com.example.eksamen.Repository.RestordreRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EksamensprojektApplication {

	public static void main(String[] args) {
		DataClean dc = DataClean.getInstance();
		dc.table();
		SpringApplication.run(EksamensprojektApplication.class, args);
	}
}
