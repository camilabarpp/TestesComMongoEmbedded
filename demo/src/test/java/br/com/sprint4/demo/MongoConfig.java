package br.com.sprint4.demo;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import com.mongodb.client.MongoClients;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.ImmutableMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfig;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;


@TestConfiguration
@TestPropertySource(properties = "spring.mongodb.embedded.version=3.2.8")
public class MongoConfig {
    private static final String CONNECTION_STRING = "mongodb://%s:%d";

    @Value("${spring.data.mongodb.host:localhost}")
    String ip;
    @Value("${spring.data.mongodb.port:27017}")
    int port;


    @Bean
    public MongoTemplate mongoTemplate() throws IOException {
        return new MongoTemplate(MongoClients.create(String.format(CONNECTION_STRING, ip, port)), "test");

    }

    @Bean
    public MongodExecutable mongodExecutable() throws IOException {
        ImmutableMongodConfig mongodConfig = MongodConfig
                .builder()
                .isShardServer(true)
                .version(Version.Main.PRODUCTION)
                .net(new Net(ip, port, Network.localhostIsIPv6()))
                .build();

        MongodStarter starter = MongodStarter.getDefaultInstance();
        return starter.prepare(mongodConfig);
    }
}
