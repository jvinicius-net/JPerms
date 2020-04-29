package net.jvinicius.perms.v1.commands;

import java.sql.SQLException;

import net.jvinicius.perms.v1.main.main;
import net.jvinicius.perms.v1.sql.MySQLManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;



public class commandJPerms implements CommandExecutor {
	public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
		if (!(sender instanceof Player)){
			sender.sendMessage("§4Você precisa ser um player para usar este comando!");
			return true;
		}
		final Player p = (Player)sender;
		if (cmd.getName().equalsIgnoreCase("jperms")) {
			if (!p.hasPermission("jperms.command.list")) {
				p.sendMessage("§4Você não tem permissão!");
				return true;
			}
			if(args.length < 1){
				p.sendMessage("");
				p.sendMessage("§a/jperms groups §f- §aVeja os grupos do servidor.");
				p.sendMessage("§a/jperms permissions §7(grupo) §f- §aVeja as permissoes de determinado grupo.");
				p.sendMessage("§a/jperms check §7(player) §f- §aVeja as informações de determinado jogador.");
				p.sendMessage("");
				p.sendMessage("§a/jperms player §7(Player) §aset §7(Grupo) §f- §aSete o grupo de determinado player.");
				p.sendMessage("§a/jperms group §7(Grupo) §aadd §7(Perm) §f- §aAdiciona uma permiss�o a um grupo.");
				p.sendMessage("§a/jperms group §7(Grupo) §aremove §7(Perm) §f- §aRemove uma permiss�o a um grupo.");
				p.sendMessage("");
				p.sendMessage("§a/jperms reload §f- §aRecarrega a permissão de todos os players online.");
				p.sendMessage("");
				return true;
			}
			if(args[0].equalsIgnoreCase("group")) {
				if(args.length < 1) {p.sendMessage("§cVocê não indicou um grupo");return false;}
				if(args.length < 2) {p.sendMessage("§cVocê não indicou uma ação a ser feita");return false;}
				if(args.length < 4) {p.sendMessage("§cVocê não indicou uma permissão a ser adicionada ou removida");return false;}

				if(args[2].equalsIgnoreCase("add")) {

					if(main.addPermToGroup(args[1],args[3])) {
						p.sendMessage("§aPermissão '"+args[3]+ "' adicionada com sucesso!");return true;
					}else {
						p.sendMessage("§cPermissão '"+args[3]+ "' não foi adicionada com sucesso!");return false;

					}
				}else if(args[2].equalsIgnoreCase("remove")) {
					if(main.removePermToGroup(args[1],args[3])) {
						p.sendMessage("§aPermissão '"+args[3]+ "' removida com sucesso!");return true;
					}else {
						p.sendMessage("§cPermissão '"+args[3]+ "' não foi removida com sucesso!");return false;

					}
				}else {
					p.sendMessage("§a/jperms group §7(Grupo) §aadd §7(Perm) §f- §aAdiciona uma permissão a um grupo.");
					p.sendMessage("§a/jperms group §7(Grupo) §aremove §7(Perm) §f- §aRemove uma permissão a um grupo.");
				}

				return true;
			}else if(args[0].equalsIgnoreCase("groups")) {
				p.sendMessage("");
				main.listGroups(p);
				p.sendMessage("");
				return true;
			}else if(args[0].equalsIgnoreCase("permissions")) {
				if(args.length < 2) {p.sendMessage("§cVocê não indicou um grupo");return false;}
				p.sendMessage("");
				main.listGroupPerms(p, args[1]);
				p.sendMessage("");
				return true;
			}else if(args[0].equalsIgnoreCase("check")) {
				if(args.length < 2) {p.sendMessage("§cVocã não determinou um player");return false;}
				p.sendMessage("");
				Player p2 = Bukkit.getPlayer(args[1]);
				main.checkPlayer(p2);
				p.sendMessage("");
				return true;
			}else if(args[0].equalsIgnoreCase("reload")) {
				main.playerPermissions.clear();
				for(Player jogador : Bukkit.getOnlinePlayers()) {
					main.setupPermissions(jogador);
				}
				p.sendMessage("");
				p.sendMessage("§aPlugin recarregado com sucesso!");
				p.sendMessage("");
				return true;
			}else if(args[0].equalsIgnoreCase("player")) {
				if(args.length < 1) {p.sendMessage("§cVocê não determinou um player");return false;}
				if(args.length < 2) {p.sendMessage("§cVocê não indicou uma ação a ser feita");return false;}
				if(args.length < 4) {p.sendMessage("§cVocê não indicou um grupo");return false;}

				Player p2 = Bukkit.getPlayer(args[1]);
				if(!MySQLManager.CheckPlayer(p2)) { p.sendMessage("§4"+p2.getName()+" não existe no banco de dados!"); return false;}

				if(args[2].equalsIgnoreCase("set")) {
					if(!main.checkGroup(args[3])) {p.sendMessage("§cVocê indicou um grupo que não existe");return false;}

					if(MySQLManager.setGroup(p2, args[3])) {
						p.sendMessage("");
						p.sendMessage("§aGrupo de §f"+p2.getName()+" §adefinido para §f"+args[3]);
						p.sendMessage("");
						main.playerPermissions.remove(p2.getName());
						main.setupPermissions(p2);
					}else {
						p.sendMessage("§cFalha a definir o grupo de §f"+p2.getName());
					}


					return true;
				}else {
					p.sendMessage("§c/jperms player §7(Player) §cset §7(Grupo) §f- §cSete o grupo de determinado player.");
					return false;
				}
			}else {
				p.sendMessage("");
				p.sendMessage("§a/jperms groups §f- §aVeja os grupos do servidor.");
				p.sendMessage("§a/jperms permissions §7(grupo) §f- §aVeja as permissoes de determinado grupo.");
				p.sendMessage("§a/jperms check §7(player) §f- §aVeja as informações de determinado jogador.");
				p.sendMessage("");
				p.sendMessage("§a/jperms player §7(Player) §aset §7(Grupo) §f- §aSete o grupo de determinado player.");
				p.sendMessage("§a/jperms group §7(Grupo) §aadd §7(Perm) §f- §aAdiciona uma permiss�o a um grupo.");
				p.sendMessage("§a/jperms group §7(Grupo) §aremove §7(Perm) §f- §aRemove uma permiss�o a um grupo.");
				p.sendMessage("");
				p.sendMessage("§a/jperms reload §f- §aRecarrega a permissão de todos os players online.");
				p.sendMessage("");

				return false;
			}


		}
		return false;
	}
}
