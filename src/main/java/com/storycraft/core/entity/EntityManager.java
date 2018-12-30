package com.storycraft.core.entity;

import com.storycraft.StoryPlugin;
import com.storycraft.core.MiniPlugin;
import com.storycraft.core.MiniPluginLoader;
import com.storycraft.core.entity.movement.CrawlMovement;

public class EntityManager extends MiniPlugin {

    private CrawlMovement crawlMovement;

    @Override
    public void onLoad(StoryPlugin plugin) {
        MiniPluginLoader loader = plugin.getMiniPluginLoader();

        loader.addMiniPlugin(crawlMovement = new CrawlMovement());
    }

    public CrawlMovement getCrawlMovement() {
        return crawlMovement;
    }
}