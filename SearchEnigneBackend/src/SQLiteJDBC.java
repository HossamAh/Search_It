//import java.io.IOException;
//import java.io.ObjectOutputStream;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.sql.*;
//import java.util.*;
//
//public class SQLiteJDBC {
//
//    //these are the column names
//    public static final String COL_pageID = "COL_pageID";
//    public static final String COL_word = "COL_word";
//    public static final String COL_count = "COL_count";
//
//    public static final String COL_pageID_links = "COL_pageID_links";
//    public static final String COL_links = "COL_links";
//    public static final String link_id = "link_id";
//
//    public static final String COL_ID = "page_id";
//    public static final String COL_title = "COL_title";
//    public static final String COL_URL = "COL_URL";
//    public static final String COL_pop = "COL_pop";
//    public static final String COL_description = "COL_description";
//
//    public static final String COL_single_word = "COL_single_word";
//    public static final String COL_docs_count = "COL_docs_count";
//
//    //public static final String COL_Image_ID = "COL_Image_ID";
//    public static final String COL_Caption = "COL_Caption";
//    public static final String COL_ABSurl = "COL_ABSurl";
//
//    public static final String COL_pageID_image = "COL_pageID_image";
//    //public static final String COL_ImageID = "ImageID";
//
//    private static final String DATABASE_NAME = "db_indexer";
//    private static final String TABLE_NAME3 = "TABLE_words";
//    private static final String TABLE_NAME2 = "TABLE_links";
//    private static final String TABLE_NAME1 = "page_info";
//    private static final String TABLE_NAME4 = "TABLE_Word_info";
//    private static final String TABLE_NAME5 = "TABLE_NAME5";
//
//    private Connection connect = null;
//    private Statement statement = null;
//    //    private PreparedStatement preparedStatement = null;
//    private ResultSet rs = null;
//
//    public void open() throws ClassNotFoundException {
//        try {
//            Class.forName("com.mysql.jdbc.Driver");
//            connect = DriverManager.getConnection(
//                    "jdbc:mysql://localhost:3306/indexer", "root", "database");
//            //here sonoo is database name, root is username and password
//            statement = connect.createStatement();
//            System.out.println("Opened database successfully");
//            //con.close();
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//    }
//
//    public void close() throws ClassNotFoundException {
//        try {
//            connect.close();
//            statement.close();
//            rs.close();
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//    }
//
//    ///////////////when adding a page to the DB.
//    public void createPage(Page p) {
//        //Connection c = null;
//        //Statement stmt = null;
//
//        try {
//
//            System.out.println("Opened database successfully");
//            int id =0;
//            //stmt = c.createStatement();
//            String sql = "INSERT INTO " + TABLE_NAME1 + " (" + COL_title + "," + COL_URL + "," + COL_description +") " +
//                    "VALUES (?, ?,? );";
//            PreparedStatement pstmt = connect.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
////            System.out.println(sql);
////            statement.executeUpdate(sql);
//            pstmt.setString(1, p.gettitle());
//            pstmt.setString(2, p.getURL());
//            pstmt.setString(3, p.getDescription());
//            pstmt.executeUpdate();
//            try (ResultSet rs = pstmt.getGeneratedKeys()) {
//                if (rs.next()) {
//                    id = rs.getInt(1);
//                    System.out.println(id);
//                }
//            } catch (SQLException ex) {
//                System.out.println(ex.getMessage());
//            }
////            ResultSet rs = statement.executeQuery("SELECT " + COL_ID + "FROM " + TABLE_NAME1 + " where " + COL_URL + " = " + p.getURL() + ";");
////            id = rs.getInt(COL_pageID);
//            Hashtable<String, Integer> s = new Hashtable<String, Integer>();
//            s = p.getwords();
//            Set<String> keys = s.keySet();
//            for (String key : keys) {
//                //TODO: call the function that adds every word to  TABLE4
//                if(!key.equals("")){
//                    //System.out.println(key);
//                    sql = "INSERT INTO " + TABLE_NAME3 + " (" + COL_pageID + "," + COL_word + "," + COL_count + ") " +
//                            "VALUES (?, ?, ?);";
//                    addWordsCount(key);
//                    pstmt = connect.prepareStatement(sql);
//                    pstmt.setInt(1, id);
//                    pstmt.setString(2, key);
//                    pstmt.setString(3, (s.get(key)).toString());
//                    pstmt.executeUpdate();
//                }
//            }
//
//            ArrayList<String> linkat = new ArrayList<String>();
//            linkat = p.getLinks();
//
//            for (int i = 0; i < linkat.size(); i++) {
//                sql ="INSERT INTO " + TABLE_NAME2 + " (" + COL_pageID_links + "," + COL_links +"," + link_id + ") " +
//                        "VALUES (?, ?, ?);";
//                //System.out.println(linkat.get(i));
//                pstmt = connect.prepareStatement(sql);
//                pstmt.setInt(1, id);
//                pstmt.setString(2, linkat.get(i));
//                pstmt.setInt(3, id+i);
//                pstmt.executeUpdate();
//            }
//            Set<Crawler.image> images = new HashSet<Crawler.image>();
//            //images = p.getImages();
//            for (Crawler.image img : images) {
//                sql ="INSERT INTO " + TABLE_NAME5 + " (" + COL_pageID_image + "," + COL_Caption + "," + COL_ABSurl + ") " +
//                        "VALUES (?, ?, ?);";
//                pstmt = connect.prepareStatement(sql);
//                pstmt.setInt(1, id);
//                pstmt.setString(2, img.imageCaption);
//                pstmt.setString(3, img.imageSrc);
//                pstmt.executeUpdate();
//            }
//        } catch (Exception e) {
//            System.err.println(e.getClass().getName() + ": " + e.getMessage());
//            System.exit(0);
//        }
//        System.out.println("Records created successfully");
//    }
//
//    ///////////////retureive page iD by url.
//    public int retrievePageID(String url) {
//        //Connection c = null;
//        //Statement stmt = null;
//        int id = 0;
//        try {
//            //c = this.open();
//            rs = statement.executeQuery("SELECT " + COL_ID + "FROM " + TABLE_NAME1 + " where " + COL_URL + " = " + url + ";");
//            id = rs.getInt(COL_ID);
//            rs.close();
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return id;
//    }
//
//    ///////////////add the number of docs containing a word.
//    public void addWordsCount(String word) {
//        // Connection c = null;
//        //Statement stmt = null;
//        try {
//            //c = this.open();
//
//            //stmt = c.createStatement();
//            String sql = "SELECT * FROM " + TABLE_NAME4 + " where " + COL_single_word + " = '" + word + "' ;";
//            //System.out.println(sql);
//            rs = statement.executeQuery(sql);
//
//            if (rs.next()) {
//                int count = rs.getInt(COL_docs_count);
//                count = count + 1;
//                sql = "UPDATE " + TABLE_NAME4 + " set " + COL_docs_count + " = ? where " + COL_single_word + " = ?;";
//                PreparedStatement upd = connect.prepareStatement(sql);
//                upd.setInt(1, count); // replace first ? with value for first name
//                upd.setString(2, word);    // replace second ? with value for userid
//                upd.executeUpdate();
//            } else {
//                sql = "INSERT INTO " + TABLE_NAME4 + " (" + COL_single_word + "," + COL_docs_count + ") " +
//                        "VALUES (?, ?);";
//                PreparedStatement pstmt = connect.prepareStatement(sql);
//                pstmt = connect.prepareStatement(sql);
//                pstmt.setString(1, word);
//                pstmt.setString(2, String.valueOf(1));
//                pstmt.executeUpdate();
//            }
//            rs.close();
////            stmt.close();
////            c.close();
//        } catch (Exception e) {
//            System.err.println(e.getClass().getName() + ": " + e.getMessage());
//            System.exit(0);
//        }
//        System.out.println("Operation done successfully");
//    }
//
//    ////////////////retrieve page by url.
//    public Page retrievePage(String url) {
//        //Connection c = null;
//        //Statement stmt = null;
//        Page p = null;
//        try {
//            //c = this.open();
//            rs = statement.executeQuery("SELECT * FROM " + TABLE_NAME1 + " where " + COL_URL + " = '" + url + "' ;");
//            p = new Page(rs.getString(COL_URL), rs.getString(COL_title),rs.getString(COL_description));
//
//            //rs.close();
////            stmt.close();
////            c.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return p;
//    }
//
//    ////////////////retrieve page by ID.
//    public Page retrievePageByID(int id) {
//        //Connection c = null;
//        //Statement stmt = null;
//        Page p = null;
//        try {
//            //c = this.open();
//            rs = statement.executeQuery("SELECT * FROM " + TABLE_NAME1 + " where " + COL_ID + " = " + id + ";");
//            if (rs.next()) {
//                p = new Page(rs.getString(COL_URL), rs.getString(COL_title), rs.getString(COL_description), rs.getInt(COL_ID));
//            }
//            //rs.close();
////            stmt.close();
////            c.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return p;
//    }
//
//    ////////////////return the images of certain page.
//    public Set<Crawler.image> retriveImages(int id) {
//        //int id = retrievePageID(url);
//        //Connection c = null;
//        //Statement stmt = null;
//        Set<Crawler.image> images = new HashSet<Crawler.image>();
//        try {
//            //c = this.open();
//            //stmt = c.createStatement();
//            rs = statement.executeQuery("SELECT * FROM " + TABLE_NAME5 + " where " + COL_pageID_image + " = " + id + ";");
//            while (rs.next()) {
//                images.add(new Crawler.image(rs.getString(COL_ABSurl), rs.getString(COL_Caption)));
//            }
//            //rs.close();
////            stmt.close();
////            c.close();
//        } catch (Exception e) {
//            System.err.println(e.getClass().getName() + ": " + e.getMessage());
//            System.exit(0);
//        }
//        return images;
//    }
//
//    ///////retrieve word count in documents.
//    public int retrieveWordsCount(String word) {
//        //Connection c = null;
//        //Statement stmt = null;
//        int count = 0;
//        try {
//            //c = this.open();
//            //stmt = c.createStatement();
//            rs = statement.executeQuery("SELECT * FROM " + TABLE_NAME4 + " where " + COL_single_word + " = '" + word + "';");
//            if(rs.next()){
//                count = rs.getInt(COL_docs_count);
//            }
//            //rs.close();
////            stmt.close();
////            c.close();
//        } catch (Exception e) {
//            System.err.println(e.getClass().getName() + ": " + e.getMessage());
//            System.exit(0);
//        }
//        return count;
//    }
//
//    public Hashtable<String, Integer> retrieveSetOfWordsCount(String word) {
//
//        Hashtable<String, Integer> allWords = new Hashtable<String, Integer>();
//        String[] arrOfStr = word.split(" ");
//        for (String a : arrOfStr) {
//            allWords.put(a,retrieveWordsCount(a));
//        }
//        return allWords;
//    }
//
//    //TODO:set popularity of a page.
//
//    public void setPop(int id, double pop){
//        String sql = "UPDATE " + TABLE_NAME1 + " set " + COL_pop + " = ? where " + COL_ID + " = ?;";
//        PreparedStatement upd;
//        try {
//            upd = connect.prepareStatement(sql);
//            upd.setDouble(1, pop); // replace first ? with value for first name
//            upd.setInt(2, id);    // replace second ? with value for userid
//            upd.executeUpdate();
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//    }
//
//    public double getPop(int id) throws SQLException {
//        try {
//            rs = statement.executeQuery("SELECT "+ COL_pop+" FROM " + TABLE_NAME1 + " where " + COL_ID + " = " + id + ";");
//
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//        return rs.getDouble(COL_pop);
//    }
//
//    public Hashtable<String, Integer> retrieveWords(int id, String word) {
//        //int id = retrievePageID(url);
//        Hashtable<String, Integer> words = new Hashtable<String, Integer>();
//        //Connection c = null;
//        //Statement stmt = null;
//        try {
//            //c = this.open();
//            //stmt = c.createStatement();
//            String[] arrOfStr = word.split(" ");
//            for (String a : arrOfStr) {
//                rs = statement.executeQuery("SELECT * FROM " + TABLE_NAME3 + " where " + COL_pageID + " = " + id + " and "+
//                        COL_word+ " = '"+ a + "';");
//                while (rs.next()) {
//                    words.put(rs.getString(COL_word), rs.getInt(COL_count));
//                }
//            }
//
//            //rs.close();
////            stmt.close();
////            c.close();
//        } catch (Exception e) {
//            System.err.println(e.getClass().getName() + ": " + e.getMessage());
//            System.exit(0);
//        }
//        return words;
//    }
//
//
//    public Hashtable<String, Integer> retrieveAllWords(int id) {
//        //int id = retrievePageID(url);
//        Hashtable<String, Integer> words = new Hashtable<String, Integer>();
//        //Connection c = null;
//        //Statement stmt = null;
//        try {
//            //c = this.open();
//            //stmt = c.createStatement();
//            rs = statement.executeQuery("SELECT * FROM " + TABLE_NAME3 + " where " + COL_pageID + " = " + id + ";");
//            while (rs.next()) {
//                words.put(rs.getString(COL_word), rs.getInt(COL_count));
//            }
//            //rs.close();
////            stmt.close();
////            c.close();
//        } catch (Exception e) {
//            System.err.println(e.getClass().getName() + ": " + e.getMessage());
//            System.exit(0);
//        }
//        return words;
//    }
//
//    public ArrayList<String> retrieveAllLinks(int id) {
//        //int id = retrievePageID(url);
//
//        ArrayList<String> linkat = new ArrayList<String>();
//        //Connection c = null;
//        //Statement stmt = null;
//        try {
//            //c = this.open();
//            //stmt = c.createStatement();
//            rs = statement.executeQuery("SELECT * FROM " + TABLE_NAME2 + " where " + COL_pageID_links + " = " + id + ";");
//            while (rs.next()) {
//                linkat.add(rs.getString(COL_links));
//            }
//            //rs.close();
////            stmt.close();
////            c.close();
//        } catch (Exception e) {
//            System.err.println(e.getClass().getName() + ": " + e.getMessage());
//            System.exit(0);
//        }
//        return linkat;
//    }
//
//    /////////////retrieve pages containing a specific word.
//    public List<Page> retrievePages(String word) {
//        List<Page> pages = new ArrayList<Page>();
//        List<Integer> ID = new ArrayList<Integer>();
//        //Connection c = null;
//        //Statement stmt = null;
//        try {
//            //c = this.open();
//            //stmt = c.createStatement();
//            rs = statement.executeQuery("SELECT * FROM " + TABLE_NAME3 + " where " + COL_word + " = '" + word + "';");
//            while (rs.next()) {
//                System.out.println(rs.getInt(COL_pageID));
//                ID.add(rs.getInt(COL_pageID));
//            }
//            for (int i=0; i< ID.size();i++){
//                pages.add(retrievePageByID(ID.get(i)));
//            }
////            rs.close();
////            stmt.close();
////            c.close();
//        } catch (Exception e) {
//            System.err.println(e.getClass().getName() + ": " + e.getMessage());
//            System.exit(0);
//        }
//        return pages;
//    }
//
//    /////////////retrieve pages containing a specific set of words.
//    public List<Page> retrievePagesSetWords(String words) throws IOException {
//        List<Page> pages = new ArrayList<Page>();
//        List<Page> retrievedPages;
//        boolean found = false;
//        String[] arrOfStr = words.split(" ");
//        for (String a : arrOfStr) {
//            System.out.println(a);
//            retrievedPages = retrievePages(a);
//            for (int i = 0; i < retrievedPages.size(); i++) {
//                for (int j = 0; j < pages.size(); j++) {
//                    if (pages.get(j).getID() == retrievedPages.get(i).getID()) {
//                        found = true;
//                    }
//                }
//                if (!found) {
//                    //retrievedPages.get(i).setImages(retriveImages(retrievedPages.get(i).getID()));
//                    retrievedPages.get(i).setLinks(retrieveAllLinks(retrievedPages.get(i).getID()));
//                    retrievedPages.get(i).setwords(retrieveWords(retrievedPages.get(i).getID(), words));
//                    pages.add(retrievedPages.get(i));
//                    //retrievedPages.get(i);
//                }
//            }
//        }
//        Page.printPages(pages);
//        // Server will be started on 1700 port number
//        ServerSocket server=new ServerSocket(1700);
//
//        // Server listening for connection
//        Socket s=server.accept();
//
//        // Stream object for sending object
//        ObjectOutputStream os=new ObjectOutputStream(s.getOutputStream());
//
//
//        os.writeObject(pages);
//        System.out.println("hello it's me");
//        s.close();
//        return pages;
//    }
//
//    /////////////retrieve pages containing a specific word by joining two tables.
//    public Hashtable<Page, Integer> retrievePagesJoin(String word) {
//        List<Page> pages = new ArrayList<Page>();
//        Hashtable<Page, Integer> lista = new Hashtable<Page, Integer>();
//        Page p;
//        //Connection c = null;
//        //Statement stmt = null;
//        try {
//            //c = this.open();
//            //stmt = c.createStatement();
//            rs = statement.executeQuery("SELECT * FROM " + TABLE_NAME3 + " m INNER JOIN " + TABLE_NAME1 + " w ON w.COL_pageID = m.page_id where " + COL_word + " = '" + word + "';");
//            while (rs.next()) {
//                p = new Page(rs.getString(COL_URL), rs.getString(COL_title), rs.getInt(COL_pageID));
//                lista.put(p, rs.getInt(COL_count));
//                //pages.add(retrievePageByID(rs.getInt(COL_pageID)));
//            }
//            //rs.close();
////            stmt.close();
////            c.close();
//        } catch (Exception e) {
//            System.err.println(e.getClass().getName() + ": " + e.getMessage());
//            System.exit(0);
//        }
//        return lista;
//    }
//    /////////////retrieve pages containing a specific set of words.
//    public List<Page> retrievePagesSetWordsByjoining(String words) {
//        List<Page> pages = new ArrayList<Page>();
//        List<Page> retrievedPages;
//        Hashtable<Page, Integer> lista;
//        boolean found = false;
//        String[] arrOfStr = words.split(" ");
//        for (String a : arrOfStr) {
//            retrievedPages = retrievePages(a);
//            for (int i = 0; i < retrievedPages.size(); i++) {
//                for (int j = 0; i < pages.size(); j++) {
//                    if (pages.get(j).getID() == pages.get(i).getID()) {
//                        found = true;
//                    }
//                }
//                if (!found) {
//                    //retrievedPages.get(i).setImages(retriveImages(retrievedPages.get(i).getID()));
//                    retrievedPages.get(i).setLinks(retrieveAllLinks(retrievedPages.get(i).getID()));
//                    retrievedPages.get(i).setwords(retrieveAllWords(retrievedPages.get(i).getID()));
//                    pages.add(retrievedPages.get(i));
//                }
//            }
//        }
//        return pages;
//    }
//}
//
//
//
////    public static void main(String args[]){
////        try{
////            Class.forName("com.mysql.jdbc.Driver");
////            Connection con=DriverManager.getConnection(
////                    "jdbc:mysql://localhost:3306/indexer","root","database");
////          //here sonoo is database name, root is username and password
////            Statement stmt=con.createStatement();
////            System.out.println("Opened database successfully");
////
//////            ResultSet rs=stmt.executeQuery("select * from page_info");
//////            while(rs.next())
//////                System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3));
////            con.close();
////        }catch(Exception e){ System.out.println(e);}
////    }
////}
//
////    public void createDB(){
////        Connection c = null;
////        SQLiteJDBC db = new SQLiteJDBC();
////        try {
////            Class.forName("org.sqlite.JDBC");
////            c = DriverManager.getConnection("jdbc:sqlite:" + DATABASE_NAME + ".db");
////
////        } catch (Exception e) {
////            System.err.println(e.getClass().getName() + ": " + e.getMessage());
////            System.exit(0);
////        }
////        System.out.println("Opened database successfully");
////        db.createTb1();
////        db.createTb2();
////        db.createTb3();
////        db.createTb4();
////        db.createTb5();
////    }
//
////    public static void main(String[] arg) {
////        Connection c = null;
////        SQLiteJDBC db = new SQLiteJDBC();
////        try {
////            Class.forName("org.sqlite.JDBC");
////            c = DriverManager.getConnection("jdbc:sqlite:" + DATABASE_NAME + ".db");
////
////        } catch (Exception e) {
////            System.err.println(e.getClass().getName() + ": " + e.getMessage());
////            System.exit(0);
////        }
////        System.out.println("Opened database successfully");
////        db.createTb1();
////        db.createTb2();
////        db.createTb3();
////        db.createTb4();
////        db.createTb5();
////    }