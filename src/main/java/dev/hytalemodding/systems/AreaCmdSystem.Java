package dev.hytalemodding.systems;

import java.util.Set;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.orbisguard.api.OrbisGuardAPI;
import com.orbisguard.api.region.IRegion;



public class AreaCmdSystem extends EntityTickingSystem<EntityStore> {
  
    protected String regionName = null;


    public AreaCmdSystem(){
      super();
    }
    @Override
    @SuppressWarnings("UnnecessaryReturnStatement")
    public void tick(float dt, int index, @Nonnull ArchetypeChunk<EntityStore> archetypeChunk,
      @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer) {
        Ref<EntityStore> ref = archetypeChunk.getReferenceTo(index);
        PlayerRef playerRef = store.getComponent(ref, PlayerRef.getComponentType());
        if (playerRef == null) return;
        Player player = store.getComponent(ref, Player.getComponentType());
        if (player == null) return;
        UUID worldUuid = playerRef.getWorldUuid();
        if (worldUuid == null) return;
        World world = Universe.get().getWorld(worldUuid);
        if (world == null) return;

        Vector3d pos  = playerRef.getTransform().getPosition();
        //is player in region
        Set<IRegion> region = OrbisGuardAPI.getInstance().getRegionContainer().getRegionsAt(world.getName(), (int) pos.x, (int) pos.y, (int) pos.z);
        //if in region get string
        if (region == null) return;
        else regionName = region.toString();


    }

    //getter for regionName
    public String getRegion(){
      return regionName;
    }

    @Override
    @Nullable
    public Query<EntityStore> getQuery() {
      return null;
    }



}