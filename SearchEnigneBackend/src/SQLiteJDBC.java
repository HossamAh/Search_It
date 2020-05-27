import java.sql.*;
import java.util.*;

public class SQLiteJDBC {

    //these are the column names
    public static final String COL_pageID = "ID";
    public static final String COL_word = "word";
    public static final String COL_count = "count";

    public static final String COL_pageID_links = "ID";
    public static final String COL_links = "links";

    public static final String COL_ID = "ID";
    public static final String COL_title = "title";
    public static final String COL_URL = "URL";
    public static final String COL_pop = "popularity";

    public static final String COL_single_word = "single_word";
    public static final String COL_docs_count = "docs_count";

    public static final String COL_Image_ID = "ImageID";
    public static final String COL_Caption = "Caption";
    public static final String COL_ABSurl = "ABSurl";

    public static final String COL_pageID_image = "pageID";
    public static final String COL_ImageID = "ImageID";

    private static final String DATABASE_NAME = "db_indexer";
    private static final String TABLE_NAME3 = "tb3_words";
    private static final String TABLE_NAME2 = "tb2_links";
    private static final String TABLE_NAME1 = "tb1_info";
    private static final String TABLE_NAME4 = "tb4_word_count";
    private static final String TABLE_NAME5 = "tb5_imageInfo";
    private static final String TABLE_NAME6 = "tb6_img_pg";
    private static final int DATABASE_VERSION = 1;

