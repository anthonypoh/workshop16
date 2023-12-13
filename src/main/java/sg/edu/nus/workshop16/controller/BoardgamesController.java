package sg.edu.nus.workshop16.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sg.edu.nus.workshop16.repo.BoardgamesRepo;
import sg.edu.nus.workshop16.service.BoardgamesService;

@RestController
@RequestMapping(path = "/api")
public class BoardgamesController {

  @Autowired
  private BoardgamesService bgService;

  @Autowired
  private BoardgamesRepo bgRepo;

  @PostMapping(
    path = "/boardgame",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<String> postBoardgame(@RequestBody String jsonRequest) {
    String responseBody = bgService.insert(jsonRequest);
    return new ResponseEntity<>(responseBody, HttpStatus.OK);
  }

  @GetMapping(
    path = "/boardgame/{id}",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<String> getMethodName(@PathVariable int id) {
    String bg = bgRepo.getById("boardgames", id);
    if (bg != null) {
      return ResponseEntity.ok(bg);
    } else {
      return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body("Board game not found");
    }
  }

  @PutMapping(
    path = "boardgame/{id}",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<Object> updateBoardGame(
    @PathVariable int id,
    @RequestParam(name = "upsert", defaultValue = "false") boolean upsert,
    @RequestBody String jsonRequest
  ) {
    try {
      String bg = bgRepo.getById("boardgames", id);

      if (bg == null && !upsert) {
        return ResponseEntity
          .status(HttpStatus.BAD_REQUEST)
          .body("Board game not found");
      }

      String responseBody = bgService.insertAtId(
        jsonRequest,
        String.valueOf(id)
      );

      return ResponseEntity.ok(responseBody);
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("Error updating board game");
    }
  }
}
