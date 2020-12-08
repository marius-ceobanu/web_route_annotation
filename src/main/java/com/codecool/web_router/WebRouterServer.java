package com.codecool.web_router;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;

public class WebRouterServer {
    public static void main(String[] args) throws Exception {
        Routes routes = new Routes();
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/", new MyHandler(routes));
        server.setExecutor(null); // creates a default executor
        server.start();
    }

    static class MyHandler implements HttpHandler {
        private final Routes routes;

        public MyHandler(Routes routes) {
            this.routes = routes;
        }

        @Override
        public void handle(HttpExchange t) throws IOException {
            String path = t.getRequestURI().getPath();
            String[] segments = path.split("/");
            String parentRoot = "/";
            if(segments.length==2) {
                parentRoot += segments[1];
            } else if(segments.length>2) {
                parentRoot += segments[1]+"/";
            }
            Class<Routes> theClass = Routes.class;

            for(Method m: theClass.getDeclaredMethods()) {
                if(m.isAnnotationPresent(WebRoute.class)) {
                    Annotation annotation = m.getAnnotation(WebRoute.class);
                    WebRoute webRoute = (WebRoute) annotation;

                    if(webRoute.path().equals(parentRoot) && segments.length==2) {
                        try {
                            m.invoke(routes, t);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    } else if(webRoute.path().equals(parentRoot) && segments.length>2) {
                        try {
                            m.invoke(routes, t, segments[2]);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
