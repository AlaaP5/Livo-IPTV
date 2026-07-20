package com.rand.ishowApi.test;


import com.rand.ishowApi.home.mobile.api.response.HomeMobileResponse;
import com.rand.ishowApi.home.mobile.service.HomeMobileService;
import com.rand.ishowApi.metadata.video.OriginalUploadMetadata;
import com.rand.ishowApi.movie.admin.api.request.MovieAdminRequest;
import com.rand.ishowApi.movie.admin.application.service.MovieAdminService;
import com.rand.ishowApi.movie.admin.domain.entity.Movies;
import com.rand.ishowApi.movie.mobile.api.response.MovieSectionResponse;
import com.rand.ishowApi.movie.mobile.service.MovieMobileService;
import com.rand.ishowApi.openSearch.response.SectionBannerResponse;
import com.rand.ishowApi.storage.model.request.UploadRequest;
import com.rand.ishowApi.storage.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch.core.InfoResponse;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/v1/public/")
@RequiredArgsConstructor
public class testController {
    private final OpenSearchClient openSearchClient;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final StorageService storageService;
    private final MovieAdminService movieAdminService;
    private final MovieMobileService movieMobileService;
    private final HomeMobileService homeMobileService;

    @GetMapping("test/openSearch")
    public String testOpenSearch() {
        try {
            // Get OpenSearch cluster info
            InfoResponse response = openSearchClient.info();
            return "OpenSearch cluster: " + response.clusterName() +
                    ", version: " + response.version().number();
        } catch (Exception e) {
            return "Failed to connect to OpenSearch: " + e.getMessage();
        }
    }

    @PostMapping("message/publish")
    public String sendMessage(@RequestBody UserMessage message) {
        // You can send the Object directly; Kafka handles the JSON conversion
        try {
          kafkaTemplate.send("test-topic", message);
            return "Message sent to Kafka successfully!";
        }
        catch(Exception ex){
            return ex.getMessage();
        }
    }
    @PostMapping("upload")
    public OriginalUploadMetadata sendMessage(@ModelAttribute UploadRequest request) throws IOException {
        // You can send the Object directly; Kafka handles the JSON conversion

         return storageService.uploadFile(request);

    }

    @PostMapping("test/upload-zip")
    public Object uploadZip(MultipartFile zip) {
        List<MultipartFile> imagesFromZip=  storageService.extractImagesFromZip(zip);
        return imagesFromZip.stream().count();
    }

    @PostMapping("movie/create")
    public Movies sendMessage(@ModelAttribute MovieAdminRequest request) throws IOException {
        // You can send the Object directly; Kafka handles the JSON conversion

        return movieAdminService.addMovie(request);
    }
    @GetMapping("home")
    public HomeMobileResponse sendMessage() throws IOException {
        // You can send the Object directly; Kafka handles the JSON conversion

        return homeMobileService.getHomeMobile();
    }

    @PostMapping("movie/search")
    public SectionBannerResponse<MovieSectionResponse> movieSection() throws IOException {
        // You can send the Object directly; Kafka handles the JSON conversion

        return  movieMobileService.getMoviesSections();

    }

}
