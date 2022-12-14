package be.utopia.library.events;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.config.TopicBuilder;

/**
 * TopicFactory is used to automatically register a topic to the Kafka broker for each service
 */
public class TopicFactory {

  /**
   * Should be called once for every service in the class that is annotated with @Configuration
   * @param topic
   * The three-digit code of the service itself
   */
  public static NewTopic initializeKafka(String topic) {
    return TopicBuilder.name(topic)
      .partitions(1)
      .replicas(1)
      .build();
  }
    
}
