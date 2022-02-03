package com.klasha.audits.listeners;

import com.klasha.audits.model.*;
import com.klasha.audits.repositories.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

@Configuration
@Slf4j
public class MessageListener {

    @Autowired
    AuditRepository auditRepository;

    private final SessionsLogsRepository sessionsLogRepository;

    private final AuthRequestResponseLogsRepository authRequestResponseLogsRepository;

    private final MarketRequestResponseLogsRepository marketRequestResponseLogsRepository;

    private final MerchantRequestResponseLogsRepository merchantRequestResponseLogsRepository;

    private final NotificationRequestResponseLogsRepository notificationRequestResponseLogsRepository;

    private final NucleusRequestResponseLogsRepository nucleusRequestResponseLogsRepository;

    private final PaymentRequestResponseLogsRepository paymentRequestResponseLogsRepository;

    private final WalletRequestResponseLogsRepository walletRequestResponseLogsRepository;

    private CountDownLatch latch = new CountDownLatch(3);

    private CountDownLatch partitionLatch = new CountDownLatch(2);

    private CountDownLatch filterLatch = new CountDownLatch(2);

    private CountDownLatch greetingLatch = new CountDownLatch(1);

    public MessageListener(SessionsLogsRepository sessionsLogRepository, AuthRequestResponseLogsRepository authRequestResponseLogsRepository, MarketRequestResponseLogsRepository marketRequestResponseLogsRepository, MerchantRequestResponseLogsRepository merchantRequestResponseLogsRepository, NotificationRequestResponseLogsRepository notificationRequestResponseLogsRepository, NucleusRequestResponseLogsRepository nucleusRequestResponseLogsRepository, PaymentRequestResponseLogsRepository paymentRequestResponseLogsRepository, WalletRequestResponseLogsRepository walletRequestResponseLogsRepository) {
        this.sessionsLogRepository = sessionsLogRepository;
        this.authRequestResponseLogsRepository = authRequestResponseLogsRepository;
        this.marketRequestResponseLogsRepository = marketRequestResponseLogsRepository;
        this.merchantRequestResponseLogsRepository = merchantRequestResponseLogsRepository;
        this.notificationRequestResponseLogsRepository = notificationRequestResponseLogsRepository;
        this.nucleusRequestResponseLogsRepository = nucleusRequestResponseLogsRepository;
        this.paymentRequestResponseLogsRepository = paymentRequestResponseLogsRepository;
        this.walletRequestResponseLogsRepository = walletRequestResponseLogsRepository;
    }


    @KafkaListener(topics = "${authentication.topic.name}", groupId = "auth", containerFactory = "greetingKafkaListenerContainerFactory")
    public void listenGroupFoo(Object message) {
        System.out.println("Received Message in group 'auth': " + message.toString());
        ConsumerRecord<String, Object> consumerRecord = (ConsumerRecord<String, Object>) message;
        System.out.println(consumerRecord.toString());
        ModelMapper modelMapper = new ModelMapper();
        System.out.println("Received Message in group 'auth': " + consumerRecord.value());
        ObjectHistory objectHistory = modelMapper.map(consumerRecord.value(), ObjectHistory.class);
        System.out.println("Received Message in group 'auth': " + objectHistory.toString());
        auditRepository.save(objectHistory);
        latch.countDown();
    }

    @KafkaListener(topics = "${nucleus.topic.name}", groupId = "bar", containerFactory = "barKafkaListenerContainerFactory")
    public void listenGroupBar(String message) {
        System.out.println("Received Message in group 'bar': " + message);
        latch.countDown();
    }

    @KafkaListener(topics = "${merchant.topic.name}", containerFactory = "headersKafkaListenerContainerFactory")
    public void listenWithHeaders(@Payload String message, @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
        System.out.println("Received Message: " + message + " from partition: " + partition);
        latch.countDown();
    }

    @KafkaListener(topicPartitions = @TopicPartition(topic = "${payment.topic.name}", partitions = {"0", "3"}), containerFactory = "partitionsKafkaListenerContainerFactory")
    public void listenToPartition(@Payload String message, @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
        System.out.println("Received Message: " + message + " from partition: " + partition);
        this.partitionLatch.countDown();
    }

