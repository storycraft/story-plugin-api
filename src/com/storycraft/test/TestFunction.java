package com.storycraft.test;

import com.storycraft.StoryPlugin;
import com.storycraft.server.packet.AsyncPacketOutEvent;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class TestFunction implements Listener {

    public static void test(StoryPlugin plugin, org.bukkit.World world) {
        WorldServer w = ((CraftWorld) world).getHandle();

        plugin.getServer().getPluginManager().registerEvents(new TestFunction(), plugin);
    }
}