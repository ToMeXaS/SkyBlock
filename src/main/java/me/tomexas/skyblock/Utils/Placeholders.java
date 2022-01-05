package me.tomexas.skyblock.Utils;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.tomexas.skyblock.Skyblock;
import org.bukkit.OfflinePlayer;

public class Placeholders extends PlaceholderExpansion {

    private final Skyblock plugin;

    public Placeholders(Skyblock plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getAuthor() {
        return "ToMeXaS";
    }

    @Override
    public String getIdentifier() {
        return "mcn";
    }

    @Override
    public String getVersion() {
        return "0.1";
    }

    @Override
    public boolean persist() {
        return true; // This is required or else PlaceholderAPI will unregister the Expansion on reload
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if(params.equalsIgnoreCase("title")){
            return "\uE009";
        }
        if(params.equalsIgnoreCase("rank_skrajoklis")){
            return "\uE001";
        }
        if(params.equalsIgnoreCase("guild_icon")){
            return "\uE012";
        }
        if(params.equalsIgnoreCase("rank_staff")){
            return "\uE002";
        }

        return null; // Placeholder is unknown by the Expansion
    }
}
