package com.dtt.organization.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Locale;

@Configuration
public class AppConfiguration {


    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource =
                new ReloadableResourceBundleMessageSource();

        messageSource.setBasenames(
                "classpath:/messages/api_error_messages",
                "classpath:/messages/api_response_messages"
        );

        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setFallbackToSystemLocale(false);

        return messageSource;
    }


    @Bean
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver resolver =
                new AcceptHeaderLocaleResolver();

        resolver.setDefaultLocale(Locale.ENGLISH);
        return resolver;
    }

}
