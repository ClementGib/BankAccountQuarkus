package com.cdx.bas.application.config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.ws.rs.Produces;

import java.time.Clock;

@ApplicationScoped
public class Producers {

    @Produces
    @Default
    public Clock produceClock(){
        return Clock.systemDefaultZone();
    }

}
