package be.utopia.library.events;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@Configuration
public class CustomConsumerFactory {
    @Bean
    public ConsumerFactory<String, ApplicationEvent> consumerFactory(){
        Map<String, Object> props = new HashMap<String, Object>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.10.115:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "group_id");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        DefaultKafkaConsumerFactory<String, ApplicationEvent> factory = new DefaultKafkaConsumerFactory<String, ApplicationEvent>(props);
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ApplicationEvent> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ApplicationEvent> factory = new ConcurrentKafkaListenerContainerFactory<String, ApplicationEvent>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}