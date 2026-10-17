package com.revel.playwright.support;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType.LaunchOptions;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.revel.playwright.config.ConfigManager;

public final class BrowserSession {
    private static final ThreadLocal<BrowserSession> CURRENT = new ThreadLocal<>();

    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    private Page page;

    private BrowserSession() {
    }

    public static void start() {
        BrowserSession session = new BrowserSession();
        CURRENT.set(session);

        session.playwright = Playwright.create();
        session.browser = switch (ConfigManager.browser()) {
            case CHROMIUM -> session.playwright.chromium().launch(
                    new LaunchOptions().setHeadless(ConfigManager.headless()));
            case FIREFOX -> session.playwright.firefox().launch(
                    new LaunchOptions().setHeadless(ConfigManager.headless()));
            case WEBKIT -> session.playwright.webkit().launch(
                    new LaunchOptions().setHeadless(ConfigManager.headless()));
        };
        session.context = session.browser.newContext();
        session.page = session.context.newPage();
    }

    public static Page page() {
        BrowserSession session = CURRENT.get();
        if (session == null || session.page == null) {
            throw new IllegalStateException("The browser session has not been started.");
        }
        return session.page;
    }

    public static void stop() {
        BrowserSession session = CURRENT.get();
        if (session == null) {
            return;
        }

        Throwable failure = null;
        if (session.page != null) {
            failure = close(failure, session.page::close);
        }
        if (session.context != null) {
            failure = close(failure, session.context::close);
        }
        if (session.browser != null) {
            failure = close(failure, session.browser::close);
        }
        if (session.playwright != null) {
            failure = close(failure, session.playwright::close);
        }
        CURRENT.remove();

        if (failure instanceof RuntimeException runtimeException) {
            throw runtimeException;
        }
        if (failure instanceof Error error) {
            throw error;
        }
    }

    private static Throwable close(Throwable previousFailure, Runnable closeAction) {
        try {
            closeAction.run();
        } catch (RuntimeException | Error exception) {
            if (previousFailure == null) {
                return exception;
            }
            previousFailure.addSuppressed(exception);
        }
        return previousFailure;
    }
}
