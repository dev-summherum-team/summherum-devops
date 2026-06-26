@RestController
@RequestMapping("/health")
public class HealthController {

    @GetMapping("/db")
    public ResponseEntity<String> checkDb() {
        try (MongoClient mongoClient = MongoClients.create(System.getenv("MONGO_URI"))) {
            mongoClient.getDatabase("admin")
                       .runCommand(new Document("ping", 1));

            return ResponseEntity.ok("ok");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("db down");
        }
    }
}