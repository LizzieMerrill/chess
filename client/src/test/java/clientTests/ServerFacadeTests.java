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
    void a() throws Exception {
        assertTrue(true);
    }
    void d() throws Exception {
        assertTrue(true);
    }
    void b() throws Exception {
        assertTrue(true);
    }
    void c() throws Exception {
        assertTrue(true);
    }
    void e() throws Exception {
        assertTrue(true);
    }void f() throws Exception {
        assertTrue(true);
    }


}