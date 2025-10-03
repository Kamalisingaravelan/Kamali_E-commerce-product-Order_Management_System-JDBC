package org.anudip.ecommerce.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import java.util.logging.Logger;
import java.io.InputStream;

public class DBUtil {
	 private static final Logger LOGGER = Logger.getLogger(DBUtil.class.getName());
	    private static String url;
	    private static String user;
	    private static String password;

	    static {
	        try (InputStream in = DBUtil.class.getClassLoader().getResourceAsStream("db.properties")) {
	            Properties props = new Properties();
	            props.load(in);
	            url = props.getProperty("db.url");
	            user = props.getProperty("db.user");
	            password = props.getProperty("db.password");

	            // load driver optional for modern drivers, but safe:
	            Class.forName("com.mysql.cj.jdbc.Driver");
	        } catch (Exception e) {
	            LOGGER.severe("Failed to load DB properties: " + e.getMessage());
	            throw new RuntimeException(e);
	        }
	    }

	    public static Connection getConnection() throws Exception {
	        return DriverManager.getConnection(url, user, password);
	    }
}
