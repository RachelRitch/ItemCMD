package dev.hytalemodding.systems;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import javax.annotation.Nonnull;

import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.DelayedEntitySystem;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandManager;
import com.hypixel.hytale.server.core.console.ConsoleSender;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.Config;
import com.orbisguard.api.OrbisGuardAPI;
import com.orbisguard.api.region.IRegion;
import com.orbisguard.api.region.IRegionManager;

import dev.hytalemodding.config.AreaCmdConfig;




public class AreaCmdSystem extends DelayedEntitySystem<EntityStore> {

    protected String regionName = null;
    protected boolean currentStatus;
    protected boolean status;
    protected final Config<AreaCmdConfig> config;
    protected Map<String, Map<String, String>> areaMap = null;

    public AreaCmdSystem(Config<AreaCmdConfig> config){
      super(0.2f);
      this.config = config;
    }

    @Override
    public void tick(float dt, int index, @Nonnull ArchetypeChunk<EntityStore> archetypeChunk,
      @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer) {
        Ref<EntityStore> ref = archetypeChunk.getReferenceTo(index);
        PlayerRef playerRef = store.getExternalData().getStore().getComponent(ref, PlayerRef.getComponentType());
        if (playerRef == null) return;
        Player player = store.getExternalData().getStore().getComponent(ref, Player.getComponentType());
        if (player == null) return;
        UUID worldUuid = playerRef.getWorldUuid();
        if (worldUuid == null) return;
        player.sendMessage(Message.raw("Hello " + player.getDisplayName() + " this is the tick of the time"));

        Vector3d pos  = playerRef.getTransform().getPosition();
        World world = store.getExternalData().getWorld();

        //is player in region
        OrbisGuardAPI api = OrbisGuardAPI.getInstance();
        IRegionManager manager = api.getRegionContainer().getRegionManager(world.getName());
        Set<IRegion> regions = manager.getRegionsAt((int) pos.x,(int) pos.y,(int) pos.z);
        List<String> areaRegions = new ArrayList<>();
        player.sendMessage(Message.raw("World: " + world.getName()));
        //if in region get string
        for (IRegion region : regions) {
          if (!region.getId().matches("__global__")){
            areaRegions.add(region.getId());
          }
        }
        try{
            areaMap = config.load().get().getMap();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            player.sendMessage(Message.raw("Operation was interrupted"));
            return;
        } catch (ExecutionException ex) {
              player.sendMessage(Message.raw("Operation was interrupted"));
              System.getLogger(AreaCmdSystem.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        status = isAreaRegion(player, areaRegions, areaMap);
        player.sendMessage(Message.raw("status: " + status + " currentStatus: " + currentStatus));
        // 2. Only look up the specific region we care about
        Map<String, String> innerMap = areaMap.get(regionName);
        if(status != currentStatus){
          if (innerMap != null) {
            String targetAction = !currentStatus ? "enter" : "exit";
            innerMap.forEach((command, actionType) -> {
              if (actionType.equalsIgnoreCase(targetAction)) {
                CommandManager.get().handleCommand(ConsoleSender.INSTANCE, command);
                player.sendMessage(Message.raw("Inner cmd and type: " + command + ", " + actionType));
              }
            });
          }
        }
        currentStatus = status;
    }

    public boolean isAreaRegion(Player player, List<String> regionAreas, Map<String, Map<String, String>> areaMap){
        for (String regionArea : regionAreas){
          player.sendMessage(Message.raw("Region name: " + regionArea));
          regionName = regionArea;
          if (areaMap == null){
            player.sendMessage(Message.raw("areaMap = null!!!!!!!!!!!!!!!!!!!!!!"));
            return false;
          }
          if (areaMap.containsKey(regionName)) {
            return true;
          }
          player.sendMessage(Message.raw("return false 1"));
          return false;
      }
      player.sendMessage(Message.raw("return false 2"));
      return false;
    }
    //getter for regionName
    public String getRegion(){
      return regionName;
    }

    @NonNullDecl
    @Override
    public Query<EntityStore> getQuery() {
      return Query.and(Player.getComponentType());
    }



}