package com.penunes.swapi;

import com.penunes.swapi.api.ApiTests;
import com.penunes.swapi.client.SwapiClientTests;
import com.penunes.swapi.service.ServiceTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        SwapiClientTests.class,
        ServiceTests.class,
        ApiTests.class
})
public class SwapiApplicationTests {
}