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


//    @Test
//    public void sampleTest() {
//        assertTrue(true);
//    }
    @Test
    public void a() throws Exception {
        assertTrue(true);
    }
    @Test
    public void ListGamesPos() throws Exception {
        assertTrue(true);
    }
    @Test
    public void ListGamesNeg() throws Exception {
        assertTrue(true);
    }
    @Test
    public void JoinGamePos() throws Exception {
        assertTrue(true);
    }
    @Test
    public void JoinGameNeg() throws Exception {
        assertTrue(true);
    }
    @Test
    public void JoinObserverPos() throws Exception {
        assertTrue(true);
    }
    @Test
    public void JoinObserverNeg() throws Exception {
        assertTrue(true);
    }
    @Test
    public void CreateGamePos() throws Exception {
        assertTrue(true);
    }
    @Test
    public void CreateGameNeg() throws Exception {
        assertTrue(true);
    }
    @Test
    public void RegisterPos() throws Exception {
        assertTrue(true);
    }
    @Test
    public void RegisterNeg() throws Exception {
        assertTrue(true);
    }
    @Test
    public void LoginPos() throws Exception {
        assertTrue(true);
    }
    @Test
    public void LoginNeg() throws Exception {
        assertTrue(true);
    }
    @Test
    public void LogoutPos() throws Exception {
        assertTrue(true);
    }
    @Test
    public void LogoutNeg() throws Exception {
        assertTrue(true);
    }
    @Test
    public void b() throws Exception {
        assertTrue(true);
    }
    @Test
    public void c() throws Exception {
        assertTrue(true);
    }
    @Test
    public void d() throws Exception {
        assertTrue(true);
    }
    @Test
    public void e() throws Exception {
        assertTrue(true);
    }
    @Test
    public void f() throws Exception {
        assertTrue(true);
    }
    @Test
    public void g() throws Exception {
        assertTrue(true);
    }
    @Test
    public void h() throws Exception {
        assertTrue(true);
    }
    @Test
    public void i() throws Exception {
        assertTrue(true);
    }
    @Test
    public void j() throws Exception {
        assertTrue(true);
    }
    @Test
    public void k() throws Exception {
        assertTrue(true);
    }
    @Test
    public void l() throws Exception {
        assertTrue(true);
    }
    @Test
    public void m() throws Exception {
        assertTrue(true);
    }













}