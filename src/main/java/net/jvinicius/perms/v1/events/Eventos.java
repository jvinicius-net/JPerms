package net.jvinicius.perms.v1.events;

import java.sql.SQLException;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.jvinicius.perms.v1.main.main;
import net.jvinicius.perms.v1.sql.MySQLManager;

public class Eventos implements Listener {
	   @EventHandler
	    public void join(PlayerJoinEvent event) {
	        Player player = event.getPlayer();
	        try {
				MySQLManager.join(player);
		        main.setupPermissions(player);
			} catch (SQLException e) {
		        main.setupPermissions(player);
			}
	        if(player.hasPermission("teste.join")) {
	        	player.sendMessage("Teste 123");
	        }
	    }
	   @EventHandler
	    public void leave(PlayerQuitEvent event) {
	        Player player = event.getPlayer();
	        main.playerPermissions.remove(player.getName());
	    }

}
