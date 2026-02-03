package dev.hytalemodding.config;

import java.util.HashMap;
import java.util.Map;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.map.MapCodec;


//not in use!!
public class Itemcmdconfig {

    private Map<String, String> itemsCmds;

    public static final BuilderCodec<Itemcmdconfig> CODEC =
        BuilderCodec.builder(Itemcmdconfig.class, Itemcmdconfig::new)
            .append(new KeyedCodec<>("itemsCmds", new  MapCodec<>(Codec.STRING, HashMap::new, false)),
            (data, value) -> data.itemsCmds = value,
            data -> data.itemsCmds)
        .add()
        .build();

    public Itemcmdconfig(){
        
    }

    public Map<String, String> getItemCmds(){
        return itemsCmds;
    }


}
