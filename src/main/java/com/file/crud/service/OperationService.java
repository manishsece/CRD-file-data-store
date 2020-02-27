package com.file.crud.service;

import com.file.crud.exception.ServiceException;

public interface OperationService {
 void createData(String key,String value,Integer timeToLeave) throws ServiceException;
 void createData(String key, String value) throws ServiceException;;
 void readData(String key) throws ServiceException;
 void deleteData(String key) throws ServiceException;
}
