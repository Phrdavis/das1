package br.univille.eventos.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.azure.core.amqp.AmqpTransportType;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusErrorContext;
import com.azure.messaging.servicebus.ServiceBusProcessorClient;
import com.azure.messaging.servicebus.ServiceBusReceivedMessage;
import com.azure.messaging.servicebus.ServiceBusReceivedMessageContext;
import com.azure.messaging.servicebus.ServiceBusSenderClient;

@Configuration(proxyBeanMethods = false)
public class ServiceBusClientConfiguration {

    private static final String SERVICE_BUS_FQDN = "das1univille.servicebus.windows.net";
    private static final String TOPIC_NAME = "das1";
    private static final String SUBSCRIPTION_NAME = "subsdavipinheiro";

    @Bean
    ServiceBusClientBuilder serviceBusClientBuilder() {
        return new ServiceBusClientBuilder()
                   .fullyQualifiedNamespace(SERVICE_BUS_FQDN)
                   .transportType(AmqpTransportType.AMQP_WEB_SOCKETS)
                   .credential(new DefaultAzureCredentialBuilder().build());
    }

    @Bean
    ServiceBusSenderClient serviceBusSenderClient(ServiceBusClientBuilder builder) {
        return builder
               .sender()
               .topicName(TOPIC_NAME)
               .buildClient();
    }

    @Bean
    ServiceBusProcessorClient serviceBusProcessorClient(ServiceBusClientBuilder builder) {
        return builder.processor()
                      .topicName(TOPIC_NAME)
                      .subscriptionName(SUBSCRIPTION_NAME)
                      .processMessage(ServiceBusClientConfiguration::processMessage)
                      .processError(ServiceBusClientConfiguration::processError)
                      .buildProcessorClient();
    }

    private static void processMessage(ServiceBusReceivedMessageContext context) {
        ServiceBusReceivedMessage message = context.getMessage();
        System.out.printf("Processing message. Id: %s, Sequence #: %s. Contents: %s%n",
            message.getMessageId(), message.getSequenceNumber(), message.getBody());
    }

    private static void processError(ServiceBusErrorContext context) {
        System.out.printf("Error when receiving messages from namespace: '%s'. Entity: '%s'%n",
                context.getFullyQualifiedNamespace(), context.getEntityPath());
    }
}