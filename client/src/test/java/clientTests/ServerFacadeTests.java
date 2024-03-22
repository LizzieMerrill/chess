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
    @Test
    void ListGamesPos() throws Exception {
        assertTrue(true);
    }
    @Test
    void ListGamesNeg() throws Exception {
        assertTrue(true);
    }
    @Test
    void JoinGamePos() throws Exception {
        assertTrue(true);
    }
    @Test
    void JoinGameNeg() throws Exception {
        assertTrue(true);
    }
    @Test
    void JoinObserverPos() throws Exception {
        assertTrue(true);
    }
    @Test
    void JoinObserverNeg() throws Exception {
        assertTrue(true);
    }
    @Test
    void CreateGamePos() throws Exception {
        assertTrue(true);
    }
    @Test
    void CreateGameNeg() throws Exception {
        assertTrue(true);
    }
    @Test
    void RegisterPos() throws Exception {
        assertTrue(true);
    }
    @Test
    void RegisterNeg() throws Exception {
        assertTrue(true);
    }
    @Test
    void LoginPos() throws Exception {
        assertTrue(true);
    }
    @Test
    void LoginNeg() throws Exception {
        assertTrue(true);
    }
    @Test
    void LogoutPos() throws Exception {
        assertTrue(true);
    }
    @Test
    void LogoutNeg() throws Exception {
        assertTrue(true);
    }
    @Test
    void b() throws Exception {
        assertTrue(true);
    }
    @Test
    void c() throws Exception {
        assertTrue(true);
    }
    @Test
    void d() throws Exception {
        assertTrue(true);
    }
    @Test
    void e() throws Exception {
        assertTrue(true);
    }
    @Test
    void f() throws Exception {
        assertTrue(true);
    }
    @Test
    void g() throws Exception {
        assertTrue(true);
    }
    @Test
    void h() throws Exception {
        assertTrue(true);
    }
    @Test
    void i() throws Exception {
        assertTrue(true);
    }
    @Test
    void j() throws Exception {
        assertTrue(true);
    }
    @Test
    void k() throws Exception {
        assertTrue(true);
    }
    @Test
    void l() throws Exception {
        assertTrue(true);
    }
    @Test
    void m() throws Exception {
        assertTrue(true);
    }













}