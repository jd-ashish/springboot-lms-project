package com.user.app;

import com.google.gson.Gson;
import com.user.app.dto.QuestionExerciseDto;
import com.user.app.entity.Exercise;
import com.user.app.entity.QuestionExercise;
import com.user.app.repositories.ExerciseRepository;
import com.user.app.repositories.QuestionExerciseRepository;
import com.user.app.service.QuestionExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class SmartcontactmanagerApplication implements CommandLineRunner {


	public static void main(String[] args) {
		SpringApplication.run(SmartcontactmanagerApplication.class, args);


	}

	@Override
	public void run(String... args) throws Exception {

	}
}
