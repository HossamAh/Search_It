import javax.print.DocFlavor;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.*;

public class SQLiteJDBC {

    //these are the column names
    public static final String COL_pageID = "COL_pageID";
    public static final String COL_word = "COL_word";
    public static final String COL_count = "COL_count";

    public static final String COL_pageID_links = "COL_pageIDLinks";
    public static final String COL_links = "COL_links";
    public static final String link_id = "link_id";

    public static final String COL_ID = "page_id";
    public static final String COL_title = "COL_title";
    public static final String COL_URL = "COL_URL";
    public static final String COL_pop = "COL_popularity";
    public static final String COL_description = "COL_description";
    public static final String COL_wordCount = "COL_wordCount";

    public static final String COL_single_word = "COL_singleWord";
    public static final String COL_docs_count = "COL_docsCount";

    public static final String COL_ImageID_word = "COL_ImageID_word";
    public static final String COL_ImageWord = "COL_ImageWord";

    public static final String COL_Image_ID = "COL_ImageID";
    public static final String COL_ABSurl = "COL_ABSurl";
    public static final String COL_pageID_image = "COL_pageIDImage";
    public static final String COL_Caption = "COL_Caption";

    private static final String DATABASE_NAME = "indexer";
    private static final String TABLE_NAME3 = "TABLE_words";
    private static final String TABLE_NAME2 = "TABLE_links";
    private static final String TABLE_NAME1 = "page_info";
    private static final String TABLE_NAME4 = "TABLE_Word_info";
    private static final String TABLE_NAME5 = "TABLE_images";
    private static final String TABLE_NAME6 = "TABLE_ImageWord";

    private Connection connect = null;
    private Statement statement = null;
    private ResultSet rs = null;

    //function to start the connection with the database.
    public void open() throws ClassNotFoundException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/indexer?characterEncoding=utf8&useSSL=false&useUnicode=true", "root", "database");

