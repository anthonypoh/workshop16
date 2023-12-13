package sg.edu.nus.workshop16.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@Repository
public class BoardgamesRepo {

  @Autowired
  @Qualifier("jsonredis")
  private RedisTemplate<String, String> template;

  public String getCount(String hkey) {
    HashOperations<String, String, String> hashValue = template.opsForHash();
    return Long.toString(hashValue.size(hkey));
  }

  public String getUpdateCount() {
    ValueOperations<String, String> opsValue = template.opsForValue();
    opsValue.increment("update_count");
    String update_count = opsValue.get("update_count");
    return update_count;
  }

  public void insert(String hkey, String gid, String jsonString) {
    HashOperations<String, String, String> hashValue = template.opsForHash();
    hashValue.put(hkey, gid, jsonString);
  }

  public String getById(String hkey, int id) {
    HashOperations<String, String, String> hashValue = template.opsForHash();
    return hashValue.get(hkey, String.valueOf(id));
  }
}
