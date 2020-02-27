package com.file.crud;

import com.file.crud.exception.DataException;
import java.io.File;
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.Test;

public class DatabaseOperationsTest {


    String databaseFile = "database.json";
    String currentPath = System.getProperty("user.dir");
    String absolutePath = currentPath + File.separator + databaseFile;

    IDatabaseOperations iDatabaseOperations = new DatabaseOperations(absolutePath);

    @Test
    public void dataShouldBeCreatedWithValidKeyAndValueWithoutTimeToLeave() throws DataException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constants.VALUE, "hello");
        Assert.assertEquals(true, iDatabaseOperations.save("22", jsonObject));
    }

    @Test
    public void dataShouldBeCreatedWithValidKeyAndValueAndTimeToLeave() throws DataException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constants.VALUE, "hello");
        jsonObject.put(Constants.TIME_TO_LIVE, 5);
        Assert.assertEquals(true, iDatabaseOperations.save("23", jsonObject));
    }

    @Test
    public void shouldThrowExceptionForExistingInvalidKeyAndWithoutTimeToLiveDuringDataCreation()
        throws DataException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constants.VALUE, "hello");
        Assert.assertEquals(true, iDatabaseOperations.save("21", jsonObject));
    }

    @Test
    public void shouldThrowExceptionForInvalidKeyDuringDataCreation() throws DataException {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put(Constants.VALUE, "manish");
        jsonObject.put(Constants.TIME_TO_LIVE, 5);
        Assert.assertEquals(true, iDatabaseOperations.save("21", jsonObject));
    }

    @Test
    public void shouldReadDataForValidKey() throws DataException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constants.VALUE, "hello");
        Assert.assertEquals(jsonObject, iDatabaseOperations.read("22"));
    }

    @Test
    public void shouldThrowExceptionForInvalidKeyToReadData() throws DataException {
        Assert.assertEquals(null, iDatabaseOperations.read("22"));
    }

    @Test
    public void deleteDataForValidKey() throws DataException, Exception {
        Assert.assertEquals(true, iDatabaseOperations.delete("21"));
    }

    @Test
    public void shouldThrowExceptionForInvalidKeyToDeleteData() throws DataException, Exception {
        Assert.assertEquals(false, iDatabaseOperations.delete("33"));
    }

    @Test
    public void shouldThrowExceptionForExpiredKeyToDeleteData() throws DataException, Exception {
        Assert.assertEquals(false, iDatabaseOperations.delete("22"));
    }
}