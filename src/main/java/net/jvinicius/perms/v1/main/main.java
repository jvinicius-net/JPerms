package net.jvinicius.perms.v1.main;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import net.jvinicius.perms.v1.events.Eventos;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.java.JavaPlugin;

import net.jvinicius.perms.v1.commands.commandJPerms;
import net.jvinicius.perms.v1.events.*;
import net.jvinicius.perms.v1.sql.MySQLManager;

public class main extends JavaPlugin {
    public static main plugin;

    public static HashMap<String, PermissionAttachment> playerPermissions = new HashMap<>();

    public void onEnable() {
        plugin = this;
        plugin.getConfig().options().copyDefaults(true);
        plugin.saveConfig();
        try {MySQLManager.setMysql();} catch (SQLException e) {}
        plugin.getServer().getPluginManager().registerEvents(new Eventos(), this);
        plugin.getCommand("jperms").setExecutor(new commandJPerms());

    }
    
    public void onDisable() {
        playerPermissions.clear();
    }
    
    public static boolean checkGroup(String group) {
    	if(plugin.getConfig().getConfigurationSection("groups").contains(group)) {
    		return true;
    	}else {
    		return false;
    	}
    }
    
    public static void setupPermissions(Player player) {
        PermissionAttachment attachment = player.addAttachment(plugin);
        playerPermissions.put(player.getName(), attachment);
        try {permissionsSetter(player,MySQLManager.getGroupPlayer(player));} catch (SQLException e) {e.printStackTrace();}
    }

    private static void permissionsSetter(Player p,String group) {
        PermissionAttachment attachment = playerPermissions.get(p.getName());
        for (String permissions : plugin.getConfig().getStringList("groups." + group + ".permissions")) {
            attachment.setPermission(permissions, true);
        }
        
    }
    
    public static void listGroups(Player p) {
    	p.sendMessage("§fOs grupos do servidor são:");
    	p.sendMessage("");

        for (String groups : plugin.getConfig().getConfigurationSection("groups").getKeys(false)) {
        	p.sendMessage("§a"+groups);
        }
    }

    public static void listGroupPerms(Player p,String group) {
    	if(!checkGroup(group)) { p.sendMessage("§4O grupo "+group+" não existe!"); return;}
    	p.sendMessage("§fAs permissões do grupo §a"+group+" §fdo servidor são:");
    	p.sendMessage("");
	
        for (String permissions : plugin.getConfig().getStringList("groups." + group + ".permissions")) {
        	p.sendMessage("§a"+permissions);
        }

    }
    public static void checkPlayer(Player p) {
    	if(!MySQLManager.CheckPlayer(p)) { p.sendMessage("§4"+p.getName()+" não existe no banco de dados!"); return;}
       p.sendMessage("§fInformações do player §a"+p.getName()+":");
   	p.sendMessage("");

    	p.sendMessage("§aUUID: §f"+p.getUniqueId().toString());
    	p.sendMessage("§aUsuario: §f"+p.getName());
    	p.sendMessage("§aIP: §f"+p.getAddress());
    	
    	try {p.sendMessage("§aGrupo: §f"+MySQLManager.getGroupPlayer(p).toUpperCase());} catch (SQLException e) {}

    }
    
    public static boolean addPermToGroup(String group,String perm) {
    	List<String> permlist = plugin.getConfig().getStringList("groups." + group + ".permissions");
    		if(!permlist.contains(perm)) {
    			permlist.add(perm);
    			plugin.getConfig().set("groups." + group + ".permissions", permlist);
    			plugin.saveConfig();
    	        playerPermissions.clear();
    		    for(Player jogador : Bukkit.getOnlinePlayers()) {
    		    	setupPermissions(jogador);
    		    }
    		    return true;
    		}else {
    			return false;
    		}
    }
    
    public static boolean removePermToGroup(String group,String perm) {
    	List<String> permlist = plugin.getConfig().getStringList("groups." + group + ".permissions");
    		if(!permlist.contains(perm)) {
    			permlist.remove(perm);
    			plugin.getConfig().set("groups." + group + ".permissions", permlist);
    			plugin.saveConfig();
    	        playerPermissions.clear();
    		    for(Player jogador : Bukkit.getOnlinePlayers()) {
    		    	setupPermissions(jogador);
    		    }
    		    return true;

    		}else {
    			return false;
    		}
    }
    
    
    
}
