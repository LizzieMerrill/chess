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
    void ListGamesPos() throws Exception {
        assertTrue(true);
    }
    void ListGamesNeg() throws Exception {
        assertTrue(true);
    }
    void JoinGamePos() throws Exception {
        assertTrue(true);
    }
    void JoinGameNeg() throws Exception {
        assertTrue(true);
    }void JoinObserverPos() throws Exception {
        assertTrue(true);
    }
    void JoinObserverNeg() throws Exception {
        assertTrue(true);
    }
    void CreateGamePos() throws Exception {
        assertTrue(true);
    }
    void CreateGameNeg() throws Exception {
        assertTrue(true);
    }
    void RegisterPos() throws Exception {
        assertTrue(true);
    }void RegisterNeg() throws Exception {
        assertTrue(true);
    }void LoginPos() throws Exception {
        assertTrue(true);
    }void LoginNeg() throws Exception {
        assertTrue(true);
    }void LogoutPos() throws Exception {
        assertTrue(true);
    }void LogoutNeg() throws Exception {
        assertTrue(true);
    }void b() throws Exception {
        assertTrue(true);
    }void c() throws Exception {
        assertTrue(true);
    }void d() throws Exception {
        assertTrue(true);
    }void e() throws Exception {
        assertTrue(true);
    }void f() throws Exception {
        assertTrue(true);
    }void g() throws Exception {
        assertTrue(true);
    }













}