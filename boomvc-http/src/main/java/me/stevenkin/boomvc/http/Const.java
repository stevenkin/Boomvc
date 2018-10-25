/**
 * Copyright (c) 2017, biezhi 王爵 (biezhi.me@gmail.com)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package me.stevenkin.boomvc.http;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface Const {

    int DEFAULT_SERVER_PORT = 9000;
    String DEFAULT_SERVER_ADDRESS = "0.0.0.0";
    String LOCAL_IP_ADDRESS = "127.0.0.1";
    String CONTENT_TYPE_HTML = "text/html; charset=UTF-8";
    String CONTENT_TYPE_JSON = "application/json; charset=UTF-8";
    String CONTENT_TYPE_TEXT = "text/plain; charset=UTF-8";
    String HTTP_DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss zzz";
    String INTERNAL_SERVER_ERROR_HTML = "<center><h1>500 Internal Server Error</h1><hr/></center>";
    String DEFAULT_THREAD_NAME = "boom_thread_2333_";
    String DEFAULT_BANNER_PATH = "/banner.txt";
    List<String> DEFAULT_STATICS = new ArrayList<>(
            Arrays.asList("/favicon.ico", "/robots.txt", "/static", "/upload", "/webjars/"));

    String PROP_NAME0 = "classpath:app.properties";
    String PROP_NAME = "classpath:application.properties";
    String DEFAULT_SCAN_PACKAGE = "me.stevenkin.boom.mvc";

    // Env key
    String ENV_KEY_APP_NAME = "app.name";
    String ENV_KEY_APP_THREAD_NAME = "app.thread-name";
    String ENV_KEY_BANNER_PATH = "app.banner-path";
    String ENV_KEY_CONTEXT_PATH = "app.context-path";
    String ENV_KEY_GZIP_ENABLE = "http.gzip.enable";
    String ENV_KEY_CORS_ENABLE = "http.cors.enable";
    String ENV_KEY_SESSION_KEY = "http.session.key";
    String ENV_KEY_SESSION_TIMEOUT = "http.session.timeout";
    String ENV_KEY_AUTH_USERNAME = "http.auth.username";
    String ENV_KEY_AUTH_PASSWORD = "http.auth.password";
    String ENV_KEY_HTTP_CACHE_TIMEOUT = "http.cache.timeout";
    String ENV_KEY_HTTP_REQUEST_COST = "http.request.cost";
    String ENV_KEY_PAGE_404 = "mvc.view.404";
    String ENV_KEY_PAGE_500 = "mvc.view.500";
    String ENV_KEY_STATIC_DIRS = "mvc.statics";
    String ENV_KEY_STATIC_LIST = "mvc.statics.show-list";
    String ENV_KEY_IOC_PACKAGES = "mvc.ioc.packages";
    String ENV_KEY_TEMPLATE_PATH = "mvc.template.path";
    String ENV_KEY_SERVER_ADDRESS = "server.address";
    String ENV_KEY_SERVER_PORT = "server.port";
    String ENV_KEY_SERVER_ACCEPT_THREAD_COUNT = "server.accept-thread.count";
    String ENV_KEY_SERVER_IO_THREAD_COUNT = "server.io-thread.count";

    int DEFAULT_SO_BACKLOG = 1024;
    int DEFAULT_ACCEPT_THREAD_COUNT = 1;
    int DEFAULT_IO_THREAD_COUNT = Runtime.getRuntime().availableProcessors();

    String NEW_LINE = "\r\n";

    String BANNER_TEXT = NEW_LINE +
            " ____" + NEW_LINE +
            "| __ )  ___   ___  _ __ ___" + NEW_LINE +
            "|  _ \\ / _ \\ / _ \\| '_ ` _ \\" + NEW_LINE +
            "| |_) | (_) | (_) | | | | | |" + NEW_LINE +
            "|____/ \\___/ \\___/|_| |_| |_|";


}