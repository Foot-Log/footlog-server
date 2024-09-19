package footlogger.footlog.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Component
public class TourApi {

    private final ObjectMapper jacksonObjectMapper;
    @Value("${tour_api.service_key}")
    private String serviceKey;

    public TourApi(ObjectMapper jacksonObjectMapper) {
        this.jacksonObjectMapper = jacksonObjectMapper;
    }

    public String getSigunguCourse(int areaCode, int sigunguCode) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://apis.data.go.kr/B551011/GreenTourService1/areaBasedList1"
                        + "?serviceKey=" + serviceKey
                        + "&numOfRows=40"
                        + "&pageNo=1"
                        + "&areaCode=" + areaCode
                        + "&sigunguCode=" + sigunguCode
                        + "&MobileOS=ETC"
                        + "&MobileApp=AppTest"
                        + "&_type=json"))
                .header("accept", "application/json")
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String jsonResponse = response.body();

            return jsonResponse;

        } catch (IOException | InterruptedException e) {
            return e.getMessage();
        }


    }
}
