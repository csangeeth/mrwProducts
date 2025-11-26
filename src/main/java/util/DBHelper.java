package util;

import io.github.cozyloon.EzConfig;
import model.MRWProduct;

import java.sql.*;

public class DBHelper {
    private static final String SQL_ERROR = "SQL Error";
    private String dbUrl;
    private String userName;
    private String password;

    public DBHelper(String dbUrl, String dbUserName, String dbPassword) {
        this.dbUrl = dbUrl;
        this.userName = dbUserName;
        this.password = dbPassword;
    }

    private Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(dbUrl, userName, password);
        } catch (Exception e) {
            EzConfig.logERROR("DB Connection error", e);
        }
        return DriverManager.getConnection(dbUrl, userName, password);
    }

    public void executeQuery(String query) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            EzConfig.logERROR(SQL_ERROR, e);
        } finally {
            if (preparedStatement != null)
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    EzConfig.logERROR(SQL_ERROR, e);
                }
            if (connection != null)
                try {
                    connection.close();
                } catch (SQLException e) {
                    EzConfig.logERROR(SQL_ERROR, e);
                }
        }
    }

    public void executeDBPostStepsWithLoggers(ResultSet resultSet, Connection connection, PreparedStatement preparedStatement) {
        if (resultSet != null)
            try {
                resultSet.close();
            } catch (SQLException e) {
                EzConfig.logERROR(SQL_ERROR, e);
            }
        if (preparedStatement != null)
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                EzConfig.logERROR(SQL_ERROR, e);
            }
        if (connection != null)
            try {
                connection.close();
            } catch (SQLException e) {
                EzConfig.logERROR(SQL_ERROR, e);
            }
    }

    public MRWProduct fetchMRWProductDetails(String doi) {

        ResultSet resultSet = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        MRWProduct mrwProductDetails = new MRWProduct();

        String query = "select NAME,DOI,URL from product_api_db_qa.product p where DOI ='" + doi + "'";

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(query);
            // preparedStatement.setString(1, doi);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                mrwProductDetails.setNAME(resultSet.getString("NAME"));
                mrwProductDetails.setDOI(resultSet.getString("DOI"));
                mrwProductDetails.setURL(resultSet.getString("URL"));
            }
        } catch (SQLException e) {
            EzConfig.logERROR(SQL_ERROR, e);
        } finally {
            executeDBPostStepsWithLoggers(resultSet, connection, preparedStatement);
        }
        return mrwProductDetails;
    }

}
