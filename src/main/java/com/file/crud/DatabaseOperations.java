package com.file.crud;

import com.file.crud.exception.DataException;
import java.io.*;
import java.util.Date;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class DatabaseOperations implements IDatabaseOperations {

    private static final Logger logger = Logger.getLogger("DatabaseOperations");
    private String database;

    public DatabaseOperations(String databaseFile) {
        this.database = databaseFile;
    }

    public boolean save(String key, JSONObject data) throws DataException {
        JSONObject jsonObject = new JSONObject();
        try {
            if (getUsedFileDatabaseSize(data.toString()) <= FileUtils.ONE_GB) {
                if (new File(database).length() == 0) {
                    writeData(jsonObject);
                }
                jsonObject = getParsedData();
                if (isValidKey(key)) {
                    throw new DataException(
                        "Key already exists in database please add different key: "
                            + key);
                }
                jsonObject.put(key, data);
                writeData(jsonObject);
                System.out.println("data created in file database system for key:" + key);
            } else {
                throw new DataException("File database size is exceeding more than 1 GB");
            }
        } catch (Exception e) {
            logger.info("error occurred while adding key-value: " + e.getMessage());
            throw new DataException(e.getMessage());

        }

        return true;
    }

    @Override
    public JSONObject read(String key) throws DataException {
        try {
            if (isValidKey(key)) {
                JSONObject database = getParsedData();
                JSONObject value = (JSONObject) database.get(key);
                if (value.containsKey(Constants.VALUE)) {
                    JSONObject result = new JSONObject();
                    result.put(key, value.get(Constants.VALUE));
                    return result;
                } else {
                    throw new DataException("Exception in getting VALUE for: " + key);
                }
            } else {
                throw new Exception("Invalid key Exception: " + key);
            }
        } catch (Exception e) {
            logger.info("Read_ " + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean delete(String key) throws DataException {
        try {
            if (isValidKey(key)) {
                JSONObject databaseObj = getParsedData();
                if (databaseObj.remove(key) != null) {
                    writeData(databaseObj);
                    return true;
                } else {
                    throw new Exception("Exception in deleting: " + key);
                }
            } else {
                throw new DataException("Invalid key Exception: " + key);
            }
        } catch (Exception e) {
            logger.info("Delete_ " + e.getMessage());
        }
        return false;
    }

    private boolean isValidKey(String key) {
        try {
            JSONObject databaseObj = getParsedData();
            if (databaseObj.containsKey(key)) {
                JSONObject value = (JSONObject) databaseObj.get(key);
                if (value.containsKey(Constants.VALUE)) {
                    if (value.get(Constants.CREATE_TIME) == null) {
                        return true;
                    }
                    long time =
                        (Long) new Date().getTime() - (Long) value.get(Constants.CREATE_TIME);
                    Long timeToLive = (Long) value.get(Constants.TIME_TO_LIVE);
                    if (time <= timeToLive) {
                        return true;
                    } else {
                        databaseObj.remove(key);
                        writeData(databaseObj);
                    }
                }
            }
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        return false;
    }

    private JSONObject getParsedData() throws IOException, ParseException {
        if (new File(database).exists()) {
            return (JSONObject) new JSONParser().parse(new FileReader(database));
        }
        return null;
    }

    private void writeData(JSONObject obj) {
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(database);
            fileWriter.write(obj.toString());
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e) {
            logger.info("error while writing data in file database" + e.getMessage());
        }
    }

    private double getUsedFileDatabaseSize(String value) {
        String fileSizeReadable = FileUtils.byteCountToDisplaySize(new File(database).length());
        long sizeInBytes = Long.parseLong(fileSizeReadable.split(" ")[0]) + value.getBytes().length;
        return sizeInBytes;
    }


}
