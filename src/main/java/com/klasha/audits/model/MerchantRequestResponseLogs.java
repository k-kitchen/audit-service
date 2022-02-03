package com.klasha.audits.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Data
public class MerchantRequestResponseLogs extends BaseEntity{

    public static String tableName = "merchant_request_response_logs";
    public static String entity = "MerchantRequestResponseLogs";



    private String url;
    private String body;
    private String requestTime;
    private String responseTime;
    @Column(columnDefinition = "text")
    private String requestEntity;
    @Column(columnDefinition = "text")
    private String responseEntity;
    private String httpMethod;


}
