package dev.sobue.ai.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.shell.core.command.annotation.EnableCommand;

@EnableCommand(ChatCommand.class)
@SpringBootApplication
public class SpringAiDemoApplication {

	public static void main(String[] args) {
    SpringApplication.exit(
        SpringApplication.run(SpringAiDemoApplication.class, args));
	}
}
