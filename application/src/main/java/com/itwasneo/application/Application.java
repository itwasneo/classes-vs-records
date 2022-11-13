package com.itwasneo.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.activej.bytebuf.ByteBuf;
import io.activej.http.AsyncServlet;
import io.activej.http.HttpMethod;
import io.activej.http.HttpResponse;
import io.activej.http.RoutingServlet;
import io.activej.inject.Injector;
import io.activej.inject.annotation.Provides;
import io.activej.inject.module.ModuleBuilder;
import io.activej.launcher.Launcher;
import io.activej.launchers.http.HttpServerLauncher;

import java.io.IOException;

public class Application extends HttpServerLauncher {

    private static final Injector injector = Injector.of(ModuleBuilder.create()
            // ObjectMapper
            .bind(ObjectMapper.class).to(ObjectMapper::new)
            .build());

    @Provides
    AsyncServlet servlet() {

        ObjectMapper om = injector.getInstance(ObjectMapper.class);

        return RoutingServlet.create()
                .map(HttpMethod.GET, "/", request ->
                        HttpResponse.ok200().withPlainText("Hello world")
                ).map(
                        HttpMethod.POST, "/record", req -> req.loadBody()
                                .map(promise -> {
                                    ByteBuf body = req.getBody();
                                    try {
                                        byte[] bodyBytes = body.getArray();
                                        PersonRecord personRecord = om.readValue(bodyBytes, PersonRecord.class);
                                        return HttpResponse.ok200().withPlainText(String.format("Person with name %s and age %s is created.", personRecord.name(), personRecord.age()));
                                    } catch (IOException e) {
                                        return HttpResponse.ofCode(500).withPlainText("Invalid Person!!!");
                                    }
                                })
                ).map(
                        HttpMethod.POST, "/class", req -> req.loadBody()
                                .map(promise -> {
                                    ByteBuf body = req.getBody();
                                    try {
                                        byte[] bodyBytes = body.getArray();
                                        PersonClass personClass = om.readValue(bodyBytes, PersonClass.class);
                                        return HttpResponse.ok200().withPlainText(String.format("Person with name %s and age %s is created.", personClass.name, personClass.age));
                                    } catch (Exception e) {
                                        return HttpResponse.ofCode(500).withPlainText("Invalid Person");
                                    }
                                }));
    }

    public static void main(String[] args) throws Exception {
        Launcher launcher = new Application();
        launcher.launch(args);
    }
}
