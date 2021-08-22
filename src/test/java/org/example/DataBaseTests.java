package org.example;
import org.example.utils.ConfProperties;
import org.junit.jupiter.api.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DataBaseTests extends TestsSetups {

    @Order(1)
    @Test
    @DisplayName("Проверка создания таблицы в БД")
    public void testCreateTable() {
        String query = ConfProperties.getProperty("create_table_request");
        DaoJDBC.createTable(query);

        Assertions.assertTrue(DaoJDBC.isTableNameExist(ConfProperties.getProperty("custom_table_name")));
    }

    @Order(2)
    @Test
    @DisplayName("Отправка INSERT запроса")
    public void testInsertRequest() {
        String query = ConfProperties.getProperty("insert_table_request");
        DaoJDBC.insertIntoTable(query);

        String selectQuery = "SELECT * FROM " + ConfProperties.getProperty("custom_table_name");
        ResultSet rs = DaoJDBC.selectFromTable(selectQuery);
        assertAll("Should return inserted data",
                () -> assertEquals("490", rs.getString("ID")),
                () -> assertEquals("Renault Logan", rs.getString("MODEL_NAME")),
                () -> assertEquals("Gray", rs.getString("COLOR")));
    }

    @Order(3)
    @Test
    public void testUpdateRequest() throws SQLException {
        String query = ConfProperties.getProperty("update_table_request");
        DaoJDBC.updateInTable(query);
        String selectQuery = "SELECT COLOR FROM " + ConfProperties.getProperty("custom_table_name") + " WHERE ID = ? ";
        String param = "490";
        ResultSet rs = DaoJDBC.selectFromTable(selectQuery, param);
        String expectedColor = "RED";
        String actualColor = rs.getString("COLOR");
        assertEquals(expectedColor, actualColor, "Actual town is '" + actualColor + "'. Expected - '" + expectedColor + "'.");
    }

    @Order(4)
    @Test
    @DisplayName("Отправка DELETE запроса")
    public void testDeleteRequest() {
        String query = "DELETE FROM " + ConfProperties.getProperty("custom_table_name") + " WHERE ID=490";
        DaoJDBC.deleteFromTable(query);
        Assertions.assertTrue(DaoJDBC.isEmptyTable(ConfProperties.getProperty("custom_table_name")));
    }

    @Order(5)
    @Test
    @DisplayName("Проверка удаления таблицы из БД")
    public void test_dropTable() {
        DaoJDBC.dropTable(ConfProperties.getProperty("custom_table_name"));

        Assertions.assertFalse(DaoJDBC.isTableNameExist(ConfProperties.getProperty("custom_table_name")));
    }

    @Order(6)
    @Test
    @DisplayName("Отправка простого SELECT запроса. Проверка имени актера")
    public void testSelectRequest_checkName() throws SQLException {
        String query = "SELECT * FROM actor WHERE actor_id = 10";
        ResultSet rs = DaoJDBC.selectFromTable(query);
        String expectedName = "CHRISTIAN";
        String actualName = rs.getString("first_name");
        assertEquals(expectedName, actualName, "Actual name is '" + actualName + "'. Expected - '" + expectedName + "'.");
    }

    @Order(7)
    @Test
    @DisplayName("Отправка SELECT JOIN запроса. Проверка принадлежности адреса стране")
    public void testSelectWithJoinRequest_CheckAddressAndCountry() throws SQLException {
        String query = ConfProperties.getProperty("join_table_request");
        ResultSet rs = DaoJDBC.selectFromTable(query);
        String expectedAddress = "535 Ahmadnagar Manor";
        String actualAddress = rs.getString("address");
        assertEquals(expectedAddress, actualAddress, "Actual address is '" + actualAddress + "'. Expected - '" + expectedAddress + "'.");
    }
}
