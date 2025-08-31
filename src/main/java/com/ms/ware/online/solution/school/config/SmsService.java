package com.ms.ware.online.solution.school.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.ms.ware.online.solution.school.dao.utility.OrganizationMasterDao;
import com.ms.ware.online.solution.school.dto.OrganizationInformation;
import com.ms.ware.online.solution.school.dto.OrganizationInformationData;
import com.ms.ware.online.solution.school.entity.utility.SentSms;
import com.ms.ware.online.solution.school.exception.CustomException;
import com.ms.ware.online.solution.school.model.HibernateUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@Slf4j
public class SmsService {
    private double balance = 0;
    @Getter
    private boolean configured = false;
    private final OkHttpClient client = new OkHttpClient();
    private String token = null;
    @Autowired
    private JsonStringConverter json;
    @Autowired
    private OrganizationMasterDao repository;
    @Autowired
    private OrganizationInformation organizationInformation;
    @Autowired
    private EmailService emailService;
    @Autowired
    private HibernateUtil util;

    @PostConstruct
    public void setConfigured() {

        BikramSambatConverter.set();
        new Thread(() -> {
            util.init();
            emailService.init();
            String sql = "select ifnull(organization_name,name) name,ifnull(municipal,'') municipal,ifnull(ward_no,'') wardNo,ifnull(address,'') street,tel,ifnull(bill_bal_total,'N') balTotal,ifnull(pan_number,'') panNumber from  organization_master";
            List<Map<String, Object>> list = repository.getRecord(sql);
            if (list.isEmpty()) return;
            Map<String, Object> map = list.get(0);
            String organizationName = map.get("name").toString();
            String municipal = map.get("municipal").toString();
            String wardNo = map.get("wardNo").toString();
            String street = map.get("street").toString();
            String tel = map.get("tel").toString();
            String balTotal = map.get("balTotal").toString();
            organizationInformation.setData(OrganizationInformationData.builder()
                    .organizationName(organizationName)
                    .balTotal(balTotal)
                    .street(street)
                    .municipal(municipal)
                    .tel(tel)
                    .panNumber(map.get("panNumber").toString())
                    .wardNo(wardNo)
                    .build());
            list = repository.getRecord("select token from sms_configuration where id=1");
            if (!list.isEmpty()) {
                token = AESUtils.decrypt(list.get(0).get("token").toString());
                getBalance();
            }

        }).start();
    }

    public void setConfigured(String token) {
        new Thread(() -> {
            this.token = token;
            getBalance();
        }).start();
    }

    private void getBalance() {
        if (token != null && token.length() > 10) {
            String apiURL = "https://sms.aakashsms.com/sms/v1/credit?auth_token=" + token;
            try {
                URL url = new URL(apiURL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("User-Agent", "Mozilla/5.0");
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder response = new StringBuilder();
                String output;
                while ((output = in.readLine()) != null) {
                    response.append(output);
                }
                conn.disconnect();
                JsonNode node = json.toJsonNode(response.toString());
                balance = node.get("available_credit").asDouble();
                configured = balance >= 10;
            } catch (Exception ignored) {
                balance = 0.0;
                configured = false;
            }
        } else {
            balance = 0.0;
            configured = false;
        }
    }


    public Map<String, Object> getSmsReport(String dateFrom, String dateTo) {
        try {
            dateTo = DateConverted.toString(DateConverted.addDate(DateConverted.bsToAdDate(dateTo), 1));
            Map<String, Object> map = new HashMap<>();
            map.put("data", repository.getAll("from SentSms where sendDate between '" + DateConverted.toString(DateConverted.bsToAdDate(dateFrom)) + "' and '" + dateTo + "'"));
            map.put("balance", balance);
            return map;
        } catch (Exception e) {
            throw new CustomException(e.getMessage());
        }

    }


    public int sendSMS(String mobile, String message, String username) {
        if (balance < 10) return 0;
        FormBody formBody = new FormBody.Builder().add("to", mobile).add("auth_token", token).add("text", message).build();
        Request request = new Request.Builder().post(formBody).url("https://sms.aakashsms.com/sms/v3/send").build();
        try {
            Response response = client.newCall(request).execute();
            int status = response.code();
            if (status >= 200 && status < 300) {
                InputStream inputStream = (response.body().byteStream());
                StringBuilder sb = new StringBuilder();

                for (int ch; (ch = inputStream.read()) != -1; ) {
                    sb.append((char) ch);
                }

                JsonNode jsonNode = json.toJsonNode(sb.toString());
                int credited = 0;
                for (JsonNode node : jsonNode.get("data").get("valid")) {
                    credited = credited + node.get("credit").asInt();
                }

                repository.save(SentSms.builder().id(UUID.randomUUID().toString()).sms(message).mobile(mobile).smsCount(credited).sendBy(username).sendDate(new Date()).build());
                balance = balance - credited;
                return 1;
            }
            return 0;
        } catch (Exception e) {
            return 0;
        }
    }
}
