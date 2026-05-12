package dev.sobue.ai.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.shell.core.command.annotation.Command;

public class ChatCommand {

  private static final Logger LOGGER = LoggerFactory.getLogger(ChatCommand.class);

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

  @Command(name = "chat", description = "Conversation to AI model")
  public void chat(String prompt) {
    String response = this.chatClient.prompt()
        .messages()
        .user(prompt)
        .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, PID))
        .call()
        .entity(String.class);

    LOGGER.info("AI answers: {}", response);
  }
}
