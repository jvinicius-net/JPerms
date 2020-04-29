package net.jvinicius.perms.v1.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

import org.bukkit.entity.Player;

import net.jvinicius.perms.v1.main.main;

public class MySQLManager{
	
	  private static SQL db;
	  public static main plugin;
	  
	  public MySQLManager(main plugin2){
		  this.plugin = plugin2;
	  }
	  
	  public static void setMysql()
	    throws SQLException
	  {



		  db = new SQL(plugin);
		  db.openConnection();
	      Statement statement = db.getConnection().createStatement();
	      statement.executeUpdate("CREATE TABLE IF NOT EXISTS `jperms_accounts` (`username` varchar(32), `group` varchar(32))");
	   db.closeConnection();
	      
		  }

	  
	  public void closeDB()
	  {
	    
	  }
	  
	  public static void join(Player p)
			    throws SQLException
			  {
				  
			    String username = p.getName();
			    if (!db.checkConnection()) {
			      db.openConnection();
			    }
			    Statement s = db.getConnection().createStatement();
			    
			    ResultSet rs =  s.executeQuery("SELECT * FROM jperms_accounts WHERE `username`='" + 
			    		username + "';");
			    if (rs.next()) {
				  	  db.closeConnection();	
			      return;
			    }else {
				    s.executeUpdate("INSERT INTO jperms_accounts (`username`, `group`) VALUES ('" + 
				    		username + "', '"+main.plugin.getConfig().getString("server.default-group")+"');");
				  	    main.plugin.getLogger().log(Level.INFO,  "Os dados do jogador " + p.getName() + " foram inseridos com sucesso na database !");
			    }

			    
			  	  db.closeConnection();	

			 

			  }
			  
	  public static Boolean CheckPlayer(Player p)			  {
		   Boolean status = false;
			    String username = p.getName();
			    if (!db.checkConnection()) {
			      db.openConnection();
			    }
				try {
			    Statement s = db.getConnection().createStatement();
		
			    ResultSet rs;
			
					rs = s.executeQuery("SELECT * FROM jperms_accounts WHERE `username`='" + 
							username + "';");
			
			    if (rs.next()) {
				  	  db.closeConnection();	
				  	status= true;
			    }else {
				  	  db.closeConnection();	
				  	status = false;
			    }

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			 

				return status;
			  }
			  
	  public static String getGroupPlayer(Player p)
	    throws SQLException
	  {

	    String name = p.getName();
	    if (!db.checkConnection()) {
		      db.openConnection();
	    }
	    
	    Statement s = db.getConnection().createStatement();
	    ResultSet rs = s.executeQuery("SELECT * FROM jperms_accounts WHERE `username`='" + 
	      name + "';");
	    String retorno = "";
	    if (!rs.next()) {
	    	db.closeConnection();
	      return "default";
	    }else {
	    	 retorno = rs.getString("group");

	    	 db.closeConnection();
	    }
	     
	   
	   
	    return retorno;
	  }
	
	  public static boolean setGroup(Player p, String group)
		 {

		   String name = p.getName();
		   if (!db.checkConnection()) {
		     db.openConnection();
		   }
		   Statement s;
		try {
			s = db.getConnection().createStatement();

		     s.executeUpdate("UPDATE jperms_accounts SET `group`='" + group + "' WHERE `username`='" + name + "';");
				return true;    

		} catch (SQLException e) {
			return false;    

		}

		 }
}