    public Connection open() throws ClassNotFoundException {
        Connection c = null;
        Class.forName("org.sqlite.JDBC");
        try {
            c = DriverManager.getConnection("jdbc:sqlite:" + DATABASE_NAME + ".db");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        System.out.println("Opened database successfully");
        return c;
    }

    public void createTb1() {
        Connection c = null;
        Statement stmt = null;
        try {
            c = this.open();
            stmt = c.createStatement();
            String sql = "CREATE TABLE " + TABLE_NAME1 + " ( " +
                    COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_title + " TEXT, " +
                    COL_pop + " REAL, " +
                    COL_URL + " TEXT );";
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public void createTb2() {
        Connection c = null;
        Statement stmt = null;
        try {
            c = this.open();
            stmt = c.createStatement();
            String sql = "CREATE TABLE " + TABLE_NAME2 + " ( " +
                    COL_pageID + " INTEGER , " +
                    COL_word + " TEXT, " +
                    COL_count + " INTEGER," +
                    "PRIMARY KEY (" + COL_pageID + "," + COL_word + ")" + ");";
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public void createTb3() {
        Connection c = null;
        Statement stmt = null;
        try {
            c = this.open();
            stmt = c.createStatement();
            String sql = "CREATE TABLE " + TABLE_NAME3 + " ( " +
                    COL_pageID_links + " INTEGER , " +
                    COL_links + " TEXT, " +
                    "PRIMARY KEY( " + COL_pageID_links + "," + COL_links + ")" + ");";
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public void createTb4() {
        Connection c = null;
        Statement stmt = null;
        try {
            c = this.open();
            stmt = c.createStatement();
            String sql = "CREATE TABLE " + TABLE_NAME4 + " ( " +
                    COL_single_word + " TEXT, " +
                    COL_docs_count + " INTEGER," +
                    "PRIMARY KEY (" + COL_single_word + ")" + ");";
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    

    public void createTb5() {
        Connection c = null;
        Statement stmt = null;
        try {
            c = this.open();
            stmt = c.createStatement();
            String sql = "CREATE TABLE " + TABLE_NAME5 + " ( " +
                    COL_pageID_image + " INTEGER, " +
                    COL_Caption + " TEXT," +
                    COL_ABSurl + " TEXT, " + " PRIMARY KEY (" + COL_pageID_image + "," + COL_ABSurl + ")" +");";
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

//    public void createTb6() {
//        Connection c = null;
//        Statement stmt = null;
//        try {
//            c = this.open();
//            stmt = c.createStatement();
//            String sql = "CREATE TABLE " + TABLE_NAME6 + " ( " +
//                    COL_pageID_image + " INTEGER, " +
//                    COL_ImageID + " INTEGER," +
//                    "PRIMARY KEY(" + COL_pageID_image + "," + COL_ImageID + ")" + ");";
//            stmt.executeUpdate(sql);
//            stmt.close();
//            c.close();
//        } catch (Exception e) {
//            System.err.println(e.getClass().getName() + ": " + e.getMessage());
//            System.exit(0);
//        }
//    }

    ///////////////when adding a page to the DB.
    public void createPage(Page p) {
        Connection c = null;
        Statement stmt = null;

        try {
            c = this.open();
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            String sql = "INSERT INTO "+ TABLE_NAME1 +" ("+ COL_title+","+ COL_URL+") " +
                    "VALUES ("+p.gettitle() +", "+p.getURL()+" );";
            stmt.executeUpdate(sql);
            ResultSet rs = stmt.executeQuery( "SELECT "+COL_ID+ "FROM "+ TABLE_NAME1+" where "+COL_URL+" = "+p.getURL()+";" );
            int id = rs.getInt(COL_ID);
            Hashtable<String,Integer> s = new Hashtable<String,Integer>();
            s = p.getwords();
            Set<String> keys = s.keySet();
            for(String key: keys){
                //TODO: call the function that adds every word to  TABLE4
                stmt.executeQuery( "INSERT INTO "+TABLE_NAME2+ " ("+ COL_pageID+","+ COL_word+","+COL_count+") " +
                        "VALUES ("+id +", "+key+s.get(key)+" );");
                addWordsCount(key);
            }

            ArrayList<String> linkat = new ArrayList <String>();
            linkat = p.getLinks();

            for (int i=0; i<linkat.size();i++){
                stmt.executeQuery( "INSERT INTO "+TABLE_NAME3+ " ("+ COL_pageID_links+","+ COL_links+") " +
                        "VALUES ("+id +", "+linkat.get(i)+" );");
            }
            Set<Crawler.image> images = new HashSet<Crawler.image>();
            Iterator<Crawler.image> itr = images.iterator();
            images = p.getImages();
            while(itr.hasNext()){
                stmt.executeQuery( "INSERT INTO "+TABLE_NAME5+ " ("+COL_pageID_image+","+ COL_Caption+","+ COL_ABSurl+") " +
                        "VALUES ("+id+", "+itr.next().imageCaption +", "+itr.next().imageCaption+" );");
            }
            stmt.close();
            c.commit();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Records created successfully");
    }
///////////////retureive page iD by url.
    public int retrievePageID(String url) {
        Connection c = null;
        Statement stmt = null;
        int id = 0;
        try {
            c = this.open();
            ResultSet rs = stmt.executeQuery( "SELECT "+COL_ID+ "FROM "+ TABLE_NAME1+" where "+COL_URL+" = "+url+";" );
            id = rs.getInt(COL_ID);
            rs.close();
            stmt.close();
            c.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return id;
    }

///////////////add the number of docs containing a word.
    public void addWordsCount(String word) {
        Connection c = null;
        Statement stmt = null;
        try {
            c = this.open();

            stmt = c.createStatement();

            ResultSet rs = stmt.executeQuery( "SELECT * FROM "+TABLE_NAME4+"where "+COL_single_word+" = "+word+";" );

            if(rs.first()){
                int count = rs.getInt(COL_docs_count);
                count = count+1;
                String sql = "UPDATE "+TABLE_NAME4 +"set "+COL_docs_count +"= "+count +"+where "+COL_single_word+" = "+word+";";
                stmt.executeUpdate(sql);
                c.commit();
            }
            else{
                stmt.executeQuery( "INSERT INTO "+TABLE_NAME4+ " ("+COL_single_word+","+ COL_docs_count+") " +
                        "VALUES ("+word+", "+ 1 +" );");
            }
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Operation done successfully");
    }

    ////////////////retrieve page by url.
    public Page retrievePage(String url) {
        Connection c = null;
        Statement stmt = null;
        Page p = null;
        try {
            c = this.open();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM "+ TABLE_NAME1+" where "+COL_URL+" = "+url+";" );
            p =new Page(rs.getString(COL_URL),rs.getString(COL_title));

            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return p;
    }

    ////////////////retrieve page by ID.
    public Page retrievePageByID(int id) {
        Connection c = null;
        Statement stmt = null;
        Page p = null;
        try {
            c = this.open();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM "+ TABLE_NAME1+" where "+COL_ID+" = "+id+";" );
            p =new Page(rs.getString(COL_URL),rs.getString(COL_title));

            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return p;
    }

    ////////////////return the images of certain page.
    public Set<Crawler.image>  retriveImages(String url) {
        int id = retrievePageID(url);
        Connection c = null;
        Statement stmt = null;
        Set<Crawler.image> images = new HashSet<Crawler.image>();
        try {
            c = this.open();
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM "+TABLE_NAME5+"where "+COL_pageID_image+" = "+id+";" );
            while ( rs.next() ) {
                Crawler.image img = new Crawler.image(rs.getString(COL_ABSurl), rs.getString(COL_Caption));
                images.add(img);
            }
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        return images;
    }
    ///////retrieve word count in documents.
    public int retrieveWordsCount(String word) {
        Connection c = null;
        Statement stmt = null;
        int count=-1;
        try {
            c = this.open();
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM "+TABLE_NAME4+"where "+COL_single_word+" = "+word+";" );
            count = rs.getInt(COL_docs_count);
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        return count;
    }

    //TODO:set popularity of a page.

    public Hashtable<String,Integer> retrieveAllWords(String url) {
        int id = retrievePageID(url);
        Hashtable<String,Integer> words = new Hashtable<String,Integer>();
        Connection c = null;
        Statement stmt = null;
        try {
            c = this.open();
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM "+TABLE_NAME3+"where "+COL_pageID+" = "+id+";" );
            while ( rs.next() ) {
                words.put(rs.getString(COL_word),rs.getInt(COL_count));
            }
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        return words;
    }

    public ArrayList<String> retrieveAllLinks(String url) {
        int id = retrievePageID(url);

        ArrayList<String> linkat = new ArrayList <String>();
        Connection c = null;
        Statement stmt = null;
        try {
            c = this.open();
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM "+TABLE_NAME2+"where "+COL_pageID+" = "+id+";" );
            while ( rs.next() ) {
                linkat.add(rs.getString(COL_links));
            }
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        return linkat;
    }

    /////////////retrieve pages containing a specific word.
    public List<Page> retrievePages(String word) {
        List<Page> pages = new ArrayList<Page>();
        Connection c = null;
        Statement stmt = null;
        try {
            c = this.open();
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM "+TABLE_NAME3+"where "+COL_word+" = "+word+";" );
            while ( rs.next() ) {
                pages.add(retrievePageByID(rs.getInt(COL_pageID)));
            }
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        return pages;
    }

    /////////////retrieve pages containing a specific set of words.
    public List<Page> retrievePagesSetWords(String words) {
        List<Page> pages = new ArrayList<Page>();
        List<Page> retrievedPages;
        boolean found = false;
        String[] arrOfStr = words.split(" ");
        for (String a : arrOfStr){
            retrievedPages = retrievePages(a);
             for (int i = 0; i < retrievedPages.size(); i++) {
                for (int j = 0; i < pages.size(); j++) {
                    if(pages.get(j).getURL().equals(pages.get(i).getURL())){
                        found = true;
                    }
                }
                if(!found){
                    retrievedPages.get(i).setImages(retriveImages(retrievedPages.get(i).getURL()));
                    retrievedPages.get(i).setLinks(retrieveAllLinks(retrievedPages.get(i).getURL()));
                    retrievedPages.get(i).setwords(retrieveAllWords(retrievedPages.get(i).getURL()));
                    pages.add(retrievedPages.get(i));
                }
            }
        }
        return pages;
    }

    public void createDB(){
        Connection c = null;
        SQLiteJDBC db = new SQLiteJDBC();
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:" + DATABASE_NAME + ".db");

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully");
        db.createTb1();
        db.createTb2();
        db.createTb3();
        db.createTb4();
        db.createTb5();
    }

    public static void main(String[] arg) {
        Connection c = null;
        SQLiteJDBC db = new SQLiteJDBC();
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:" + DATABASE_NAME + ".db");

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully");
        db.createTb1();
        db.createTb2();
        db.createTb3();
        db.createTb4();
        db.createTb5();
    }
}
