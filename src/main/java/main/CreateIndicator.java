package main;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.projectiles.ProjectileSource;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class CreateIndicator implements Listener {
	
	@EventHandler
	public void interactToEntity(PlayerInteractEntityEvent e) {
		if (!(e.getRightClicked() instanceof LivingEntity)) {
			return;
		}

		Player show = e.getPlayer();
		LivingEntity target = (LivingEntity) e.getRightClicked();
		
		String text = getContext(target, -1);
		TextComponent context = new TextComponent(text);
		
		show.spigot().sendMessage(ChatMessageType.ACTION_BAR, context);
	}
	
	
	@EventHandler
	public void damagedByEntity(EntityDamageByEntityEvent e) {
		Player show = null;
		LivingEntity target = null;
		
		// TNT에 의한
		if (e.getDamager() instanceof TNTPrimed) {
			TNTPrimed TNT = (TNTPrimed) e.getDamager();
			Entity source = TNT.getSource();
			
			if (source == null) {
				return;
			}
			
			if (!(source instanceof Player)) {
				return;
			}
			
			show = (Player) source;
		}
		
		// 투척물에 의한
		if (e.getDamager() instanceof Projectile) {
			Projectile proj = (Projectile) e.getDamager();
			ProjectileSource shooter = proj.getShooter();
			
			if (shooter == null) {
				return;
			}
			
			if (!(shooter instanceof Player)) {
				return;
			}
			
			show = (Player) shooter;
		}
		
		// 잔류형 포션에 의한
		if (e.getDamager() instanceof AreaEffectCloud) {
			AreaEffectCloud cloud = (AreaEffectCloud) e.getDamager();
			ProjectileSource shooter = cloud.getSource();
			
			if (shooter == null) {
				return;
			}
			
			if (!(shooter instanceof Player)) {
				return;
			}
			
			show = (Player) shooter;
		}

		// 번개에 의한
		if (e.getDamager() instanceof LightningStrike) {
			LightningStrike thor = (LightningStrike) e.getDamager();
			show = thor.getCausingPlayer();
		}
		
		// 플레이어에 의한
		if (e.getDamager() instanceof Player) {
			show = (Player) e.getDamager();
		}
		
		// 데미지를 준 개체가 LivingEntity가 아닐 때,
		if (e.getEntity() instanceof LivingEntity) {
			target = (LivingEntity) e.getEntity();
		}
		
		if (show == null || target == null) {
			return;
		}

		String text = getContext(target, e.getDamage());
		TextComponent context = new TextComponent(text);
		
		show.spigot().sendMessage(ChatMessageType.ACTION_BAR, context);
	}
	
	
	private String getContext(LivingEntity target, double damage) {
		String default_color = ChatColor.WHITE + "" + ChatColor.BOLD;
		String name = default_color + target.getName();
		
		double maxHealth = target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
		double health = target.getHealth() - (damage < 0 ? 0 : damage);
		health = health <= 0.0 ? 0.0 : health;
		
		String damage_str = String.format("%s(" + ChatColor.AQUA + "-%,.1f%s)", default_color, damage, default_color);
		if (damage < 0) {
			damage_str = "";
		}
		String max_str = String.format(ChatColor.GOLD + "" + ChatColor.BOLD + "%,.1f", maxHealth);
		String cur_str = "";
		
		// RED -> DARK_RED -> GRAY
		double rate = health / maxHealth;
		if (0.2 < rate) {
			cur_str = String.format(ChatColor.RED + "" + ChatColor.BOLD + "%,.1f", health);
		}
		else if (0.0 < rate && rate <= 0.2) {
			cur_str = String.format(ChatColor.DARK_RED + "" + ChatColor.BOLD + "%,.1f", health);
		}
		else if (rate <= 0.0) {
			cur_str = String.format(ChatColor.GRAY + "" + ChatColor.BOLD + "%,.1f", health);
		}
		
		String text = String.format("%s : %s[%s%s %s/ %s%s]", name, default_color, cur_str, damage_str, default_color, max_str, default_color);
		
		return text;
	}
}
