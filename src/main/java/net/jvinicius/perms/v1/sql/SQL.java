package net.jvinicius.perms.v1.sql;

import java.sql.SQLException;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;

import net.jvinicius.perms.v1.main.main;
import org.bukkit.plugin.Plugin;





public class SQL extends Database{


	private static Connection connection;
	public SQL(Plugin plugin)
	{
		super(plugin);

		SQL.connection = null;
	}

	public Connection openConnection()
	{
		try
		{
			if (!main.plugin.getDataFolder().exists()) {
				main.plugin.getDataFolder().mkdir();
			}
			File file = new File(main.plugin.getDataFolder().getAbsolutePath()+"/users.db");

			if (!file.exists()){
				try {
					file.createNewFile();
				} catch (IOException e) {
					main.plugin.getLogger().log(Level.SEVERE, "Não foi possivel criar a db: users.db");
					main.plugin.getLogger().log(Level.SEVERE, "Desativando plugin!");
					main.plugin.getPluginLoader().disablePlugin(main.plugin);


				}
			}
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:" + file);
			connection.createStatement();


		}
		catch (SQLException e)
		{
			main.plugin.getLogger().log(Level.SEVERE, "Nao foi possivel conectar-se ao servidor SQL, motivo: " +
					e.getMessage());
			main.plugin.getPluginLoader().disablePlugin(main.plugin);

		}
		catch (ClassNotFoundException e)
		{
			main.plugin.getLogger().log(Level.SEVERE, "Driver JDBC nao encontrado!");
			main.plugin.getPluginLoader().disablePlugin(main.plugin);

		}
		return SQL.connection;

	}






	public boolean checkConnection()
	{
		return SQL.connection != null;
	}

	public Connection getConnection()
	{
		return SQL.connection;
	}



	public ResultSet querySQL(String query)
	{
		Connection c = null;
		if (checkConnection()) {
			c = getConnection();
		} else {
			c = openConnection();
		}
		Statement s = null;
		try
		{
			s = c.createStatement();
		}
		catch (SQLException e1)
		{
			e1.printStackTrace();
		}
		ResultSet ret = null;
		try
		{
			ret = s.executeQuery(query);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		closeConnection();

		return ret;
	}

	public void updateSQL(String update)
	{
		Connection c = null;
		if (checkConnection()) {
			c = getConnection();
		} else {
			c = openConnection();
		}
		Statement s = null;
		try
		{
			s = c.createStatement();
			s.executeUpdate(update);
		}
		catch (SQLException e1)
		{
			e1.printStackTrace();
		}
		closeConnection();
	}

	public void closeConnection()
	{
		if (SQL.connection != null) {
			try
			{


				SQL.connection.close();
				SQL.connection= null;
			}
			catch (SQLException  e)
			{
				this.plugin.getLogger().log(Level.SEVERE,
						"Error closing the MySQL Connection!");
				e.printStackTrace();
			}
		}
	}

}