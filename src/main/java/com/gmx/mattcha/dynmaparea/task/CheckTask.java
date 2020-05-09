package com.gmx.mattcha.dynmaparea.task;

import com.gmx.mattcha.dynmaparea.MainClass;

import java.util.TimerTask;

public class CheckTask extends TimerTask {

    protected MainClass plugin;

    public CheckTask(MainClass plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        this.plugin.updateMarkerSet(false);
    }
}
