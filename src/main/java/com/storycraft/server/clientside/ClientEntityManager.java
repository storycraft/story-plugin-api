package com.storycraft.server.clientside;

import com.storycraft.server.ServerExtension;
import com.storycraft.server.event.client.AsyncPlayerLoadChunkEvent;
import com.storycraft.util.ConnectionUtil;
import com.storycraft.util.EntityPacketUtil;
import net.minecraft.server.v1_13_R2.*;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientEntityManager extends ServerExtension implements Listener {

    private Map<World, List<Entity>> entityMap;

    public ClientEntityManager(){
        this.entityMap = new HashMap<>();
    }

    @Override
    public void onDisable(boolean reload){
        for (World w : entityMap.keySet()){
            for (Entity e : entityMap.get(w)){
                sendDestroyPacket(e);
            }
        }

        entityMap.clear();
    }

    @Override
    public void onEnable(){
        getPlugin().getServer().getPluginManager().registerEvents(this, getPlugin());
    }

    public void addClientEntity(Entity e){
        if (contains(e))
            return;

        if (!hasWorld(e.getWorld())) {
            entityMap.put(e.getWorld(), new ArrayList<>());
        }

        entityMap.get(e.getWorld()).add(e);
        sendSpawnPacket(e);
        sendUpdatePacket(e);
    }

    public void removeClientEntity(Entity e){
        if (!contains(e))
            return;

        entityMap.get(e.getWorld()).remove(e);
        sendDestroyPacket(e);
    }

    public boolean contains(Entity e){
        return hasWorld(e.getWorld()) && entityMap.get(e.getWorld()).contains(e);
    }

    protected boolean hasWorld(World w){
        return entityMap.containsKey(w);
    }

    public void update(Entity e){
        sendUpdatePacket(e);
    }

    protected void sendSpawnPacket(Player p, Entity e){
        ConnectionUtil.sendPacket(p, EntityPacketUtil.getEntitySpawnPacket(e));
    }

    protected void sendSpawnPacket(Entity e){
        Location loc = e.getBukkitEntity().getLocation();

        ConnectionUtil.sendPacketNearby(loc, EntityPacketUtil.getEntitySpawnPacket(e));
    }

    protected void sendUpdatePacket(Entity e){
        Location loc = e.getBukkitEntity().getLocation();

        ConnectionUtil.sendPacketNearby(loc, EntityPacketUtil.getEntityMetadataPacket(e));
    }

    protected void sendUpdatePacket(Player p, Entity e){
        ConnectionUtil.sendPacket(p, EntityPacketUtil.getEntityMetadataPacket(e));
    }

    protected void sendDestroyPacket(Entity e){
        Location loc = e.getBukkitEntity().getLocation();

        ConnectionUtil.sendPacketNearby(loc, EntityPacketUtil.getEntityDestroyPacket(e));
    }

    protected void sendDestroyPacket(Player p, Entity e){
        ConnectionUtil.sendPacket(p, EntityPacketUtil.getEntityDestroyPacket(e));
    }

    @EventHandler
    public void onPlayerChunkLoad(AsyncPlayerLoadChunkEvent e){
        if (!entityMap.containsKey(e.getWorld()))
            return;

        for (Entity entity : new ArrayList<>(entityMap.get(e.getWorld()))){
            Chunk chunk = entity.getBukkitEntity().getLocation().getChunk();
            if (e.getChunk().equals(chunk)) {
                sendSpawnPacket(e.getPlayer(), entity);
                sendUpdatePacket(e.getPlayer(), entity);
            }
        }
    }
}
