package com.xxmicloxx.NoteBlockAPI.hooks;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

public class ProtocolLibHook {

    public ProtocolManager protocolManager;

    {
        protocolManager = ProtocolLibrary.getProtocolManager();
    }

}
