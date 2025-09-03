package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class sqlserver_connect {
    public static void main(String[] args) {
        String cS="jdbc:sqlserver://JARVIS/SQLEXPRESS;LibraryManagementSystem;IntergratedSequrity=true";
        try {
            try (Connection connection= DriverManager.getConnection(cS)){
                System.out.printf("ok");
            }
        }catch (SQLException e){
            System.out.println("erroor");
        }
    }
}
