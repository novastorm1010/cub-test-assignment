package com.example.currency.common;

import io.netty.util.internal.logging.FormattingTuple;
import io.netty.util.internal.logging.MessageFormatter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Slf4j
@Component
@AllArgsConstructor
public class MessageUtils {

    private static MessageSource messageSource;
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:i18n_core");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(-1); // Set cache seconds, -1 for no cache.
        return messageSource;
    }

    public static String getMessage(String key, String lang,  Object... args) {
        if (messageSource == null) {
            AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MessageUtils.class);
            messageSource = context.getBean(MessageSource.class);
        }
        String msg;
        try {
            msg = messageSource.getMessage(key, null, new Locale(lang));
        } catch (Exception ex) {
            msg = key;
            log.warn(ex.getMessage());
        }
        FormattingTuple formattingTuple = MessageFormatter.arrayFormat(msg, args);
        return formattingTuple.getMessage();
    }

    public static String getMessage(String key) {
        return getMessage(key, "en");
    }


}
