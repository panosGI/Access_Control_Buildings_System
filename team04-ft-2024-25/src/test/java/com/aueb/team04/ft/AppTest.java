//package com.aueb.team04.ft;
//
//import io.quarkus.test.junit.QuarkusTest;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import java.io.ByteArrayOutputStream;
//import java.io.PrintStream;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
//@QuarkusTest
//public class AppTest {
//
//    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//    private final PrintStream originalOut = System.out;
//
//    @BeforeEach
//    public void setUp() {
//        System.setOut(new PrintStream(outContent));
//    }
//
//    @Test
//    public void testAppClassExists() {
//        App app = new App();
//        assertNotNull(app, "App class should be instantiated successfully");
//    }
//
//    @Test
//    public void testMain() {
//        App.main(new String[]{});
//        assertEquals("Hello World!\n", outContent.toString(), "Output should match 'Hello World!'");
//    }
//
//    @AfterEach
//    public void tearDown() {
//        System.setOut(originalOut);
//    }
//}