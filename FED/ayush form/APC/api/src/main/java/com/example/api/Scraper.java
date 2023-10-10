package com.example.api;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.util.*;
import java.util.Scanner;
import java.util.List;

@RestController
@CrossOrigin
public class Scraper {


    String url = "jdbc:mysql://ayushcenter.xyz:402/ayush";
    String username = "Ayush";
    String password = "Ayuh1315!!";
    Connection connection = DriverManager.getConnection(url, username, password);
    Statement statement = connection.createStatement();


    @Autowired
    private JavaMailSender mailSender;

    public Scraper() throws SQLException {
    }

    public void sendEmail1 (String to, String text, String sub) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("ayush99kumar12@gmail.com", "PRICE COMPARE");
        helper.setTo(to);
        helper.setSubject(sub);
        helper.setText(text);
        mailSender.send(message);
    }


    @GetMapping("/add/{key1}/{key2}/{key3}")
    public   int  add (@PathVariable  String key1 , @PathVariable String key2,@PathVariable String key3) throws SQLException        {

        // Creating a sql connection  to store data

        String url = "jdbc:mysql://ayushcenter.xyz:402/ayush";
        String username = "Ayush";
        String password = "Ayuh1315!!";
        int  status = 0; // OK
        Connection connection = DriverManager.getConnection(url, username, password);
        Statement statement = connection.createStatement();
        //ResultSet resultSet = statement.executeQuery("SELECT * FROM USERS WHERE  Name="+key1);
        // ResultSet resultSet = statement.executeQuery("SELECT * FROM USERS WHERE Name=?");
        PreparedStatement  statement2 = connection.prepareStatement("SELECT * FROM USERS WHERE Name=?");
        statement2.setString(1,key1);
        ResultSet R1  = statement2.executeQuery();
        if(R1.next()){

            status=1; // USER ALREADY EXIST

        } else {


            try {
                String insertSQL = "INSERT INTO USERS (Name, Pass, Mail) VALUES (?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
                preparedStatement.setString(1, key1);
                preparedStatement.setString(2, key2);
                preparedStatement.setString(3, key3);
                String body="Hii Thanks For Registration on Price Compare for you reference  your User Name is : "+key1+" Pass_Key : "+key2;
                String sub="Thanks For Joining Us ";
                sendEmail1(key3,body,sub);
                int rowsAffected = preparedStatement.executeUpdate();
            } catch (SQLException err) {
                System.out.println(err);
                status = 2;  // SQL ERR OCCURRED
            } catch (MessagingException | UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    if (connection != null) {

                        connection.close();
                    }
                } catch (SQLException e) {
                    System.out.println(e);
                }

            }
        }

        return status;



    }


    @GetMapping("/login/{key1}/{key2}")
    public  int  login (HttpServletResponse response, @PathVariable String key1 , @PathVariable String key2) throws SQLException {
        String url = "jdbc:mysql://ayushcenter.xyz:402/ayush";
        String username = "Ayush";
        String password = "Ayuh1315!!";
        int ststus = 1;
        Connection connection = DriverManager.getConnection(url, username, password);
        Cookie cookie = new Cookie("username", "admin");
        cookie.setMaxAge(3600); // 1 hour
        response.addCookie(cookie);
        System.out.println("OOK");
        try {
            PreparedStatement  statement =  connection.prepareStatement("SELECT * FROM USERS WHERE Name=? AND  Pass=?");
            statement.setString(1,key1);
            statement.setString(2,key2);
            ResultSet resultSet = statement.executeQuery();
            System.out.println(resultSet.getFetchSize());
            if(resultSet.next()){
                ststus=0;
//                Cookie cookie = new Cookie("username", "admin");
//                cookie.setMaxAge(3600); // 1 hour
//                response.addCookie(cookie);




            }
        }catch (SQLException err){
            System.out.println(err);
        }finally {
            if(connection!=  null){
                try{
                    connection.close();
                }catch (SQLException a){
                    System.out.println(a);
                }}
        }
        return ststus;
    }



    @Scheduled(cron = "0 0 3 * * *")
    // @Scheduled(fixedRate = 10)
    public void runTask() throws SQLException, IOException, MessagingException, jakarta.mail.MessagingException {
        String url = "jdbc:mysql://ayushcenter.xyz:402/Ayush";
        String username = "Ayush";
        String password = "Ayuh1315!!";
        Connection connection = DriverManager.getConnection(url, username, password);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM check_data ");
        ArrayList<String> Links = new ArrayList<>();
        ArrayList<Integer> Prices = new ArrayList<>();
        ArrayList<String> mail = new ArrayList<>();
        ArrayList<Integer> p= new ArrayList<>();
        ArrayList<String> F = new ArrayList<>();
        int abc=0;
        String ActualPrice = "";

        try {
            while(resultSet.next()){
                //Retrieve by column name
                String Link  = resultSet.getString("Link");
                int Price = resultSet.getInt("Price");
                String Email = resultSet.getString("Email");
                Links.add(Link);
                Prices.add(Price);
                mail.add(Email);}
        }catch (SQLException s){
            System.out.println(s);
        }finally {
            if(connection != null){
                try {
                    connection.close();
                }catch (Exception ex){
                    System.out.println(ex);
                }

            }
        }

        WebClient client = new WebClient();
        client.getOptions().setJavaScriptEnabled(false);
        for (int i=0; i <Prices.size();i++){
            String Link = Links.get(i);
            HtmlPage page = client.getPage(Link);
            List<HtmlSpan> price = page.getByXPath("//span[@class='a-price-whole']");





            if(price.size()<1){
                List<HtmlDivision> price1 = page.getByXPath("//div[@class='_30jeq3 _16Jk6d']");
                for (HtmlDivision d:price1){
                    String p1 = d.getTextContent();
                    if(abc<1){
                        F.add(p1);
                    }
                    abc++;
                    String p2 = p1.replace("₹","");
                    int p3 = Integer.parseInt(p2.replace(",",""));
                    System.out.println(price1.size());
                    p.add(p3);
                    abc=0;

                }

            }
            System.out.println(price.size());


            if(price.size()>1){


                for (HtmlSpan s:price){
                    String p1 = s.getTextContent();
                    if(abc <1){
                        F.add(p1);}
                    String p2 = p1.replace(".","");
                    int a1 = Integer.parseInt(p2.replaceAll(",",""));
                    if(abc <1){
                        p.add(a1);

                        abc++;
                    }
                }
                abc=0;
            }

//            for (HtmlSpan s:price){
//                String p1 = s.getTextContent();
//                String p2 = p1.replace(".","");
//                int a1 = Integer.parseInt(p2.replaceAll(",",""));
//                p.add(a1);
//            }}




        }
        for (int i = 0; i <Prices.size();i++){

            if(p.get(i)>Prices.get(i)){
                try{
                    String TXT ="THE PRODUCT YOU GAVE US TO TRACK IS  " +
                            " IS TOO HIGH  JUST WAIT FOR  IT  YOU WONT REGRET IT  " +
                            " WITH LOVE FROM PRICE COMPARE   THE PRICE YOU GAVE US WAS  RS "+Prices.get(i) +" AND THE PRICE NOW IS RS "+F.get(i);
                    sendEmail1(mail.get(i),TXT,"Price Drop Alert ");

                    System.out.println("SENT");


                }catch (Exception a){
                    System.out.println(a);
                }
            }}
        Links = null;
        Prices=null;
        mail=null;
        p=null;
        F=null;
        abc=0;

















    }


    @Scheduled(cron = "0 0 1 * * *") // run at 1:00 AM every day
    public void runTask1() throws SQLException, MessagingException, IOException {
        System.out.println("Executing Updater");
        updater();
        //  code to be executed at 1AM
        System.out.println("Executing task at 1AM");
        mailsender();
    }



    @GetMapping("/search/amazon/{key}")
    @CrossOrigin
    public List<data> amazon(@PathVariable String key) throws IOException {
        WebClient client = new WebClient();

        // Disable JavaScript to speed up the scraping process
        client.getOptions().setJavaScriptEnabled(false);
        // System.out.println("Enter the product name to search");
        //Scanner sc = new Scanner(System.in);
        String prefix = "https://www.amazon.in/s?k=";
        String subfix =key;
        String url2=prefix+subfix;
        HtmlPage page = client.getPage(url2);
        List<HtmlSpan> productNames1 = page.getByXPath("//span[@class='a-size-medium a-color-base a-text-normal']");
        List<HtmlSpan> productPrice = page.getByXPath("//span[@class='a-price']");
        // List<HtmlAnchor> productLinks = page.getByXPath("//a[@class='a-link-normal s-underline-text s-underline-link-text s-link-style a-text-normal']")  ;
        List<HtmlAnchor> productLinks = page.getByXPath("//a[@class='a-link-normal s-underline-text s-underline-link-text s-link-style a-text-normal' or @class='a-link-normal a-text-normal' ]");
        // List<HtmlImage> product_Image = page.getByXPath("//span[@class='s-image']");
        List<HtmlImage> productImage = page.getByXPath("//img[@data-image-source-density='1']");

        System.out.println(productLinks.size());
        System.out.println(productNames1.size());
        ArrayList<String> a = new ArrayList<>();
        ArrayList<String> b = new ArrayList<>();
        ArrayList<String> c = new ArrayList<>();
        ArrayList<String> d = new ArrayList<>();
        List<data> data1 = new ArrayList<>();
        // System.out.println(productNames1.size());

//        for(HtmlDivision span: productNames1) {
//            String productName = span.getTextContent();
//            System.out.println("Product name : " + productName);
//        }

        for (HtmlImage image : productImage) {
            String image1 = image.getSrcAttribute();
            System.out.println(image1);
            d.add(image1);
        }

        for(HtmlSpan span: productNames1) {
            String productName1 = span.getTextContent();

            a.add(productName1+" ");
            // System.out.println("Product name : " + productName1);
        }

        for(HtmlAnchor a1 :productLinks){
            String links = a1.getHrefAttribute();
            c.add(links);

        }

        for (HtmlSpan span:productPrice){
            String price = span.getTextContent();

            String a1 =price;
            String b1 = "";

            int dividend = a1.length();
            int divisor = 2;
            int quotient = dividend / divisor;

            for (int i= 0; i <quotient;i++){
                b1+=a1.charAt(i);

            }
            // System.out.println(b);
            b.add(b1);
        }

//        for (HtmlAnchor anchor : productLinks) {
//            // Extract the text content
//            String textContent = anchor.getTextContent();
//            System.out.println("textContent : " + textContent);
//
//            // Extract the href link
//            String href = anchor.getHrefAttribute();
//
//            // Extract the actual link from the href
//            String[] parts = href.split("%3D1");
//            String actualLink = parts[0];
//            c.add(actualLink);
//            System.out.println("actualLink : " + actualLink);
//        }

        for (int i =0;i<10;i++){
//            System.out.print("Name :"+a.get(i)+" "+"Price "+b.get(i)+" Link To buy "+"https://www.amazon.in"+c.get(i));
//            System.out.println();
            String name = c.get(i);
            String a12 = "";
            //System.out.println(name);
            for (int j = 0;j<5;j++){
                a12+=name.charAt(j);
                //System.out.println(a12);


            }
            if(a12.equals("https")){
                data1.add(new data(a.get(i),b.get(i),c.get(i), d.get(i)));
                // System.out.println();

            }else {
                String c1 ="https://www.amazon.in"+c.get(i);
                System.out.print("Name :"+a.get(i)+" "+"Price "+b.get(i)+" Link To buy "+c1);
                data1.add(new data(a.get(i),b.get(i),c1,d.get(i)));
                System.out.println();
            }

        }











        return data1;
    }



    @GetMapping("/search/flipkart/{key}")
    @CrossOrigin

    public List<data> flipkart  (@PathVariable String key) throws IOException {
        WebClient client = new WebClient();

        // Disable JavaScript to speed up the scraping process
        client.getOptions().setJavaScriptEnabled(false);

        // Flipkart search query
        String prefix = "https://www.flipkart.com/search?q=";
        String subfix =key; // Key word entered by the user

        // Combining to result to get the data
        String url2=prefix+subfix;

        // Search the page on web
        HtmlPage page = client.getPage(url2);


        List<HtmlDivision> productNames1 = page.getByXPath("//div[@class='_4rR01T']");
        List<HtmlAnchor> productNames2 = page.getByXPath("//a[@class='s1Q9rs']");


        List<HtmlDivision> productPrice = page.getByXPath("//div[@class='_30jeq3 _1_WHN1' or @class='_30jeq3']");
        // List<HtmlAnchor> productLinks = page.getByXPath("//a[@class='a-link-normal s-underline-text s-underline-link-text s-link-style a-text-normal']")  ;
        List<HtmlAnchor> productLinks = page.getByXPath("//a[@class='_1fQZEK' or @class='s1Q9rs']");
        List<HtmlImage> p = page.getByXPath("//img[@class='_396cs4']");
        //System.out.println(productLinks.size());
        //System.out.println(productNames1.size());

        ArrayList<String> a = new ArrayList<>();
        ArrayList<String> b = new ArrayList<>();
        ArrayList<String> c = new ArrayList<>();
        ArrayList<String> d= new ArrayList<>();
        List<data> data1 = new ArrayList<>();

        for (HtmlImage img:p){
            String image = img.getSrcAttribute();
            d.add(image);
        }

        if(productNames1.size()>1){

            for(HtmlDivision div: productNames1) {
                String productName1 = div.getTextContent();

                a.add(productName1+" ");
                // System.out.println("Product name : " + productName1);
            }}else {

            for (HtmlAnchor a1:productNames2){

                String p2 = a1.getTextContent();
                a.add(p2);
            }


        }

        for (HtmlDivision div:productPrice){
            String price = div.getTextContent();
            b.add(price);
        }

        for (HtmlAnchor an :productLinks){

            String link = an.getHrefAttribute();

            c.add("https://www.flipkart.com"+link);

        }

        for (int i = 0; i <10;i++){
            data1.add(new data(a.get(i),b.get(i),c.get(i),d.get(i)));
        }
        return data1;

//        List<HtmlDivision> productNames1 = page.getByXPath("//div[@class='_4rR01T']");  // keyword used to identify the name of the product
//
//
//        List<HtmlDivision> productPrice = page.getByXPath("//div[@class='_30jeq3 _1_WHN1']"); // keyword used to identify the price  of the product
//
//
//        ArrayList<String> a = new ArrayList<>();
//        ArrayList<String> b = new ArrayList<>();
//        ArrayList<String> c= new ArrayList<>();
//        List<data> data1 = new ArrayList<>();
//        for(HtmlDivision div: productNames1) {
//            String productName1 = div.getTextContent();
//
//            a.add(productName1+" ");
//            // System.out.println("Product name : " + productName1);
//        }
//
//        for (HtmlDivision div:productPrice){
//            String price = div.getTextContent();
//            b.add(price);
//        }
//
//        for (int i =0;i<5;i++){
//            c.add("Name :"+a.get(i)+" "+"Price "+b.get(i));
//        }
//
//        for (int j = 0;j<5;j++){
//            String a1 = a.get(j);
//            String prefix1 = a.get(j);
//            String c1 = prefix1.replaceAll(" ","");
//            String a3 = "https://www.flipkart.com/search?q="+c1;
//
//            String b1 = b.get(j);
//
//            data1.add(new data(a1,b1,a3));
//
//        }
//
//        System.out.println();
//
//
//        System.out.println("Found !!!!   ");
//        System.out.println(" Filtering the details ");
//        System.out.println(" Suspicious sound ");
//        System.out.println("Got It");
//        return data1;







    }



    @CrossOrigin
    @PostMapping("/login")
    public int addtocart(@RequestBody Cart req) throws SQLException, IOException {
        // Your login logic here, using the username and password from the request

        String Name= req.getName();
        int Price =req.getPrice();
        String Link=req.getLink();
        String Username= req.getUsername();
        String Vendor = req.getVendor();
        int status =0;
        int pointer = 0;
        WebClient client = new WebClient();
        client.getOptions().setJavaScriptEnabled(false);
        HtmlPage page = client.getPage(Link);

        if(Objects.equals(Vendor, "Amazon")){
            List<HtmlSpan> price = page.getByXPath("//span[@class='a-price-whole']");
            for (HtmlSpan s:price){
                String p1 = s.getTextContent();
                String p2 = p1.replace(".","");
                pointer= Integer.parseInt(p2.replace(",",""));
                break;

            }



        }

        if(Objects.equals(Vendor,"Flipkart")){
            List<HtmlDivision> price1 = page.getByXPath("//div[@class='_30jeq3 _16Jk6d']");

            for (HtmlDivision f:price1){
                String p1 = f.getTextContent();
                String p2 = p1.replace("₹","");
                pointer= Integer.parseInt(p2.replace(",",""));
                System.out.println(pointer);
            }


        }








        try{
            String url = "jdbc:mysql://ayushcenter.xyz:402/ayush";
            String username = "Ayush";
            String password = "Ayuh1315!!";
            Connection connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();



            String insertSQL = "INSERT INTO CART (Name, Price,Cprice, Link,Uname,Vendor) VALUES (?, ?, ?,?,?,?)";
            PreparedStatement preparedStatement =connection.prepareStatement(insertSQL);
            preparedStatement.setString(1,Name);
            preparedStatement.setInt(2,Price);
            preparedStatement.setInt(3,pointer);
            preparedStatement.setString(4,Link);
            preparedStatement.setString(5,Username);
            preparedStatement.setString(6,Vendor);
            int row=preparedStatement.executeUpdate();


        }catch (SQLException e){
            status = 1;
        }finally {
            if (connection!= null){
                try {
                    connection.close();
                }catch (SQLException a){
                    status =1;
                }
            }
        }






        return status;


    }


    @GetMapping("/get/{key1}")
    public List<cart_data> get_cart_details(@PathVariable String key1) throws SQLException {

        //ResultSet resultSet = statement.executeQuery("select  *  from  CART  where  Uname = ?");
        String url = "jdbc:mysql://ayushcenter.xyz:402/ayush";
        String username = "Ayush";
        String password = "Ayuh1315!!";
        Connection connection = DriverManager.getConnection(url, username, password);
        Statement statement = connection.createStatement();

        PreparedStatement  statement1 = connection.prepareStatement("SELECT * FROM CART WHERE BINARY Uname = ?");
        statement1.setString(1,key1);
        ResultSet resultSet =statement1.executeQuery();
        ArrayList<String> Name1 = new ArrayList<>();
        ArrayList<Integer>Price1 = new ArrayList<>();
        ArrayList<Integer> CPrice =new ArrayList<>();
        ArrayList<String> Link_TO_Buy = new ArrayList<>();
        List<cart_data> data = new ArrayList<>();

        while (resultSet.next()){
            String  Name = resultSet.getString("Name");
            Name1.add(Name);

            int Price = resultSet.getInt("Price");
            Price1.add(Price);


            int Current_price = resultSet.getInt("Cprice");
            CPrice.add(Current_price);

            String Link = resultSet.getString("Link");
            Link_TO_Buy.add(Link);

        }

        for (int i =0;i<Name1.size();i++){

            data.add(new cart_data(Name1.get(i),Price1.get(i),CPrice.get(i),Link_TO_Buy.get(i)));
        }












        return data;
    }


    public static class Cart{

        private  String name;
        private int price;

        private String vendor;

        public String getVendor() {
            return vendor;
        }

        public void setVendor(String vendor) {
            this.vendor = vendor;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        private  String link;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        private String username;



    }


    public void mailsender() throws SQLException, MessagingException, UnsupportedEncodingException {

        String url = "jdbc:mysql://ayushcenter.xyz:402/ayush";
        String username = "Ayush";
        String password = "Ayuh1315!!";
        Connection connection = DriverManager.getConnection(url, username, password);
        Statement statement = connection.createStatement();

        String sql = "select  *  from  CART";
        Statement st = connection.createStatement();
        ResultSet resultSet = st.executeQuery(sql);

        int pointer = 0;
        String pro ="";
        String UNAME="";
        String sub ="Price Drop Alert";



        String Mail1 ="";

        while (resultSet.next()){

            int price = resultSet.getInt("Price");

            int current_price=resultSet.getInt("Cprice");

            String key1 = resultSet.getString("Uname");
            UNAME=key1;

            String Product_Name= resultSet.getString("Name");




            if(price>=current_price){
                pointer=price;
                PreparedStatement  statement1 = connection.prepareStatement("SELECT * FROM USERS WHERE BINARY Name = ?");
                statement1.setString(1,key1);
                ResultSet r1 = statement1.executeQuery();

                while (r1.next()){

                    Mail1= r1.getString("Mail");
                }
                String text = " Hii "+key1+" Your Product Named "+Product_Name+" is at your desired price which is :"+price+"  if you are interested  kindly checkout";
                sendEmail1(Mail1,text,sub);
            }
        }



    }


    public void   updater() throws SQLException, IOException {

        // Amazon Updater

        int pointer1 = 0;
        int pointer2 = 0;
        int pointer=0;
        String url = "jdbc:mysql://ayushcenter.xyz:402/ayush";
        String username = "Ayush";
        String password = "Ayuh1315!!";
        Connection connection = DriverManager.getConnection(url, username, password);
        Statement statement = connection.createStatement();

        String sql = "select  *  from  CART";
        Statement st = connection.createStatement();
        ResultSet resultSet = st.executeQuery(sql);

        ArrayList<String> LINK = new ArrayList<>();
        ArrayList<Integer> PRICE = new ArrayList<>();
        ArrayList<Integer> ID = new ArrayList<>();
        ArrayList<String> VENDOR = new ArrayList<>();

        WebClient client = new WebClient();
        client.getOptions().setJavaScriptEnabled(false);

        while (resultSet.next()){

            String link = resultSet.getString("Link");
            LINK.add(link);

            int price = resultSet.getInt("Cprice");
            PRICE.add(price);

            int id = resultSet.getInt("ID");
            ID.add(id);

            String vendor = resultSet.getString("Vendor");
            VENDOR.add(vendor);

        }

        try {

            for (int i = 0; i < LINK.size(); i++) {
                HtmlPage page = client.getPage(LINK.get(i));
                if (Objects.equals(VENDOR.get(i), "Amazon")) {       // UPDATING THE AMAZON PRICE


                    List<HtmlSpan> price = page.getByXPath("//span[@class='a-price-whole']");
                    for (HtmlSpan s : price) {
                        String p1 = s.getTextContent();
                        String p2 = p1.replace(".", "");
                        pointer = Integer.parseInt(p2.replace(",", ""));
                        break;

                    }


                    PreparedStatement preparedStatement = connection.prepareStatement(" update CART  set Cprice = ? where id = ?");
                    preparedStatement.setInt(1, pointer);
                    preparedStatement.setInt(2, ID.get(i));
                    int row = preparedStatement.executeUpdate();


                } else {                   // UPDATING THE FLIPKART PRICE
                    List<HtmlDivision> price1 = page.getByXPath("//div[@class='_30jeq3 _16Jk6d']");
                    for (HtmlDivision f : price1) {
                        String p1 = f.getTextContent();
                        String p2 = p1.replace("₹", "");
                        pointer = Integer.parseInt(p2.replace(",", ""));
                        System.out.println(pointer);
                    }

                    PreparedStatement preparedStatement = connection.prepareStatement("update CART  set Cprice = ? where id = ?");
                    preparedStatement.setInt(1, pointer);
                    preparedStatement.setInt(2, ID.get(i));
                    int row = preparedStatement.executeUpdate();


                }


            }
        }catch (SQLException a){
            System.out.println("ERR");
        }
        finally {
            try {
                if(connection!=null){
                    connection.close();
                    LINK=null;
                    PRICE=null;
                    ID=null;
                    VENDOR=null;
                }
            }catch (SQLException b){
                System.out.println("ERR CLOSING CONNECTION");
            }
        }











    }


}