    @KafkaListener(topics = "${payment.topic.name}", containerFactory = "filterKafkaListenerContainerFactory")
    public void listenWithFilter(String message) {
        System.out.println("Received Message in filtered listener: " + message);
        this.filterLatch.countDown();
    }

    @KafkaListener(topics = "auth_req_log", groupId = "auth_req_log", containerFactory = "authKafkaListenerContainerFactory")
    public void listenAuthFilter(Object message) {
        System.out.println("Received Message in filtered auth listener: " + message);
        ConsumerRecord<String, Object> consumerRecord = (ConsumerRecord<String, Object>) message;

        Map<String, Object> map = (Map<String, Object>) consumerRecord.value();
        log.info("here is the map: " +map);

        AuthRequestResponseLogs authRequestResponseLogs = new AuthRequestResponseLogs();
        authRequestResponseLogs.setBody(String.valueOf(map.get("body")));
        authRequestResponseLogs.setUrl(String.valueOf(map.get("url")));
        authRequestResponseLogs.setRequestTime(String.valueOf(map.get("requestTime")));
        authRequestResponseLogs.setResponseTime(String.valueOf(map.get("responseTime")));
        authRequestResponseLogs.setHttpMethod(String.valueOf(map.get("httpMethod")));
        authRequestResponseLogs.setRequestEntity(String.valueOf(map.get("entity")));
        authRequestResponseLogs.setResponseEntity(String.valueOf(map.get("responseEntity")));

//        System.out.println("After converting::::" + authRequestResponseLogs.toString());

        authRequestResponseLogsRepository.save(authRequestResponseLogs);
        System.out.println("Auth Saved!");

        latch.countDown();
    }

    @KafkaListener(topics = "market_req_log", groupId = "market_req_log", containerFactory = "marketKafkaListenerContainerFactory")
    public void listenMarketFilter(Object message) {
        System.out.println("Received Message in filtered market listener: " + message);
        ConsumerRecord<String, Object> consumerRecord = (ConsumerRecord<String, Object>) message;

        Map<String, Object> map = (Map<String, Object>) consumerRecord.value();
        log.info("here is the map: " +map);

        MarketRequestResponseLogs marketRequestResponseLogs = new MarketRequestResponseLogs();
        marketRequestResponseLogs.setBody(String.valueOf(map.get("body")));
        marketRequestResponseLogs.setUrl(String.valueOf(map.get("url")));
        marketRequestResponseLogs.setRequestTime(String.valueOf(map.get("requestTime")));
        marketRequestResponseLogs.setResponseTime(String.valueOf(map.get("responseTime")));
        marketRequestResponseLogs.setHttpMethod(String.valueOf(map.get("httpMethod")));
        marketRequestResponseLogs.setRequestEntity(String.valueOf(map.get("entity")));
        marketRequestResponseLogs.setResponseEntity(String.valueOf(map.get("responseEntity")));

        System.out.println("After converting::::" + marketRequestResponseLogs.toString());

//        marketRequestResponseLogsRepository.save(marketRequestResponseLogs);
        System.out.println("Market Saved!");

        latch.countDown();
    }

    @KafkaListener(topics = "merchant_req_log", groupId = "merchant_req_log", containerFactory = "merchantKafkaListenerContainerFactory")
    public void listenMerchantFilter(Object message) {
        System.out.println("Received Message in filtered merchant listener: " + message);
        ConsumerRecord<String, Object> consumerRecord = (ConsumerRecord<String, Object>) message;

        Map<String, Object> map = (Map<String, Object>) consumerRecord.value();
        log.info("here is the map: " +map);

        MerchantRequestResponseLogs merchantRequestResponseLogs = new MerchantRequestResponseLogs();
        merchantRequestResponseLogs.setBody(String.valueOf(map.get("body")));
        merchantRequestResponseLogs.setUrl(String.valueOf(map.get("url")));
        merchantRequestResponseLogs.setRequestTime(String.valueOf(map.get("requestTime")));
        merchantRequestResponseLogs.setResponseTime(String.valueOf(map.get("responseTime")));
        merchantRequestResponseLogs.setHttpMethod(String.valueOf(map.get("httpMethod")));
        merchantRequestResponseLogs.setRequestEntity(String.valueOf(map.get("entity")));
        merchantRequestResponseLogs.setResponseEntity(String.valueOf(map.get("responseEntity")));

//        System.out.println("After converting::::" + merchantRequestResponseLogs.toString());

        merchantRequestResponseLogsRepository.save(merchantRequestResponseLogs);
        System.out.println("Merchant Saved!");

        latch.countDown();
    }

