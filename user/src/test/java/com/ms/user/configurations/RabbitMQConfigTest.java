package com.ms.user.configurations;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ContextConfiguration(classes = {RabbitMQConfig.class})
@ExtendWith(SpringExtension.class)
class RabbitMQConfigTest {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQConfigTest.class);

    @Autowired
    private RabbitMQConfig rabbitMQConfig;

    @Test
    void testMessageConverter() {
        Jackson2JsonMessageConverter converter = rabbitMQConfig.messageConverter();
        assertNotNull(converter, "Jackson2JsonMessageConverter não deve ser nulo");
        assertTrue(converter.getJavaTypeMapper() instanceof DefaultJackson2JavaTypeMapper,
                "JavaTypeMapper deve ser uma instância de DefaultJackson2JavaTypeMapper");
        logger.info("Teste de conversor de mensagem realizado com sucesso.");
    }
}
