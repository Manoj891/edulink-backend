package com.ms.ware.online.solution.school;

import com.ms.ware.online.solution.school.model.DatabaseName;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpringBootWebApplication {
    public static void main(String[] args) {
        if (args.length == 4) {
            System.setProperty("server.port", args[0]);
            System.setProperty("server.servlet.context-path", "/edulink/" + args[0]);
            System.setProperty("logging.file.name", "/home/tomcat/webapps/logs/" + args[0] + ".log");
            DatabaseName.setDatabase(args[1]);
            DatabaseName.setPort(args[2]);
            DatabaseName.setDocumentUrl(args[3]);
        } else {
            System.setProperty("server.port", "9092");
            System.setProperty("server.servlet.context-path", "/edulink/9092");
            DatabaseName.setDocumentUrl("krishnaratna");
        }
        SpringApplication.run(SpringBootWebApplication.class, args);

    }
}
