package server.yto.kafka;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import server.yto.entity.EntityGPS;


@Component
@Slf4j
public class TianMaiKafkaProducer {

    @Value("${kafka_topic}")
    private String tianMaiTopic;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private Gson gson = new GsonBuilder().create();

    public void send(EntityGPS data) {

        log.info("【++++++++++++++++++ message ：{}】", gson.toJson(data));
        //发送消息
        kafkaTemplate.send(tianMaiTopic, gson.toJson(data));
    }


}
