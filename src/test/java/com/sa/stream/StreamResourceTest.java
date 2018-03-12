package com.sa.stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class StreamResourceTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Test
    public void streamWithInterceptor() {
        assertResponseEchoed(false, true);
    }

    @Test
    public void noStreamingWithInterceptor() {
        assertResponseEchoed(true, true);
    }

    @Test
    public void streamWithNoInterceptor() {
        assertResponseEchoed(false, false);
    }

    @Test
    public void noStreamingWithNoInterceptor() {
        assertResponseEchoed(true, false);
    }

    private void assertResponseEchoed(boolean buffering, boolean intercept) {
        String myRequest = "MyRequest";
        String url = "http://localhost:" + port + "/stream?buffer=" + buffering + "&intercept=" + intercept;

        String response = this.restTemplate.postForObject(url, myRequest, String.class);
        assertThat(response).isEqualTo(myRequest);
    }

}