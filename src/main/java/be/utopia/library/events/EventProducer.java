package be.utopia.library.events;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;

/**
 * EventProducer is used to send ApplicationEvents to other services
 */
public class EventProducer {

	/**
	 * Constructor for EventProducer
	 */
	public EventProducer() {
		Map<String, Object> props = new HashMap<String, Object>();
		
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.10.115:9092");
		props.put(ProducerConfig.RETRIES_CONFIG, 0);
		props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
		props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
		props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		

		DefaultKafkaProducerFactory<String, ApplicationEvent> factory = new DefaultKafkaProducerFactory<String, ApplicationEvent>(props);
		kafkaTemplate = new KafkaTemplate<String, ApplicationEvent>(factory, true);
	}
	
    static KafkaTemplate<String, ApplicationEvent> kafkaTemplate;

	/**
	 * Used to send the ApplicationEvent provided on the Topic of the Kafka broker
	 * @param topic
	 * The topic is name of the microservice, usually defined as a constant in the EventService.
	 * @param event
	 * ApplicationEvent created through its static factory method
	 */
	public void Send(String topic, ApplicationEvent event) {
		String key = event.data.getClass().toString();
		kafkaTemplate.send(topic, key, event);
	}
}