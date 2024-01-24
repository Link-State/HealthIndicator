package main;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class CreateIndicator implements Listener {
	
	@EventHandler
	public void interactToEntity(PlayerInteractEntityEvent e) {
		
	}
	
	@EventHandler
	public void damagedByProjectile(ProjectileHitEvent e) {
		
	}
	
	@EventHandler
	public void damagedByEntity(EntityDamageByEntityEvent e) {
		if (!(e.getDamager() instanceof Player)) {
			return;
		}
		if (!(e.getEntity() instanceof LivingEntity)) {
			return;
		}

		Player show = (Player) e.getDamager();
		LivingEntity target = (LivingEntity) e.getEntity();

		String text = getContext(target, e.getDamage());
		TextComponent context = new TextComponent(text);
		
		show.spigot().sendMessage(ChatMessageType.ACTION_BAR, context);
	}
	
	private String getContext(LivingEntity target, double damage) {
		String default_color = ChatColor.WHITE + "" + ChatColor.BOLD;
		String name = default_color + target.getName();
		
		double maxHealth = target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
		double health = target.getHealth() - damage;
		health = health <= 0.0 ? 0.0 : health;
		
		String max_str = String.format(ChatColor.GOLD + "" + ChatColor.BOLD + "%,.1f", maxHealth);
		String cur_str = "";
		
		// RED -> DARK_RED -> GRAY
		double rate = health / maxHealth;
		if (0.2 < rate && rate <= 1) {
			cur_str = String.format(ChatColor.RED + "" + ChatColor.BOLD + "%,.1f", health);
		}
		else if (0.0 < rate && rate <= 0.2) {
			cur_str = String.format(ChatColor.DARK_RED + "" + ChatColor.BOLD + "%,.1f", health);
		}
		else if (rate <= 0.0) {
			cur_str = String.format(ChatColor.GRAY + "" + ChatColor.BOLD + "%,.1f", health);
		}
		
		String text = String.format("%s : %s(%s%s/%s%s)", name, default_color, cur_str, default_color, max_str, default_color);
		
		return text;
	}
}
