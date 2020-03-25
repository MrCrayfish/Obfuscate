package com.mrcrayfish.obfuscate.proxy;

import com.mrcrayfish.obfuscate.common.data.SyncedPlayerData;

import java.util.List;

/**
 * Author: MrCrayfish
 */
public class CommonProxy
{
    public void preInit() {}

    public void init() {}

    public void postInit() {}

    public void updatePlayerData(int entityId, List<SyncedPlayerData.DataEntry<?>> entries) {}
}
