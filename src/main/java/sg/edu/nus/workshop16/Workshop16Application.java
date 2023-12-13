package sg.edu.nus.workshop16;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import java.io.FileReader;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class Workshop16Application implements ApplicationRunner {

  @Autowired
  @Qualifier("jsonredis")
  private RedisTemplate<String, String> template;

  public static void main(String[] args) {
    SpringApplication.run(Workshop16Application.class, args);
  }

  public static void postBoardgameToApi(String jsonRequest) {
    RestTemplate restTemplate = new RestTemplate();

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> request = new HttpEntity<>(jsonRequest, headers);
    ResponseEntity<String> response = restTemplate.postForEntity(
      "http://localhost:8080/api/boardgame",
      request,
      String.class
    );

    System.out.println(
      "Posted Boardgame " + jsonRequest + ". Response: " + response.getBody()
    );
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {
    ValueOperations<String, String> opsValue = template.opsForValue();
    opsValue.set("update_count", "0");

    final String path = args.getOptionValues("path").get(0);

    try (JsonReader jsonReader = Json.createReader(new FileReader(path))) {
      JsonArray jsonArray = jsonReader.readArray();

      for (JsonObject jsonObject : jsonArray.getValuesAs(JsonObject.class)) {
        // Do some validation here
        postBoardgameToApi(jsonObject.toString());
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
