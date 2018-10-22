package ru.stegnin.virtualbox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class VirtualboxApplication {

    public static void main(String[] args) {
        SpringApplication.run(VirtualboxApplication.class, args);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // TODO: 14.10.2018 Добавить пользователям workspaces, settings
    // TODO: 14.10.2018 Добавить в приложение help (справка с описанием методов)
    // TODO: 14.10.2018 Добавить работу с файлами (включая импорт и экспорт)
    // TODO: 14.10.2018 Добавить импорт/экспорт папки
    // TODO: 14.10.2018 Добавить синхронизацию
    // TODO: 21.10.2018 Добавить страницу для работы с ролями
}
