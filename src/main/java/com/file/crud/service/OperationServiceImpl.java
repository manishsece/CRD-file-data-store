package com.file.crud.service;

import com.file.crud.Constants;
import com.file.crud.DatabaseOperations;
import com.file.crud.exception.DataException;
import com.file.crud.exception.ServiceException;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.json.simple.JSONObject;

public class OperationServiceImpl implements OperationService {

    private DatabaseOperations databaseOperations;

    public OperationServiceImpl(String jsonFilePath) {
        databaseOperations = new DatabaseOperations(jsonFilePath);
    }

    ExecutorService taskExecuter = Executors.newFixedThreadPool(3);

    @Override
    public void createData(final String key, final String value, final Integer timeToLive)
        throws ServiceException {
        try {

            if (key.length() <= 32 && value.getBytes().length <= 1024 * 16) {
                Runnable task = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put(Constants.VALUE, value);
                            jsonObject.put(Constants.TIME_TO_LIVE, timeToLive);
                            jsonObject.put(Constants.CREATE_TIME, new Date().getTime());
                            databaseOperations.save(key, jsonObject);
                        } catch (DataException de) {
                            de.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                };
                taskExecuter.submit(task);
            } else {
                throw new ServiceException(
                    "invalid key, must have 32 capped character and value size should be less than 16kb   "
                        + key + " : " + value);
            }
        } catch (Exception e) {
            throw new ServiceException("exception occurred while creating data for key: " + key, e);
        }

    }

    @Override
    public void createData(final String key, final String value) throws ServiceException {
        try {
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject jsonValue = new JSONObject();
                        jsonValue.put(Constants.VALUE, value);
                        databaseOperations.save(key, jsonValue);
                    } catch (DataException de) {
                        de.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };

            taskExecuter.submit(task);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage() + "key: " + key, e);
        }
    }

    @Override
    public void readData(final String key) throws ServiceException {
        try {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject jsonObject = databaseOperations.read(key);
                        if (jsonObject != null) {
                            System.out.println("For key :" + key + " Data are :" + jsonObject);
                        } else {
                            throw new ServiceException(
                                "Given key does not exists in database to read");
                        }
                    } catch (DataException de) {
                        de.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
            thread.join();
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());

        }
    }

    @Override
    public void deleteData(final String key) throws ServiceException {
        try {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (databaseOperations.delete(key)) {
                            System.out.println("Successfully data deleted for key: " + key);
                        } else {
                            throw new ServiceException("Sorry! some unexpected exception occurred");
                        }
                    } catch (DataException de) {
                        de.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
            thread.join();
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());

        }
    }
}
