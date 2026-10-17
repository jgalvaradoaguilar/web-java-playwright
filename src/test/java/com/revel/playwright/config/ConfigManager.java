package com.revel.playwright.config;

import java.util.Locale;

public final class ConfigManager {
    private static final String DEFAULT_BASE_URL = "https://driverevel.com";

    private ConfigManager() {
    }

    public static Environment environment() {
        return enumValue("ENVIRONMENT", Environment.class, Environment.QA);
    }

    public static BrowserName browser() {
        return enumValue("BROWSER", BrowserName.class, BrowserName.CHROMIUM);
    }

    public static boolean headless() {
        String value = System.getenv("HEADLESS");
        if (value == null || value.isBlank()) {
            return true;
        }
        if ("true".equalsIgnoreCase(value)) {
            return true;
        }
        if ("false".equalsIgnoreCase(value)) {
            return false;
        }
        throw new IllegalStateException("HEADLESS must be set to true or false.");
    }

    public static String baseUrl() {
        String configuredUrl = System.getenv("BASE_URL");
        if (configuredUrl != null && !configuredUrl.isBlank()) {
            return configuredUrl.replaceAll("/+$", "");
        }
        return switch (environment()) {
            case QA, DEV, STAGE, PROD -> DEFAULT_BASE_URL;
        };
    }

    public static String testPhone() {
        return requiredEnvironmentVariable("TEST_PHONE");
    }

    public static String validOtp() {
        return requiredEnvironmentVariable("VALID_OTP");
    }

    public static String invalidOtp() {
        String invalidOtp = System.getenv("INVALID_OTP");
        if (invalidOtp == null || invalidOtp.isBlank()) {
            invalidOtp = "1234";
        }
        if (invalidOtp.equals(validOtp())) {
            throw new IllegalStateException("INVALID_OTP must differ from VALID_OTP.");
        }
        return invalidOtp;
    }

    private static String requiredEnvironmentVariable(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Set the " + name + " environment variable to run the login scenarios.");
        }
        return value;
    }

    private static <T extends Enum<T>> T enumValue(String name, Class<T> enumType, T defaultValue) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        try {
            return Enum.valueOf(enumType, value.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            throw new IllegalStateException(
                    "Unsupported " + name + " value '" + value + "'. Allowed values: "
                            + String.join(", ", java.util.Arrays.stream(enumType.getEnumConstants())
                                    .map(Enum::name)
                                    .toList()),
                    exception);
        }
    }
}
