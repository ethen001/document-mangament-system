
/*
This is a utility class for version control
 */
package org.guccigang.mini_google_docs.model;
import java.util.*;

public class VersionUtil
{
    private static diff_match_patch dmp = new diff_match_patch();

    public static void save(String docID, String newText, String author) throws java.sql.SQLException
    {
        int version;
        String currText;
        String diff;
        java.sql.ResultSet query = DbUtil.processQuery("SELECT COUNT(*) FROM revisions where docID="+docID+";");
        query.next();
        version = query.getInt(1)+1;//the current version is the number of past versions + 1

        query = DbUtil.processQuery("SELECT content FROM documents where docID="+docID+";");
        query.next();
        currText = query.getString(1);

        diff = getChanges(newText,currText);

        DbUtil.executeUpdateDB("INSERT INTO revisions VALUE("+docID+","+version+",\"1941-12-07\",?,?)",author,diff);
        DbUtil.executeUpdateDB("UPDATE documents SET content = \""+newText+"\" WHERE docID = ?",docID);
    }

    /**This function creates an empty document in the database and returns a docID to reference it
     *
     * @return an int which serves as the docID for the newly created document
     */
    public static String create(String owner,String title)throws java.sql.SQLException
    {
        DbUtil.executeUpdateDB("INSERT INTO documents (owner,docName,content,isLocked,restricted,createdDate,tabooFlag) VALUE(?,?,?,0,1,\"1941-12-07\",0)",owner,title,"");
        java.sql.ResultSet query = DbUtil.processQuery("select max(docID) from documents where docName=? and owner=?;",title,owner);
        query.next();
        return query.getString(1);
    }

    /**This function returns the contents of the current version of the document
     *
     * @return a string representing the contents of the current version of the doc
     */
    public static String open(String docID) throws java.sql.SQLException
    {
        //select content from documents where docID=1;
        java.sql.ResultSet query = DbUtil.processQuery("select content from documents where docID=?;",docID);
        query.next();
        return query.getString(1);
    }

    /**This function creates an empty document in the database and
     *
     * @return a string representing the contents of the given version of the doc
     */
    public static String openVersion(String docID, int version) throws java.sql.SQLException
    {
        version++;//account for version off-by-one
        java.sql.ResultSet query = DbUtil.processQuery("select content from documents where docID=?;",docID);
        query.next();
        String currVersion = query.getString(1);
        ArrayList<String> versions = new ArrayList<>();

        query = DbUtil.processQuery("SELECT content FROM revisions where docID="+docID+";");
        while(query.next())
        {
            String temp = query.getString(1);
            versions.add(temp);
            System.out.println(temp);
        }


        for(int i=versions.size()-1; i>version;i--)
        {
            currVersion = applyChanges(currVersion,versions.get(i));
        }
        return currVersion;
    }

    public static int getCurrentVersion(String docID) throws java.sql.SQLException
    {
        java.sql.ResultSet query = DbUtil.processQuery("SELECT COUNT(*) FROM revisions where docID="+docID+";");
        query.next();
        return query.getInt(1)+1;//the current version is the number of past versions + 1
    }

    /**This function takes in two strings and returns the list of differences from the first to the second.
     *
     * @param str1 first string
     * @param str2 string
     * @return a string detailing the list of changes
     */
    public static String getChanges(String str1, String str2)
    {
        LinkedList<diff_match_patch.Patch> p = dmp.patch_make(str1,str2);
        return dmp.patch_toText(p);
    }

    /**This function takes a string and applies the listOfChanges to it and returns the result
     *
     * @param originalString first string
     * @param listOfChanges string
     * @return a string detailing the list of changes
     */
    public static String applyChanges(String originalString, String listOfChanges)
    {
        List<diff_match_patch.Patch> pList = dmp.patch_fromText(listOfChanges);
        LinkedList<diff_match_patch.Patch> p = new LinkedList<>();
        p.addAll(pList);
        return (String) dmp.patch_apply(p,originalString)[0];
    }
}