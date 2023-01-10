package com.example.carofinal;

public interface FragmentCallBacks {
    /// List Device
    public  void SelectDeviceMaintoFragment(String[] list_device);
    public void ChatMainToFrag(String chat);
    /// play game
    public void InitgameMaintoFrag(Boolean check);
    public void PlayFromMainToFragment(Integer x,Integer y);
}

