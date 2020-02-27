package com.file.crud;
import com.file.crud.exception.DataException;
import org.json.simple.JSONObject;

public interface IDatabaseOperations {

 boolean save(String key,JSONObject data) throws DataException;
 JSONObject read(String key) throws DataException;
 boolean delete(String key) throws Exception,DataException;


}
