create table user(
id int primary key auto_increment,
name varchar(20) not null,
password varchar(20) not null
);

create table passage(
id int primary key auto_increment,
author varchar(20) not null,
title varchar(100),
words varchar(100)
);

import javax.sql.*;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import java.sql.*;
import java.util.Scanner;

import com.mysql.jdbc.Driver;
/**
 * ClassName:Blog
 * Package:PACKAGE_NAME
 * Description:
 *
 * @Date:2020/2/22 8:31
 * @Author:DangWei
 */
public class Blog {
    private static String username;
    private static String userpassword;

    private DataSource getdatasource(){
        MysqlDataSource dataSource=new MysqlDataSource();
        dataSource.setURL("jdbc:mysql://127.0.0.1:3306/blog");
        dataSource.setUser("root");
        dataSource.setPassword("");
        dataSource.setUseSSL(false);
        dataSource.setCharacterEncoding("utf8");
        return dataSource;
    }

    public void register() throws ClassNotFoundException, SQLException {
        Scanner sc=new Scanner(System.in);
        System.out.println("请输入注册名字：");
        String name=sc.nextLine();
        System.out.println("请输入注册密码：");
        String password=sc.nextLine();
        Class.forName("com.mysql.jdbc.Driver");
        String url="jdbc:mysql://127.0.0.1:3306/blog?" +
                "useSSL=false&characterEncoding=utf8";
        String user="root";
        String password2="";
        try(Connection con=DriverManager.getConnection(url,user,password2)) {
            String sql="insert into user(name,password) values(?,?)";
            try(PreparedStatement statement=con.prepareStatement(sql)){
                statement.setString(1,name);
                statement.setString(2,password);
                statement.executeUpdate();
                System.out.println("注册成功");
            }
        }
        }

        public  void login() throws SQLException {
            Scanner sc=new Scanner(System.in);
            System.out.println("请输入登录名字：");
            String name=sc.nextLine();
            System.out.println("请输入登录密码：");
            String password=sc.nextLine();
            DataSource dataSource=getdatasource();
            try(Connection con=dataSource.getConnection()) {
                String sql="select name,password from user";
                try(PreparedStatement statement=con.prepareStatement(sql)){
                    try(ResultSet resultSet=statement.executeQuery()){
                        while(resultSet.next()){
                            String a=resultSet.getString(1);
                            String b=resultSet.getString(2);
                            if(name.equals(a)&&password.equals(b)){
                                System.out.println("登录成功");
                                username=name;
                                userpassword=password;
                                return;
                            }
                            if(name.equals(a)){
                                System.out.println("密码错误");
                                return;
                            }
                        }
                        System.out.println("没有此用户");
                    }
                }
            }
        }

        public void publish() throws SQLException {
           if(username==null||userpassword==null){
            System.out.println("请先登录");
            return;
           }
            Scanner sc=new Scanner(System.in);
            System.out.println("请输入标题：");
            String ti=sc.next();
            System.out.println("请输入内容：");
            String wo=sc.next();
            DataSource dataSource=getdatasource();
            try(Connection con=dataSource.getConnection()){
                String sql="insert into passage(author,title,words) values"+
                        "(?,?,?)";
                try(PreparedStatement statement=con.prepareStatement(sql)){
                    statement.setString(1,username);
                    statement.setString(2,ti);
                    statement.setString(3,wo);
                    statement.executeUpdate();
                    System.out.println("发表成功");
                }
            }
        }

    public void select() throws SQLException {
        if(username==null||userpassword==null){
            System.out.println("请先登录");
            return;
        }
        DataSource dataSource=getdatasource();
        try(Connection con=dataSource.getConnection()){
            String sql="select author,title,words from passage where"
                    +" author<=>?";
            try(PreparedStatement statement=con.prepareStatement(sql)){
                statement.setString(1,username);
                com.mysql.jdbc.PreparedStatement p=(com.mysql.jdbc.PreparedStatement)statement;
                System.out.println(p.asSql());
                try(ResultSet resultSet=statement.executeQuery()){
                    while(resultSet.next()){
                        String n=resultSet.getString(1);
                        String ti=resultSet.getString(2);
                        String wo=resultSet.getString(3);
                        System.out.println("author:"+n+"|||title:"+
                        ti+"|||words:"+wo);
                    }
                }
            }
        }
    }

    public void exit(){
        System.exit(0);
    }
    public void menu(){
        System.out.println("===========================================");
        System.out.println("1.注册用户");
        System.out.println("2.用户登录");
        System.out.println("3.发表文章");
        System.out.println("4.查询文章内容");
        System.out.println("0.退出");
        System.out.println("===========================================");
    }


}


public class Test {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Scanner sc=new Scanner(System.in);
        Blog blog=new Blog();
        while(true){
            blog.menu();
            System.out.println("请输入你的选项：");
            int select=sc.nextInt();
            switch(select){
                case 1:
                    blog.register();
                    break;
                case 2:
                    blog.login();
                    break;
                case 3:
                    blog.publish();
                    break;
                case 4:
                    blog.select();
                    break;
                case 0:
                    blog.exit();
                    break;
                default:
                    break;

            }
        }
    }
}

