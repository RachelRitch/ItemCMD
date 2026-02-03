package dev.hytalemodding.commands;

import com.hypixel.hytale.server.core.command.system.basecommands.AbstractCommandCollection;

import dev.hytalemodding.commands.subcommands.AreaCmdCreate;
import dev.hytalemodding.commands.subcommands.AreaCmdDelete;




public class AreaCmdCommand extends AbstractCommandCollection  {


    public AreaCmdCommand(){
        super("areacmd", "areacmd is the area command for ItemCMD!");
        addSubCommand(new AreaCmdDelete());
        addSubCommand(new AreaCmdCreate());

        this.addAliases("ac");
    }

}
