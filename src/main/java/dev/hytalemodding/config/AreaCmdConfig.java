package dev.hytalemodding.config;



import java.util.HashMap;
import java.util.Map;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.map.MapCodec;


public class AreaCmdConfig {
    // Codec for adding new components to config json file
    public static final BuilderCodec<AreaCmdConfig> CODEC =
        BuilderCodec.builder(AreaCmdConfig.class, AreaCmdConfig::new)
            .append(new KeyedCodec<>("AreaCmd", new  MapCodec<>(Codec.STRING, HashMap::new, false)),
            (data, value) -> data.AreaCmd = value,
            data -> data.AreaCmd)
        .add()
        .build();
    
    private Map<String, String> AreaCmd = new HashMap<>();


    public AreaCmdConfig() {
    }
        //getter
    public Map<String, String> getMap(Map<String, String>  AreaMap){
        return AreaCmd;
    }
        //setter
    public void setMap(Map<String, String>  AreaMap){
        this.AreaCmd = AreaMap;
    }

}
