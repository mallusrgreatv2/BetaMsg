package tk.discopika.betamsg;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class BetaMsg extends JavaPlugin {
    private final HashMap<String, String> replyMap = new HashMap<>();
    @Override
    public void onEnable() {
        System.out.println("[BetaMsg] Loaded");
    }
    @Override
    public void onDisable() {
        replyMap.clear();
        System.out.println("[BetaMsg] Disabled");
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Console can't use this.");
            return true;
        }

        Player player = (Player) sender;

        if (label.equalsIgnoreCase("msg")) {
            if (args.length < 2) {
                player.sendMessage(ChatColor.RED + "Usage: /msg <player> <message>");
                return true;
            }

            Player target = Bukkit.getPlayer(args[0]);

            if (target == null || !target.isOnline()) {
                player.sendMessage(ChatColor.RED + "That player is not online.");
                return true;
            }

            String message = joinArgs(args, 1);
            String msgToSend = ChatColor.GRAY + "[" + ChatColor.WHITE + player.getName() + ChatColor.GRAY + " -> " + ChatColor.WHITE + target.getName() + ChatColor.GRAY + "] " + message;
            player.sendMessage(msgToSend);
            target.sendMessage(msgToSend);
            replyMap.put(target.getName(), player.getName());
            replyMap.put(player.getName(), player.getName());
            return true;
        }

        if (label.equalsIgnoreCase("r")) {
            if (args.length < 1) {
                player.sendMessage(ChatColor.RED + "Usage: /r <message>");
                return true;
            }
            String lastMessager = replyMap.get(player.getName());
            if (lastMessager == null) {
                player.sendMessage(ChatColor.RED + "No one has messaged you yet.");
                return true;
            }

            Player target = Bukkit.getPlayer(lastMessager);
            if (target == null || !target.isOnline()) {
                player.sendMessage(ChatColor.RED + "That player is not online anymore.");
                return true;
            }

            String message = joinArgs(args, 0);
            String msgToSend = ChatColor.GRAY + "[" + ChatColor.WHITE + player.getName() + ChatColor.GRAY + " -> " + ChatColor.WHITE + target.getName() + ChatColor.GRAY + "] " + message;
            player.sendMessage(msgToSend);
            target.sendMessage(msgToSend);

            replyMap.put(target.getName(), player.getName());
            return true;
        }

        return false;
    }

    private String joinArgs(String[] args, int start) {
        StringBuilder sb = new StringBuilder();
        for (int i = start; i < args.length; i++) {
            sb.append(args[i]).append(" ");
        }
        return sb.toString().trim();
    }
}
