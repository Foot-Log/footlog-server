package footlogger.footlog.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class TourApiTest {
    @Value("${Tour_ServiceKey}")
    private String serviceKey;
//
//    public String getTour() {
//        WebClient webClient = WebClient.builder()
//                .baseUrl("http://apis.data.go.kr/B551011/GreenTourService1")
//                .build();
//
//        String uri = "http://apis.data.go.kr/B551011/GreenTourService1/areaCode1?serviceKey=KlK5VUYsX1g2EcfND7iItreFmqKC67Ypt9EW9yrpkaBSFxw3pykQ0NlaXxkqSicY1PXaguxjuHiKEme9MIi0fA%3D%3D&numOfRows=17&pageNo=1&MobileOS=ETC&MobileApp=AppTest&_type=json";
//
//        String responseBody = webClient.get()
//                .uri(uri)
//                .header("accept", "application/json")
//                .retrieve()
//                .bodyToMono(String.class)
//                .block();
//
//        return responseBody;
//    }

    public String getTour() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://apis.data.go.kr/B551011/GreenTourService1/areaBasedList1"
                        + "?serviceKey=" + serviceKey
                        + "&numOfRows=1"
                        + "&pageNo=110"
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

    public String getSiCode(int areaCode) {
//        int[] areaCodes = {
//                0, 1, 31, 2, 32, 3, 8, 34, 33,
//                6, 7, 36, 35, 4, 5, 38, 37, 39
//        };
        int[] areaCodes = {
                1
        };

        HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://apis.data.go.kr/B551011/GreenTourService1/areaCode1"
                    + "?serviceKey=" + serviceKey
                    + "&numOfRows=100"
                    + "&pageNo=1"
                    + "&areaCode=" + areaCode
                    + "&MobileOS=ETC"
                    + "&MobileApp=AppTest"
                    + "&_type=json"))
                .header("accept", "application/json")
                .build();

            try {
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                String jsonResponse = response.body();

                System.out.println(jsonResponse);
                return jsonResponse;

            } catch (IOException | InterruptedException e) {
                return e.getMessage();
            }
    }

    public String getOneTour() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://apis.data.go.kr/B551011/GreenTourService1/areaBasedList1"
                        + "?serviceKey=" + serviceKey
                        + "&numOfRows=10"
                        + "&pageNo=1"
                        + "&areaCode=7"
                        + "$sigunguCode=5"
                        + "&MobileOS=ETC"
                        + "&MobileApp=AppTest"
                        + "&_type=json"))
                .header("accept", "application/json")
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String jsonResponse = response.body();

            System.out.println(jsonResponse);

            return jsonResponse;

        } catch (IOException | InterruptedException e) {
            return e.getMessage();
        }

    }
}
