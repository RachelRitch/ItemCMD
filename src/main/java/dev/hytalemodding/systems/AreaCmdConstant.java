package dev.hytalemodding.systems;

import java.util.Map;

import javax.annotation.Nonnull;

import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.DelayedEntitySystem;
import com.hypixel.hytale.server.core.command.system.CommandManager;
import com.hypixel.hytale.server.core.console.ConsoleSender;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.Config;

import dev.hytalemodding.config.AreaCmdConfig;



public class AreaCmdConstant extends DelayedEntitySystem<EntityStore> {

    protected final Config<AreaCmdConfig> config;
    protected final AreaCmdSystem acSystem;

    public AreaCmdConstant(int seconds, Config<AreaCmdConfig> config){
        super(seconds);
        this.config = config;
        this.acSystem = new AreaCmdSystem(this.config);
    }


    @Override
    public void tick(float dt, int index, @Nonnull ArchetypeChunk<EntityStore> archetypeChunk,
      @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer) {
        Ref<EntityStore> ref = archetypeChunk.getReferenceTo(index);
        Player player = store.addComponent(ref, Player.getComponentType());
        PlayerRef playerRef = store.addComponent(ref, PlayerRef.getComponentType());
        String inRegion = acSystem.getRegion();
        AreaCmdConfig settings = config.get();
        Map<String, Map<String, String>> AreaMap = settings.getMap();
        if(AreaMap == null || inRegion == null || inRegion.isEmpty()) return;
        if(AreaMap.containsKey(inRegion)){
          AreaMap.forEach((region, innerMap) -> {
            innerMap.forEach((command, active) -> {
                if (active.equalsIgnoreCase("looping")){
                    CommandManager.get().handleCommand(ConsoleSender.INSTANCE, command);
                }else return;
            });
          });
        }

    }
    @NonNullDecl
    @Override
    public Query<EntityStore> getQuery() {
      return Query.and(Player.getComponentType());
    }

}
