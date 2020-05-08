package com.gmx.mattcha.dynmaparea;

import com.gmx.mattcha.dynmaparea.util.BaseCustomMessage;
import host.kuro.kurobase.KuroBase;
import host.kuro.kurobase.database.AreaData;
import org.bukkit.plugin.java.JavaPlugin;
import org.dynmap.DynmapCommonAPI;
import org.dynmap.markers.AreaMarker;
import org.dynmap.markers.MarkerAPI;
import org.dynmap.markers.MarkerSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainClass extends JavaPlugin {

    public static final String MSG_DESCRIPTION = "marker.description";

    private MarkerAPI markerAPI;
    private MarkerSet markerSet;

    private BaseCustomMessage msg;

    public List<Integer> colors = new ArrayList<>();

    public Random rand = new Random();

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        this.msg = new BaseCustomMessage();
        this.msg.setMessage(MSG_DESCRIPTION, this.getConfig().getString(MSG_DESCRIPTION));

        // Check whether KuroBase is enabled
        KuroBase kuroBase = (KuroBase) this.getServer().getPluginManager().getPlugin("KuroBase");
        if (kuroBase == null) {
            this.getLogger().warning("Couldn't find to KuroBase");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Get DynMapCommonAPI
        DynmapCommonAPI dynmap = (DynmapCommonAPI) this.getServer().getPluginManager().getPlugin("dynmap");
        if (dynmap == null) {
            this.getLogger().warning("Couldn't find to Dynmap");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        this.markerAPI = dynmap.getMarkerAPI();

        List<String> strColors = this.getConfig().getStringList("marker.colors");
        for (String c : strColors) {
            this.colors.add(Integer.parseInt(c, 16));
        }

        this.registerMarkerSet();
        this.updateMarkerSet();

        KuroBase.GetProtect();
    }

    public int getRandomColor() {
        return this.colors.get(this.rand.nextInt(this.colors.size()));
    }

    public void registerMarkerSet() {
        String label = this.getConfig().getString("markerset.name");
        this.markerSet = this.markerAPI.createMarkerSet("protection_area_marker", label, null, false);
        if (this.markerSet == null) {
            this.getLogger().warning("Failed to create MarkerSet of protection_area_marker");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        this.markerSet.setLabelShow(true);
        this.markerSet.setHideByDefault(true);
    }

    public void updateMarkerSet() {
        List<AreaData> list = KuroBase.GetProtect();
        for (AreaData data : list) {
            String world = "world"; // bad hack
            double[] x = new double[] {data.x1, data.x2};
            double[] z = new double[] {data.z1, data.z2};

            String label = data.owner + data.name;

            AreaMarker marker = this.markerSet.createAreaMarker(null, label, true, world, x, z, false);
            marker.setRangeY(data.y1, data.y2);
            if (this.msg.getMessage(MSG_DESCRIPTION).length() > 0) {
                marker.setDescription(this.msg.getMessage(MSG_DESCRIPTION, data.name, data.owner,
                        String.valueOf(data.x1), String.valueOf(data.y1), String.valueOf(data.z1),
                        String.valueOf(data.x2), String.valueOf(data.y2), String.valueOf(data.z2)));
            }

            int color = this.getRandomColor();

            marker.setLineStyle(2, 0.9, color);
            marker.setFillStyle(0.4, color);
        }
    }
}
