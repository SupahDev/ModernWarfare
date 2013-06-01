package io.github.supahdev;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class ModernWarfare extends JavaPlugin implements Listener {
	@Override
    public void onEnable(){
		getServer().getPluginManager().registerEvents(this, this);
		getLogger().info("ModernWarfare has been enabled.");
		getLogger().info("Killing all entities.");
		List<LivingEntity> entities = new ArrayList<LivingEntity>();
		entities = getServer().getWorld("world").getLivingEntities();
		getServer().getScheduler().runTaskLaterAsynchronously(this, new Runnable(){
			public void run(){
				getServer().getWorld("world").setTime(13000);
			}
		}, 200L);
		getServer().getWorld("world").setTime(13500);
		getServer().getWorld("world").setDifficulty(Difficulty.HARD);
		for (LivingEntity e: entities){
			if (!e.getType().equals(EntityType.PLAYER)){
				e.setHealth(0);
			}
		}
    }
 
    @Override
    public void onDisable(){
		getLogger().severe("ModernWarfare has been disabled.");    	
    }
    
    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e){
    	if (e.getEntity() instanceof Player){
    		if (e.getDamager() instanceof Zombie){
    			e.setDamage(7);
    		}
    	}
    	if (e.getEntity() instanceof Zombie){
    		if (e.getDamager() instanceof Player){
    			Zombie zombie = (Zombie) e.getEntity();
        		zombie.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 5000, 3));
    		}
    	}
    }
    
    @EventHandler
    public void onSpawn(CreatureSpawnEvent e){
    	if (!e.getEntityType().equals(EntityType.ZOMBIE) && !e.getEntityType().equals(EntityType.PLAYER)){
    		Location loc = e.getEntity().getLocation();
    		e.getEntity().damage(300);
    		Zombie ent = (Zombie)getServer().getWorld("world").spawnEntity(loc, EntityType.ZOMBIE);
    		ent.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 5000, 3));
    	} else if (e.getEntityType().equals(EntityType.ZOMBIE)){
    		Zombie zombie = (Zombie) e.getEntity();
    		zombie.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 5000, 3));
    	}
    }
    
    @EventHandler
    public void onEntityKill(EntityDeathEvent e){
    	if (e.getDrops() != null){
    		for(ItemStack i: e.getDrops())
    			i.setType(Material.AIR);
    	}
    	if (e.getEntityType().equals(EntityType.ZOMBIE)){
    		Player player = (Player)e.getEntity().getKiller();
    		player.sendMessage("You killed a zombie.");
    		// Points
    	}
    }
    
    @EventHandler
    public void onInteract(PlayerInteractEvent e){
    	Player player = e.getPlayer();
    	if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
    		if (player.getItemInHand().equals(Material.BOW)){
    			Arrow arrow = player.shootArrow();
    		}
    	}
    }
}
