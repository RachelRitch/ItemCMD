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
import com.hypixel.hytale.logger.HytaleLogger;
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

    HytaleLogger LOGGER = HytaleLogger.get("<ItemCMD>");
    protected String regionName = null;
    protected boolean currentStatus;
    protected boolean status;
    protected final Config<AreaCmdConfig> config;
    protected Map<String, Map<String, String>> areaMap = null;
    protected boolean debug;

    public AreaCmdSystem(Config<AreaCmdConfig> config){
      super(0.1f);
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
        debug = player.hasPermission("item.areacmd.debug");
        

        Vector3d pos  = playerRef.getTransform().getPosition();
        World world = store.getExternalData().getWorld();
        var allPlayers =  world.getPlayerRefs();
        //is player in region
        OrbisGuardAPI api = OrbisGuardAPI.getInstance();
        IRegionManager manager = api.getRegionContainer().getRegionManager(world.getName());
        Set<IRegion> regions = manager.getRegionsAt((int) pos.x,(int) pos.y,(int) pos.z);
        List<String> areaRegions = new ArrayList<>();

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
            LOGGER.atSevere().log("Operation was interrupted");
            return;
        } catch (ExecutionException ex) {
              LOGGER.atSevere().log("Operation was interrupted");
              System.getLogger(AreaCmdSystem.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        status = isAreaRegion(player, areaRegions, areaMap);
        
        // 2. Only look up the specific region we care about
        Map<String, String> innerMap = areaMap.get(regionName);
        //enter & exit actiontypes for area command
        if(status != currentStatus){
          if (innerMap != null) {
            String targetAction = !currentStatus ? "enter" : "exit";
            innerMap.forEach((command, actionType) -> {
              if (actionType.equalsIgnoreCase(targetAction)) {
                String parsed = AreaCmdPlaceHolder(player, playerRef, world, command);
                  if (command.contains("{allplayers}")){
                    allPlayers.forEach(aplayerRef -> {
                      String playersparsed = AreaCmdPlaceHolder(player, aplayerRef, world, command);
                      CommandManager.get().handleCommand(ConsoleSender.INSTANCE, playersparsed);
                      if (debug) player.sendMessage(Message.raw("Player: " + aplayerRef.getUsername() + " had command: " + playersparsed + " put on them in region: " + regionName));
                    });
                  }else CommandManager.get().handleCommand(ConsoleSender.INSTANCE, parsed); if (debug) player.sendMessage(Message.raw("Player: " + player.getDisplayName() + " has had command: " + parsed + " used apon region: " + regionName));
                  LOGGER.atInfo().log(player.getDisplayName() + " has interacted with " + regionName + " and the commnd: " + parsed + " was sent with " + actionType);
                }
              
            });
          }
        }
        //looping actiontype for area commands
        if(status){
          if (innerMap != null) {
            String targetAction = "looping";
            innerMap.forEach((command, actionType) -> {
              if (actionType.equalsIgnoreCase(targetAction)) {
                String parsed = AreaCmdPlaceHolder(player, playerRef, world, command);
                  if (command.contains("{allplayers}")){
                    allPlayers.forEach(aplayerRef -> {
                      String playersparsed = AreaCmdPlaceHolder(player, aplayerRef, world, command);
                      CommandManager.get().handleCommand(ConsoleSender.INSTANCE, playersparsed);
                      if (debug) player.sendMessage(Message.raw("Player: " + aplayerRef.getUsername() + " had command: " + playersparsed + " put on them in region: " + regionName));
                    });
                  }else CommandManager.get().handleCommand(ConsoleSender.INSTANCE, parsed); if (debug) player.sendMessage(Message.raw("Player: " + player.getDisplayName() + " has had command: " + parsed + " used apon region: " + regionName));
                  LOGGER.atInfo().log(player.getDisplayName() + " has interacted with " + regionName + " and the commnd: " + parsed + " was sent with " + actionType);
                }
              
            });
          }
        }
        currentStatus = status;
    }

    public boolean isAreaRegion(Player player, List<String> regionAreas, Map<String, Map<String, String>> areaMap){
      for (String regionArea : regionAreas){
          regionName = regionArea;
          if (!player.hasPermission("ItemCmd.AreaCmd.region." + regionArea)){
            if (debug) player.sendMessage(Message.raw("Player: " + player.getDisplayName() + " didn't have permissions to use Region: " + regionName));
            return false;
          }
          if (areaMap == null){
            player.sendMessage(Message.raw("areaMap = null"));
            return false;
          }
          if (areaMap.containsKey(regionName)) {
            return true;
          }
          return false;
      }
      return false;
    }
    //placeholder for AreaCmdPlaceholder
    public String AreaCmdPlaceHolder(Player player, PlayerRef allPlayer, World world, String command){
      String parsed = command;
      String userPlaceholder = "{player}";
      String playersPlaceholder = "{allplayers}";
      String worldPlaceholder = "{world}";
      if (parsed.contains(userPlaceholder)) parsed = parsed.replace(userPlaceholder, player.getDisplayName());
      if (parsed.contains(worldPlaceholder)) parsed = parsed.replace(worldPlaceholder, world.getName());
      if (parsed.contains(playersPlaceholder)) parsed = parsed.replace(playersPlaceholder, allPlayer.getUsername());
      LOGGER.atInfo().log("Command: " + command + " has been parse and is now: " + parsed);
      return parsed;
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