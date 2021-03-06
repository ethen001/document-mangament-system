package org.guccigang.mini_google_docs.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.guccigang.mini_google_docs.model.DbUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsersDAO {

    public static ObservableList<UserObject> getAllUsers(String currentUserName)throws SQLException{
        ObservableList<UserObject> users;
        String selectStatementUsers = "select * from users where userName <> ? order by userName";
        //Execute select statement
        ResultSet resultSetUsers = DbUtil.processQuery(selectStatementUsers,statement -> {
                statement.setString(1,currentUserName);
        });
        users = getAllUsersList(resultSetUsers);
        return users;
    }
    private static ObservableList<UserObject> getAllUsersList(ResultSet resultSetUsers){
        ObservableList<UserObject> users = FXCollections.observableArrayList();
        String selectStatemnetInterests = "select * from interests where userName = ?";
        try{
            while(resultSetUsers.next()){
                ArrayList<String> interests = new ArrayList<String>();
                String userName = resultSetUsers.getString("userName");
                String password = resultSetUsers.getString("password");
                String firstName = resultSetUsers.getString("firstName");
                String lastName = resultSetUsers.getString("lastName");
                int membershipLevel = resultSetUsers.getInt("membershipLevel");
                ResultSet interestsResultSet = DbUtil.processQuery(selectStatemnetInterests,statement -> {
                        statement.setString(1,userName);
                });
                UserObject user = new UserObject(userName,password,firstName,lastName,membershipLevel);
                while (interestsResultSet.next()){
                    user.addInterest(interestsResultSet.getString("interest"));
                }
                users.add(user);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return users;
    }
    public static ObservableList<UserObject> getSearchedResult(String userInput, String currentUserName){
        ObservableList<UserObject> users;
        String modifiedUserInput = "%"+userInput+"%";
        String selectStatmentUserInput = "select * from users natural join interests where (userName like ? OR interest like ?) AND userName <> ?";
        ResultSet searchedResultSet = DbUtil.processQuery(selectStatmentUserInput, statement -> {
                statement.setString(1,modifiedUserInput);
                statement.setString(2,modifiedUserInput);
                statement.setString(3, currentUserName);
        });
        users = getAllSearchedUsersList(searchedResultSet);
        return users;
    }
    private static ObservableList<UserObject> getAllSearchedUsersList(ResultSet searchedResultSet){
        ObservableList<UserObject> users = FXCollections.observableArrayList();
        String selectStatemnetInterests = "select * from interests where userName = ?";
        int i = 0;
        try{
            while(searchedResultSet.next()){
                ArrayList<String> interests = new ArrayList<String>();
                String userName = searchedResultSet.getString("userName");
                String password = searchedResultSet.getString("password");
                String firstName = searchedResultSet.getString("firstName");
                String lastName = searchedResultSet.getString("lastName");
                int membershipLevel = searchedResultSet.getInt("membershipLevel");
                ResultSet interestsResultSet = DbUtil.processQuery(selectStatemnetInterests, statement -> {
                        statement.setString(1,userName);
                });
                UserObject user = new UserObject(userName,password,firstName,lastName,membershipLevel);

                while (interestsResultSet.next()){
                    user.addInterest(interestsResultSet.getString("interest"));
                }
                if(i == 0){
                    users.add(user);
                    i+=1;
                }else if(i != 0 && !users.get(i-1).getUserName().equals(user.getUserName())){
                    users.add(user);
                    i+=1;
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return users;
    }

    /**
     * This method access the database and queries all the entities in the application table
     * @return An observableList all user applications
     */
    public static ObservableList<UserObject> getAllApplications() {
        String sqlStatement = "SELECT * FROM application";
        UserObject temp = new UserObject();
        ObservableList<UserObject> applications = FXCollections.observableArrayList();
        try {
            ResultSet resultSet = DbUtil.processQuery(sqlStatement);
            //applications = createUserList(resultSet);
            while(resultSet.next())
            {
                temp = new UserObject(resultSet.getString("userName"),
                        resultSet.getString("password"),
                        resultSet.getString("firstName"),
                        resultSet.getString("lastName"),
                        resultSet.getInt("membershipLevel"));
                temp.addInterest(resultSet.getString("interest0"));
                temp.addInterest(resultSet.getString("interest1"));
                temp.addInterest(resultSet.getString("interest2"));
                applications.add(temp);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return applications;
        }
    }

    /**
     *
     * @param resultSet an object containing resulting table from querying the application or user table
     * @return an ObservableList containing user Objects
     * @throws SQLException
     */

    private static ObservableList<UserObject> createUserList(ResultSet resultSet) throws SQLException {
        ObservableList<UserObject> userList = FXCollections.observableArrayList();
        while (resultSet.next()) {
            UserObject userObject = new UserObject(resultSet.getString("username"), resultSet.getString("password"),
                    resultSet.getString("firstname"), resultSet.getString("lastname"),
                    resultSet.getInt("membershiplevel"));
            userList.add(userObject);
        }
        return userList;
    }

    public static void upgradeUserMembership(String userName){
        String SQLStatement = "UPDATE users set membershipLevel = 2 where userName = ?";
        DbUtil.executeUpdateDB(SQLStatement, userName);
    }

    public static int insertNewUser(UserObject newUser) {
        String sqlStatement1 = "INSERT INTO users VALUE(?,?,?,?,1)";
        String sqlStatement2 = "INSERT INTO interests VALUE(?,?)";
        int result = DbUtil.executeUpdateDB(sqlStatement1, newUser.getUserName(),newUser.getPassword(),
                newUser.getFirstName(), newUser.getLastName());
        List<String> interests = newUser.getInterests();
        for ( String interest: interests) {
            result += DbUtil.executeUpdateDB(sqlStatement2, interest);
        }
        return result;
    }

    public static int deleteApplication(String userName) {
        String sqlStatement = "DELETE FROM application WHERE userName= ?";
        int result = DbUtil.executeUpdateDB(sqlStatement, userName);
        return result;
    }
}
