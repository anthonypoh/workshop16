package sg.edu.nus.workshop16.service;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import java.io.StringReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import sg.edu.nus.workshop16.repo.BoardgamesRepo;

@Service
public class BoardgamesService {

  @Autowired
  @Qualifier("jsonredis")
  private RedisTemplate<String, String> template;

  @Autowired
  private BoardgamesRepo bgRepo;

  public String insert(String jsonRequest) {
    JsonObjectBuilder builder = Json.createObjectBuilder();
    try (
      JsonReader jsonReader = Json.createReader(new StringReader(jsonRequest))
    ) {
      JsonObject jsonObject = jsonReader.readObject();
      String hkey = "boardgames";
      String gid = String.valueOf(jsonObject.getInt("gid"));

      bgRepo.insert(hkey, gid, jsonObject.toString());
      String insertCount = bgRepo.getCount(hkey);
      String id = hkey + " " + gid;

      builder.add("insert_count", insertCount).add("id", id);
    } catch (Exception e) {
      e.printStackTrace();
    }

    JsonObject jsonResponse = builder.build();
    return jsonResponse.toString();
  }

  public String insertAtId(String jsonRequest, String id) {
    JsonObjectBuilder builder = Json.createObjectBuilder();
    try (
      JsonReader jsonReader = Json.createReader(new StringReader(jsonRequest))
    ) {
      JsonObject jsonObject = jsonReader.readObject();
      String hkey = "boardgames";
      String gid = String.valueOf(jsonObject.getInt("gid"));

      bgRepo.insert(hkey, gid, jsonObject.toString());
      String updateCount = bgRepo.getUpdateCount();
      String responseId = hkey + " " + gid;

      builder.add("update_count", updateCount).add("id", responseId);
    } catch (Exception e) {
      e.printStackTrace();
    }

    JsonObject jsonResponse = builder.build();
    return jsonResponse.toString();
  }
}
