package com.sa.stream;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;

@RestController
public class StreamResource {


    @PostMapping(path = "/stream", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> postOne(
            HttpServletRequest request,
            @RequestParam(value = "intercept") boolean intercept,
            @RequestParam(value = "buffer") boolean buffered
    ) throws IOException {


        // setup our request factory buffered or non-buffered (Streaming)
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setBufferRequestBody(buffered);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(requestFactory);
        if (intercept) {
            restTemplate.setInterceptors(Collections.singletonList(new ClientRequestInterceptor()));
        }

        InputStreamResource resource = new InputStreamResource(request.getInputStream());
        HttpEntity<Resource> requestEntity = new HttpEntity<>(resource);

        int port = request.getLocalPort();
        String url = "http://localhost:" + port + "/stream2";
        return restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
    }

    @PostMapping(path = "/stream2", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> postTwo(HttpServletRequest request) throws IOException {
        String string = StreamUtils.copyToString(request.getInputStream(), Charset.defaultCharset());
        return ResponseEntity.ok().body(string);
    }


}
