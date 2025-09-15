package dev.sobue.ai.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@Slf4j
@ShellComponent
public class ChatCommand {

  private static final String PID =
      java.lang.management.ManagementFactory.getRuntimeMXBean().getName().split("@")[0];

  private final ChatClient chatClient;

  public ChatCommand(
      ChatClient.Builder chatClientBuilder,
      ChatMemoryRepository chatMemoryRepository) {
    this.chatClient = chatClientBuilder
        .defaultAdvisors(
            MessageChatMemoryAdvisor.builder(
                MessageWindowChatMemory.builder()
                    .chatMemoryRepository(chatMemoryRepository)
                    .build())
                .build())
        .build();
  }

  @ShellMethod("Conversation to AI model")
  public void chat(String prompt) {
    String response = this.chatClient.prompt()
        .messages()
        .user(prompt)
        .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, PID))
        .call()
        .entity(String.class);

    log.info("AI answers: {}", response);
  }
}
