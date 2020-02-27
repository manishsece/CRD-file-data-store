package com.file.crud;

import com.file.crud.exception.ServiceException;
import com.file.crud.service.OperationService;
import com.file.crud.service.OperationServiceImpl;
import java.io.File;
import org.junit.Test;

public class JUnitTesting {

    String databaseFile = "database.json";
    String currentPath = System.getProperty("user.dir");
    String absolutePath = currentPath + File.separator + databaseFile;

    OperationService operationService = new OperationServiceImpl(absolutePath);

    @Test
    public void dataShouldBeCreatedWithValidKeyAndValueWithoutTimeToLeave() throws ServiceException {
        operationService.createData("11","hi welcome");
    }
    @Test
    public void dataShouldBeCreatedWithValidKeyAndValueAndTimeToLeave() throws ServiceException{
        operationService.createData("12","manish",1);
    }
    @Test
    public void shouldThrowExceptionForExistingInvalidKeyAndWithoutTimeToLiveDuringDataCreation()
        throws ServiceException, InterruptedException {
        Thread.sleep(1000);
        operationService.createData("11","faizabad");
    }
    @Test
    public void shouldThrowExceptionForInvalidKeyDuringDataCreation() throws ServiceException{
        operationService.createData("12","namskaar",10);
    }
    @Test
    public void shouldReadDataForValidKey() throws ServiceException {
        operationService.readData("11");
    }
    @Test
    public void shouldThrowExceptionForInvalidKeyToReadData() throws ServiceException{
        operationService.readData("13");
    }
    @Test
    public void deleteDataForValidKey() throws ServiceException{
        operationService.deleteData("11");
    }
    @Test
    public void shouldThrowExceptionForInvalidKeyToDeleteData() throws ServiceException{
        operationService.deleteData("24");
    }
    @Test
    public void shouldThrowExceptionForExpiredKeyToDeleteData() throws ServiceException{
        operationService.deleteData("12");
    }
}