    @KafkaListener(topics = "notification_req_log", groupId = "notification_req_log", containerFactory = "notificationKafkaListenerContainerFactory")
    public void listenNotificationFilter(Object message) {
        System.out.println("Received Message in filtered notification listener: " + message);
        ConsumerRecord<String, Object> consumerRecord = (ConsumerRecord<String, Object>) message;

        Map<String, Object> map = (Map<String, Object>) consumerRecord.value();
        log.info("here is the map: " +map);

        NotificationRequestResponseLogs notificationRequestResponseLogs = new NotificationRequestResponseLogs();
        notificationRequestResponseLogs.setBody(String.valueOf(map.get("body")));
        notificationRequestResponseLogs.setUrl(String.valueOf(map.get("url")));
        notificationRequestResponseLogs.setRequestTime(String.valueOf(map.get("requestTime")));
        notificationRequestResponseLogs.setResponseTime(String.valueOf(map.get("responseTime")));
        notificationRequestResponseLogs.setHttpMethod(String.valueOf(map.get("httpMethod")));
        notificationRequestResponseLogs.setRequestEntity(String.valueOf(map.get("entity")));
        notificationRequestResponseLogs.setResponseEntity(String.valueOf(map.get("responseEntity")));

//        System.out.println("After converting::::" + notificationRequestResponseLogs.toString());

        notificationRequestResponseLogsRepository.save(notificationRequestResponseLogs);
        System.out.println("Notification Saved!");

        latch.countDown();
    }

    @KafkaListener(topics = "nucleus_req_log", groupId = "nucleus_req_log", containerFactory = "nucleusKafkaListenerContainerFactory")
    public void listenNucleusFilter(Object message) {
        System.out.println("Received Message in filtered nucleus listener: " + message);
        ConsumerRecord<String, Object> consumerRecord = (ConsumerRecord<String, Object>) message;

        Map<String, Object> map = (Map<String, Object>) consumerRecord.value();
        log.info("here is the map: " +map);

        NucleusRequestResponseLogs nucleusRequestResponseLogs = new NucleusRequestResponseLogs();
        nucleusRequestResponseLogs.setBody(String.valueOf(map.get("body")));
        nucleusRequestResponseLogs.setUrl(String.valueOf(map.get("url")));
        nucleusRequestResponseLogs.setRequestTime(String.valueOf(map.get("requestTime")));
        nucleusRequestResponseLogs.setResponseTime(String.valueOf(map.get("responseTime")));
        nucleusRequestResponseLogs.setHttpMethod(String.valueOf(map.get("httpMethod")));
        nucleusRequestResponseLogs.setRequestEntity(String.valueOf(map.get("entity")));
        nucleusRequestResponseLogs.setResponseEntity(String.valueOf(map.get("responseEntity")));

//        System.out.println("After converting::::" + nucleusRequestResponseLogs.toString());

        nucleusRequestResponseLogsRepository.save(nucleusRequestResponseLogs);
        System.out.println("Nucleus Saved!");

        latch.countDown();
    }

