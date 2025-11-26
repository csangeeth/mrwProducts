package common;

import io.github.cozyloon.EzConfig;

public class Constants {
    public static final String DB_URL = EzConfig.getProperty("db.url");
    public static final String DB_USERNAME = EzConfig.getProperty("db.username");
    public static final String DB_PASSWORD = EzConfig.getProperty("db.password");
    public static final String CSV_FILE_PATH = EzConfig.getProperty("user.dir")+"\\src\\main\\resources\\data\\";
}