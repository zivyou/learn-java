package com.zivyou.learnseata;

import io.seata.saga.engine.StateMachineEngine;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(exclude = {
		SecurityAutoConfiguration.class,
})
@EnableJpaRepositories
public class LearnSeataApplication {

	public static void main(String[] args) {
		ApplicationContext applicationContext = SpringApplication.run(LearnSeataApplication.class, args);
		StateMachineEngine stateMachineEngine = (StateMachineEngine) applicationContext.getBean("stateMachineEngine");
		System.out.println(stateMachineEngine.getStateMachineConfig());
	}

}
