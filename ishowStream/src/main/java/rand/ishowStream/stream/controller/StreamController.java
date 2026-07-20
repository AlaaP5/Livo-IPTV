package rand.ishowStream.stream.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rand.ishowStream.stream.service.StreamService;

@RestController
@RequiredArgsConstructor
@RequestMapping("stream/v1")
public class StreamController {

    private final StreamService service;

    @GetMapping("/poster")
    public ResponseEntity<Resource> streamPoster(@RequestParam String dbPath) {
        HttpHeaders headers = new HttpHeaders();
        Resource resource = service.servePoster(dbPath, headers);

        if (resource == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().headers(headers).body(resource);
    }


    @GetMapping("/test")
    public ResponseEntity<String> streamPoster() {


        return ResponseEntity.ok().body("OK");
    }

}