    @KafkaListener(topics = "payment_req_log", groupId = "payment_req_log", containerFactory = "paymentKafkaListenerContainerFactory")
    public void listenPaymentFilter(Object message) {
        System.out.println("Received Message in filtered payment listener: " + message);
        ConsumerRecord<String, Object> consumerRecord = (ConsumerRecord<String, Object>) message;

        Map<String, Object> map = (Map<String, Object>) consumerRecord.value();
        log.info("here is the map: " +map);

        PaymentRequestResponseLogs paymentRequestResponseLogs = new PaymentRequestResponseLogs();
        paymentRequestResponseLogs.setBody(String.valueOf(map.get("body")));
        paymentRequestResponseLogs.setUrl(String.valueOf(map.get("url")));
        paymentRequestResponseLogs.setRequestTime(String.valueOf(map.get("requestTime")));
        paymentRequestResponseLogs.setResponseTime(String.valueOf(map.get("responseTime")));
        paymentRequestResponseLogs.setHttpMethod(String.valueOf(map.get("httpMethod")));
        paymentRequestResponseLogs.setRequestEntity(String.valueOf(map.get("entity")));
        paymentRequestResponseLogs.setResponseEntity(String.valueOf(map.get("responseEntity")));

//        System.out.println("After converting::::" + paymentRequestResponseLogs.toString());

        paymentRequestResponseLogsRepository.save(paymentRequestResponseLogs);
        System.out.println("Payment Saved!");

        latch.countDown();
    }

    @KafkaListener(topics = "wallet_req_log", groupId = "wallet_req_log", containerFactory = "walletKafkaListenerContainerFactory")
    public void listenWalletFilter(Object message) {
        System.out.println("Received Message in filtered wallet listener: " + message);
        ConsumerRecord<String, Object> consumerRecord = (ConsumerRecord<String, Object>) message;

        Map<String, Object> map = (Map<String, Object>) consumerRecord.value();
        log.info("here is the map: " +map);

        WalletRequestResponseLogs walletRequestResponseLogs = new WalletRequestResponseLogs();
        walletRequestResponseLogs.setBody(String.valueOf(map.get("body")));
        walletRequestResponseLogs.setUrl(String.valueOf(map.get("url")));
        walletRequestResponseLogs.setRequestTime(String.valueOf(map.get("requestTime")));
        walletRequestResponseLogs.setResponseTime(String.valueOf(map.get("responseTime")));
        walletRequestResponseLogs.setHttpMethod(String.valueOf(map.get("httpMethod")));
        walletRequestResponseLogs.setRequestEntity(String.valueOf(map.get("entity")));
        walletRequestResponseLogs.setResponseEntity(String.valueOf(map.get("responseEntity")));

//        System.out.println("After converting::::" + walletRequestResponseLogs.toString());

        walletRequestResponseLogsRepository.save(walletRequestResponseLogs);
        System.out.println("Wallet Saved!");

        latch.countDown();
    }

    @KafkaListener(topics = "auditRequest", groupId = "auditRequest", containerFactory = "auditRequestKafkaListenerContainerFactory")
    public void auditRequest(Object message) {
        System.out.println("Received Message in filtered audit Request: " + message);
        ConsumerRecord<String, Object> consumerRecord = (ConsumerRecord<String, Object>) message;

        Map<String, Object> map = (Map<String, Object>) consumerRecord.value();
        log.info("here is the map: {}", map);
        SessionLog cacheLog = SessionLog.builder().request(String.valueOf(map.get("request"))).response(String.valueOf(map.get("response"))).createdAt(new Date()).updatedAt(new Date()).build();
        sessionsLogRepository.save(cacheLog);
        latch.countDown();
    }

    @KafkaListener(topics = "auditResponse", groupId = "auditResponse", containerFactory = "auditResponseKafkaListenerContainerFactory")
    public void auditResponse(String message) {
        SessionLog cacheLog = SessionLog.builder().response(message).createdAt(new Date()).updatedAt(new Date()).build();
        System.out.println("Audit Response" + message);
        sessionsLogRepository.save(cacheLog);
        latch.countDown();
    }

   /* @KafkaListener(topics = "${greeting.topic.name}", containerFactory = "greetingKafkaListenerContainerFactory")
    public void greetingListener(Greeting greeting) {
        System.out.println("Received greeting message: " + greeting);
        this.greetingLatch.countDown();
    }*/


}
