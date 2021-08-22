package org.example;

import org.example.utils.Log;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;

public class TestsSetups {

    @BeforeEach
    public void setUp(TestInfo testInfo) {
        Log.info("------- Started test: " + testInfo.getDisplayName() + " -------");
        Assertions.assertNotNull(DaoJDBC.connectToDB());
    }

    @AfterEach
    public void tearDown(TestInfo testInfo) {
        DaoJDBC.closeConnection();
        Log.info("------- Finished test: " + testInfo.getDisplayName() + " -------");
    }
}
