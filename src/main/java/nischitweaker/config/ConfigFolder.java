package nischitweaker.config;

import net.minecraftforge.fml.common.Loader;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class ConfigFolder {
    private final String[] requiredModids;
    private List<ConfigFolder> childFolders;

    protected ConfigFolder(String... requiredModids) {
        this.requiredModids = requiredModids;
    }

    /** Updates this folder and the folders in its public fields, skipped if any of the required mods isn't loaded */
    public final void update() {
        if(!Arrays.stream(requiredModids).allMatch(Loader::isModLoaded)) return;
        updateConfig();
        if(childFolders == null) childFolders = findChildFolders();
        childFolders.forEach(ConfigFolder::update);
    }

    private List<ConfigFolder> findChildFolders() {
        List<ConfigFolder> folders = new ArrayList<>();
        for(Field field : getClass().getFields()) {
            if(!ConfigFolder.class.isAssignableFrom(field.getType())) continue;
            try {
                ConfigFolder child = (ConfigFolder) field.get(Modifier.isStatic(field.getModifiers()) ? null : this);
                if(child != null) folders.add(child);
            } catch(IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return folders;
    }

    /** Called after the config is loaded or changed ingame */
    protected void updateConfig() {}
}
