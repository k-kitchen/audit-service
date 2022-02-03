package com.klasha.audits.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
public class PaymentRequestResponseLogs extends BaseEntity{

    public static String tableName = "payment_request_response_logs";
    public static String entity = "PaymentRequestResponseLogs";



    private String url;
    private String body;
    private String requestTime;
    private String responseTime;
    private String requestEntity;
    private String responseEntity;
    private String httpMethod;


}