            statement = connect.createStatement();
            System.out.println("Opened database successfully");

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void close() throws ClassNotFoundException {
        try {
            System.out.println("close database");
            connect.close();
            statement.close();
            //rs.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    //store page in the database and all its attributes.
    public void createPage(Page p) {
        try {
            //store the page in the database.
            int id =0;
            String sql = "INSERT INTO " + TABLE_NAME1 + " (" + COL_title + "," + COL_URL + "," + COL_description + ","+COL_wordCount+","+COL_pop+") " +
                    "VALUES (?, ?,?,?,? );";
            PreparedStatement pstmt = connect.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, p.gettitle());
            pstmt.setString(2, p.getURL());
            pstmt.setString(3, p.getDescription());
            pstmt.setInt(4, p.getWordCount());
            pstmt.setDouble(5, p.getPop());
            pstmt.executeUpdate();
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    id = rs.getInt(1);
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }

            //store all the words that a page contains in the database.
            Hashtable<String, Integer> s = new Hashtable<String, Integer>();
            s = p.getwords();
            Set<String> keys = s.keySet();
            for (String key : keys) {
                //TODO: call the function that adds every word to  TABLE4
                if(!key.equals("")){
                    sql = "INSERT INTO " + TABLE_NAME3 + " (" + COL_pageID + "," + COL_word + "," + COL_count + ") " +
                            "VALUES (?, ?, ?);";
                    addWordsCount(key);
                    pstmt = connect.prepareStatement(sql);
                    pstmt.setInt(1, id);
                    pstmt.setString(2, key);
                    pstmt.setString(3, (s.get(key)).toString());
                    pstmt.executeUpdate();
                }
            }

            //store all the links that a page contains in the database.
            ArrayList<String> linkat = new ArrayList<String>();
            linkat = p.getLinks();
            for (int i = 0; i < linkat.size(); i++) {
                sql ="INSERT INTO " + TABLE_NAME2 + " (" + COL_pageID_links + "," + COL_links +"," + link_id + ") " +
                        "VALUES (?, ?, ?);";
                pstmt = connect.prepareStatement(sql);
                pstmt.setInt(1, id);
                pstmt.setString(2, linkat.get(i));
                pstmt.setInt(3, id+i);
                pstmt.executeUpdate();
            }
            //store all the images that a page contain in database.
            Hashtable<String,ArrayList<String>> images;
            images = p.getImages();
            keys = images.keySet();
            int imageID = 0;
            if(!images.isEmpty()) {
                for (String key : keys) {

                    sql = "INSERT INTO " + TABLE_NAME5 + " (" + COL_pageID_image  + "," + COL_ABSurl +"," + COL_Caption+ ") " +
                            "VALUES (?, ?, ?);";
                    pstmt = connect.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
                    pstmt.setInt(1, id);
                    pstmt.setString(2, key);
                    pstmt.setString(3, images.get(key).get(0));
                    pstmt.executeUpdate();
                    try (ResultSet rs = pstmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            imageID = rs.getInt(1);
                        }
                    } catch (SQLException ex) {
                        System.out.println(ex.getMessage());
                    }
                    for(int j=1;j<images.get(key).size();j++){
                        sql = "INSERT INTO " + TABLE_NAME6 + " (" + COL_ImageID_word+ "," + COL_ImageWord+ ") " +
                                "VALUES (?, ?);";
                        pstmt = connect.prepareStatement(sql);
                        pstmt.setInt(1, imageID);
                        pstmt.setString(2, images.get(key).get(j));
                        pstmt.executeUpdate();
                    }
                }
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Records created successfully");
    }


    //add the number of docs containing a word.
    public void addWordsCount(String word) {
        try {
            String sql = "SELECT * FROM " + TABLE_NAME4 + " where " + COL_single_word + " = '" + word + "' ;";
            rs = statement.executeQuery(sql);

            if (rs.next()) {
                int count = rs.getInt(COL_docs_count);
                count = count + 1;
                sql = "UPDATE " + TABLE_NAME4 + " set " + COL_docs_count + " = ? where " + COL_single_word + " = ?;";
                PreparedStatement upd = connect.prepareStatement(sql);
                upd.setInt(1, count); // replace first ? with value for first name
                upd.setString(2, word);    // replace second ? with value for userid
                upd.executeUpdate();
            } else {
                sql = "INSERT INTO " + TABLE_NAME4 + " (" + COL_single_word + "," + COL_docs_count + ") " +
                        "VALUES (?, ?);";
                PreparedStatement pstmt = connect.prepareStatement(sql);
                pstmt = connect.prepareStatement(sql);
                pstmt.setString(1, word);
                pstmt.setString(2, String.valueOf(1));
                pstmt.executeUpdate();
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    //retrieve page by url.
    public Page retrievePage(String url) {
        Page p = null;
        try {
            rs = statement.executeQuery("SELECT * FROM " + TABLE_NAME1 + " where " + COL_URL + " = '" + url + "' ;");
            if(rs.next()) {
                p = new Page(rs.getString(COL_URL), rs.getString(COL_title), rs.getString(COL_description), rs.getInt(COL_ID));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return p;
    }

    //retrieve page by ID.
    public Page retrievePageByID(int id) {
        Page p =null;
        try {
            rs = statement.executeQuery("SELECT * FROM " + TABLE_NAME1 + " where " + COL_ID + " = " + id + ";");
            if (rs.next()) {
                if((rs.getDouble(COL_pop))!=-111111111.0){
                    p = new Page(rs.getString(COL_URL), rs.getString(COL_title), rs.getString(COL_description), rs.getInt(COL_ID),rs.getInt(COL_wordCount),rs.getDouble(COL_pop));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return p;
    }
    //return the images of certain page.
    public Hashtable<String,ArrayList<Crawler.image>> retriveImages(String query) {
        Hashtable<String,ArrayList<Crawler.image>> images= new Hashtable<>();
        ArrayList<Integer>IDs = new ArrayList<>();
        try {
            String[] arrOfStr = query.split(" ");
            for (String a : arrOfStr) {
                rs = statement.executeQuery("SELECT * FROM " +TABLE_NAME6 +" , "+TABLE_NAME5 +" , "+TABLE_NAME1 +" where "+
                        COL_Image_ID + " = " + COL_ImageID_word + " and "+COL_ID+" = "+COL_pageID_image + " and "+ COL_ImageWord + " = '" + a + "';");
                while (rs.next()) {
                    if(!IDs.contains(rs.getInt(COL_ImageID_word))){
                        IDs.add(rs.getInt(COL_ImageID_word));
                    }
                    if (!images.containsKey(rs.getString(COL_URL))) {
                            images.put(rs.getString(COL_URL), new ArrayList<>());
                        }
                        images.get(rs.getString(COL_URL)).add(new Crawler.image(rs.getString(COL_ABSurl), rs.getString(COL_Caption)));

                }
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return images;
    }

    //do the phrase searching.
    public List<Page> PhraseSearching(String phrase) {
        List<Page> pages = new ArrayList<Page>();
        try {
            rs = statement.executeQuery("SELECT * FROM " + TABLE_NAME1 + " where " + COL_title +" like" + "'%" +phrase+"%'"+" or " +COL_description +" like" + "'%" +phrase+"%'" +";");

            while (rs.next()) {
                pages.add(new Page(rs.getString(COL_URL),rs.getString(COL_title),rs.getString(COL_description),rs.getInt(COL_ID)));
            }
            for(int i=0;i<pages.size();i++){
                pages.get(i).setwords(retrieveAllWords(pages.get(i).getID()));
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return pages;
    }


    ///////retrieve word's count in documents.
    public int retrieveWordsCount(String word) {
        int count = 0;
        try {
            rs = statement.executeQuery("SELECT * FROM " + TABLE_NAME4 + " where " + COL_single_word + " = '" + word + "';");
            if(rs.next()){
                count = rs.getInt(COL_docs_count);
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return count;
    }

    //set the page popularity.
    public void setPopularity(int id, double pop){
        String sql = "UPDATE " + TABLE_NAME1 + " set " + COL_pop + " = ? where " + COL_ID + " = ?;";
        PreparedStatement upd;
        try {
            upd = connect.prepareStatement(sql);
            upd.setDouble(1, pop); // replace first ? with value for first name
            upd.setInt(2, id);    // replace second ? with value for userid
            upd.executeUpdate();
            upd.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //retrieve the popularity of a page.
    public double getPopularity(int id) throws SQLException {
        try {
            rs = statement.executeQuery("SELECT "+ COL_pop+" FROM " + TABLE_NAME1 + " where " + COL_ID + " = " + id + ";");
            if(rs.next()){
                return rs.getDouble(COL_pop);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return 0;
    }

   //retrieve the words and its count in a page.
    public Hashtable<String, Integer> retrieveWords(int id, String word) {
        Hashtable<String, Integer> words = new Hashtable<String, Integer>();
        try {
            String[] arrOfStr = word.split(" ");
            for (String a : arrOfStr) {
                rs = statement.executeQuery("SELECT * FROM " + TABLE_NAME3 + " where " + COL_pageID + " = " + id + " and "+
                COL_word+ " = '"+ a + "';");
                while (rs.next()) {
                    words.put(rs.getString(COL_word), rs.getInt(COL_count));
                }
            }

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return words;
    }


    //retrieve all words in a page.
    public Hashtable<String, Integer> retrieveAllWords(int id) {
        Hashtable<String, Integer> words = new Hashtable<String, Integer>();
        try {
            rs = statement.executeQuery("SELECT * FROM " + TABLE_NAME3 + " where " + COL_pageID + " = " + id + ";");
            while (rs.next()) {
                words.put(rs.getString(COL_word), rs.getInt(COL_count));
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return words;
    }

    //retrieve all page in database.
    public List<Page> retrieveAllPages() {
        List<Page> pages = new ArrayList<Page>();
        try {
            Page p ;
            rs = statement.executeQuery("SELECT * FROM " + TABLE_NAME1 +";");
            while (rs.next()) {
                p = new Page(rs.getString(COL_URL),rs.getString(COL_title),rs.getString(COL_description),rs.getInt(COL_ID));
                pages.add(p);
            }
            for(int i=0;i<pages.size();i++){
                pages.get(i).setLinks(retrieveAllLinks(pages.get(i).getID()));
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return pages;
    }

    //retrieve all links that a page contains.
    public ArrayList<String> retrieveAllLinks(int id) {

        ArrayList<String> linkat = new ArrayList<String>();
        try {
            rs = statement.executeQuery("SELECT * FROM " + TABLE_NAME2 + " where " + COL_pageID_links + " = " + id + ";");
            while (rs.next()) {
                linkat.add(rs.getString(COL_links));
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return linkat;
    }

    /////////////retrieve pages containing a specific word.
    public List<Page> retrievePages(String word) {
        List<Page> pages = new ArrayList<Page>();
        List<Integer> ID = new ArrayList<Integer>();
        try {
            rs = statement.executeQuery("SELECT * FROM " + TABLE_NAME3 + " where " + COL_word + " = '" + word + "';");
            while (rs.next()) {
                ID.add(rs.getInt(COL_pageID));
            }
            for (int i=0; i< ID.size();i++){
                if(retrievePageByID(ID.get(i)) == null) {
                    pages.add(retrievePageByID(ID.get(i)));
                }
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return pages;
    }

    /////////////retrieve pages containing a specific set of words.
    public List<Page> retrievePagesSetWords(String words) throws IOException {
        List<Page> pages = new ArrayList<Page>();
        List<Page> retrievedPages;
        boolean found = false;
        String[] arrOfStr = words.split(" ");
        for (String a : arrOfStr) {
            retrievedPages = retrievePages(a);
            for (int i = 0; i < retrievedPages.size(); i++) {
                for (int j = 0; j < pages.size(); j++) {
                    if (pages.get(j).getID() == retrievedPages.get(i).getID()) {
                        found = true;
                    }
                }
                if (!found) {
                    retrievedPages.get(i).setwords(retrieveWords(retrievedPages.get(i).getID(), words));
                    pages.add(retrievedPages.get(i));
                }
            }
        }

        return pages;
    }
}
