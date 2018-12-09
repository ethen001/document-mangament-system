package org.guccigang.mini_google_docs.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DocumentDAO {

    /**
     * Queries the database for all documents.
     * @return documentFiles
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static ObservableList<DocumentFile> getAllDocumentFilesData() throws SQLException, ClassNotFoundException{
        String selectStatement = "SELECT * FROM documents";
        //Execute select statement
        try{

            ResultSet resultSet = DbUtil.processQuery(selectStatement);
            ObservableList<DocumentFile> documentFiles = getAllReadableDocuments(resultSet);
            return documentFiles;

        }catch (SQLException e){
            System.out.println("SQL query has failed" + e);
            throw e;
        }
    }

    /**
     *Use ResultSet from DB as parameter and set Document attributes as such then returns every document from database.
     * @param resultSet
     * @return
     * @throws SQLException
     */
    private static ObservableList<DocumentFile> getAllDocuments(ResultSet resultSet)throws SQLException{
        ObservableList<DocumentFile> documentFiles = FXCollections.observableArrayList();

        while(resultSet.next()){
            DocumentFile document = new DocumentFile();
            document.setiD(resultSet.getInt("docID"));
            document.setOwner(resultSet.getString("owner").toString());
            document.setDocumentName(resultSet.getString("docName"));
            document.setContent(resultSet.getString("content"));
            document.setLock(resultSet.getInt("isLocked"));
            document.setRestricted(resultSet.getInt("restricted"));
            //Gonna leave it for now. might just change document date to string unless you guys can fix it.
            //document.setDate(new Date(resultSet.getString("createdDate")));
            document.setTabooFlag(resultSet.getInt("tabooFlag"));

            documentFiles.add(document);
        }
        return documentFiles;
    }

    /**
     * Returns a observable list of readable documents. Restricted level greater or equal to 2.
     * 0 = private, 1 = shared, 2 = public, 3 = restricted.
     * 2 and 3 are readable. 2 is read while 3 is read/write.
     * @param resultSet
     * @return documentFiles
     * @throws SQLException
     */
    private static ObservableList<DocumentFile> getAllReadableDocuments(ResultSet resultSet)throws SQLException{
        ObservableList<DocumentFile> documentFiles = FXCollections.observableArrayList();
        while(resultSet.next()){
            if(resultSet.getInt("restricted") >= 2){
                DocumentFile document = new DocumentFile();
                document.setiD(resultSet.getInt("docID"));
                document.setOwner(resultSet.getString("owner").toString());
                //document.setDocumentName(resultSet.getString("docName"));
                document.setContent(resultSet.getString("content"));
                document.setLock(resultSet.getInt("isLocked"));
                document.setRestricted(resultSet.getInt("restricted"));
                //Gonna leave it for now. might just change document date to string unless you guys can fix it.
                //document.setDate(new Date(resultSet.getString("createdDate")));
                document.setTabooFlag(resultSet.getInt("tabooFlag"));
                documentFiles.add(document);

            }
        }
        return documentFiles;
    }


    /**
     * Queries the database for all documents.
     * @return documentFiles
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static ObservableList<DocumentFile> getSpecificsUsersDocuments(String userName) throws SQLException, ClassNotFoundException{
        String selectStatement = "SELECT * FROM documents where owner = ?";
        //Execute select statment
        try{

            ResultSet resultSet = DbUtil.processQuery(selectStatement,userName);
            ObservableList<DocumentFile> documentFiles = getAllReadableDocuments(resultSet);
            return documentFiles;

        }catch (SQLException e){
            System.out.println("SQL query has failed" + e);
            throw e;
        }
    }

    /**
     *Use ResultSet from DB as parameter and set Document attributes as such then returns every document from database.
     * @param resultSet
     * @return
     * @throws SQLException
     */
    private static ObservableList<DocumentFile> getSpecificUserDocumentsList(ResultSet resultSet)throws SQLException{
        ObservableList<DocumentFile> documentFiles = FXCollections.observableArrayList();

        while(resultSet.next()){
            DocumentFile document = new DocumentFile();
            document.setiD(resultSet.getInt("docID"));
            document.setOwner(resultSet.getString("owner").toString());
            document.setDocumentName(resultSet.getString("docName"));
            document.setContent(resultSet.getString("content"));
            document.setLock(resultSet.getInt("isLocked"));
            document.setRestricted(resultSet.getInt("restricted"));
            //Gonna leave it for now. might just change document date to string unless you guys can fix it.
            //document.setDate(new Date(resultSet.getString("createdDate")));
            document.setTabooFlag(resultSet.getInt("tabooFlag"));

            documentFiles.add(document);
        }
        return documentFiles;
    }


}
