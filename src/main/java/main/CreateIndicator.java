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
		Player show = null;
		LivingEntity target = null;
		
		if (e.getDamager() instanceof Player) {
			show = (Player) e.getDamager();
			target = (LivingEntity) e.getEntity();
		}
		if ((e.getEntity() instanceof Player) &&
			(e.getDamager() instanceof LivingEntity)) {
			show = (Player) e.getEntity();
			target = (LivingEntity) e.getDamager();
		}
		
		if (show == null || target == null) {
			return;
		}
		
		String target_name = target.getName();
		double target_maxHealth = target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
		double target_currentHealth = target.getHealth();
		
		String text = String.format("%s : %,.1f / %,.1f", target_name, target_currentHealth, target_maxHealth);
		
		TextComponent context = new TextComponent(text);
		show.spigot().sendMessage(ChatMessageType.ACTION_BAR, context);
	}
	
	@EventHandler
	public void targetedByEntity(EntityTargetLivingEntityEvent e) {
		
	}
	
}
