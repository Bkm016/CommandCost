package me.skymc.commandcost;

import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * ����
 * 
 * @author sky
 * @since 2018��2��3�� ����7:41:29
 */
public class CommandCost extends JavaPlugin implements Listener {
	
	private PlayerPointsAPI pointsAPI;
	
	@Override
	public void onLoad() {
		saveDefaultConfig();
	}
	
	@Override
	public void onEnable() {
		pointsAPI = ((PlayerPoints) Bukkit.getPluginManager().getPlugin("PlayerPoints")).getAPI();
		// ������
		Bukkit.getPluginManager().registerEvents(this, this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		reloadConfig();
		sender.sendMessage("reload ok!");
		return true;
	}

	@EventHandler
	public void command(PlayerCommandPreprocessEvent e) {
		for (String command : getConfig().getConfigurationSection("commands").getKeys(false)) {
			// ָ����ͬ
			if (e.getMessage().toLowerCase().startsWith(command)) {
				// ��������
				if (e.getMessage().split(" ").length >= getConfig().getInt("commands." + command + ".length")) {
					// ��ȯ����
					if (!pointsAPI.take(e.getPlayer().getUniqueId(), getConfig().getInt("commands." + command + ".points"))) {
						e.setCancelled(true);
						e.getPlayer().sendMessage(getConfig().getString("commands." + command + ".message").replace("&", "��"));
					}
				}
				return;
			}
		}
	}
}
