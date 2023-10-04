package com.ferox.game.service; // dont forget to change packaging ^-^

import com.ferox.game.world.entity.mob.player.Player;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class FoxVote implements Runnable {

    public static final String HOST = "198.12.12.226";
    public static final String USER = "obsidia4_vote";
    public static final String PASS = "YaAllah1212!";
    public static final String DATABASE = "obsidia4_vote";

    private Player player;
    private Connection conn;
    private Statement stmt;

    public FoxVote(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        try {
            if (!connect(HOST, DATABASE, USER, PASS)) {
                return;
            }

            String name = player.getUsername().replace(" ", "_");
            ResultSet rs = executeQuery("SELECT * FROM fx_votes WHERE username='"+name+"' AND claimed=0 AND callback_date IS NOT NULL");

            while (rs.next()) {
                String timestamp = rs.getTimestamp("callback_date").toString();
                String ipAddress = rs.getString("ip_address");
                int siteId = rs.getInt("site_id");


                // -- ADD CODE HERE TO GIVE TOKENS OR WHATEVER
                rs.updateInt("claimed", 1); // do not delete otherwise they can reclaim!

                player.getInventory().add(4447, 1);
                player.getInventory().add(619, 5);


                System.out.println("[FoxVote] Vote claimed by "+name+". (sid: "+siteId+", ip: "+ipAddress+", time: "+timestamp+")");
                rs.updateRow();
            }

            destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public boolean connect(String host, String database, String user, String pass) {
        try {
            this.conn = DriverManager.getConnection("jdbc:mysql://"+host+":3306/"+database, user, pass);
            return true;
        } catch (SQLException e) {
            System.out.println("Failing connecting to database!");
            return false;
        }
    }

    public void destroy() {
        try {
            conn.close();
            conn = null;
            if (stmt != null) {
                stmt.close();
                stmt = null;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public int executeUpdate(String query) {
        try {
            this.stmt = this.conn.createStatement(1005, 1008);
            int results = stmt.executeUpdate(query);
            return results;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    public ResultSet executeQuery(String query) {
        try {
            this.stmt = this.conn.createStatement(1005, 1008);
            ResultSet results = stmt.executeQuery(query);
            return results;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
