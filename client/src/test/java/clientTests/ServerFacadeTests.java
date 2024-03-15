package clientTests;

import org.junit.jupiter.api.*;
import server.Server;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void sampleTest() {
        assertTrue(true);
    }
    @Test
    void register() throws Exception {
        var authData = facade.register("player1", "password", "p1@email.com");
        assertTrue(authData.getAuthToken().length() > 10);
    }

    static private PetServer petServer;
    static PetClient client;

    @BeforeAll
    static void startServer() throws Exception {
        var service = new PetService(new MemoryDataAccess());
        petServer = new PetServer(service);
        petServer.run(0);
        var url = "http://localhost:" + petServer.port();
        client = new PetClient(url, null);
        client.signIn("tester");
    }

    @Test
    void rescuePet() {
        var result = assertDoesNotThrow(() -> client.rescuePet("joe", "fish"));
        assertMatches("You rescued joe. Assigned ID: \\d+", result);

        result = assertDoesNotThrow(() -> client.rescuePet("sally", "cat"));
        assertMatches("You rescued sally. Assigned ID: \\d+", result);
    }

    @Test
    void adoptPet() throws Exception {
        var id = getId(client.rescuePet("joe", "frog"));
        client.rescuePet("sally", "cat");

        var result = assertDoesNotThrow(() -> client.adoptPet(id));
        assertEquals("joe says ribbit", result);
    }


    @Test
    void adoptAllPets() throws Exception {
        client.rescuePet("joe", "rock");
        client.rescuePet("pat", "cat");
        var result = assertDoesNotThrow(() -> client.adoptAllPets());
        assertEquals("joe says roll\npat says meow\n", result);
        assertEquals("", client.listPets());
    }

    @Test
    void deleteNonexistentPet() {
        assertThrows(ResponseException.class, () -> client.adoptPet("933432"));
    }


    @Test
    void listPet() {
        assertDoesNotThrow(() -> client.rescuePet("joe", "fish"));
        assertDoesNotThrow(() -> client.rescuePet("sally", "fish"));

        var result = assertDoesNotThrow(() -> client.listPets());
        assertMatches("""
                \\{'id':\\d+,'name':'joe','type':'FISH'}
                \\{'id':\\d+,'name':'sally','type':'FISH'}
                """, result);
    }

    private void assertMatches(String expected, String actual) {
        actual = actual.replace('"', '\'');

        assertTrue(actual.matches(expected), actual + "\n" + expected);
    }

    private static String getId(String text) {
        Pattern p = Pattern.compile("ID: (\\d+)");
        Matcher m = p.matcher(text);
        if (m.find()) {
            return m.group(1);
        }
        fail("ID not found");
        return "";
    }

}