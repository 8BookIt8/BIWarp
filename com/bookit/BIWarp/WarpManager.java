package com.bookit.BIWarp;

import java.util.*;

public class WarpManager {
    protected static Map<String, Warp> warpMap = new HashMap<String, Warp>();

    /**
     * Add warp to warpMap
     *
     * @param warp Warp to register
     * @return True if registration is successful
     */
    public static boolean registerWarp(Warp warp) {
        if (isRegistered(warp)) {
            return false;
        }

        warpMap.put(warp.getName(), warp);
        return true;
    }

    /**
     * Remove warp to warpMap
     *
     * @param warp Warp to unregister
     * @return True if unregistration is successful
     */
    public static boolean unregisterWarp(Warp warp) {
        if (!isRegistered(warp)) {
            return false;
        }

        warpMap.remove(warp.getName());
        return true;
    }

    /**
     * Remove warp to warpMap
     *
     * @param name Warp to unregister
     * @return True if unregistration is successful
     */
    public static boolean unregisterWarp(String name) {
        if (!isRegistered(name)) {
            return false;
        }

        warpMap.remove(name.toLowerCase());
        return true;
    }

    /**
     * Get if warp is already registered
     *
     * @param warp Warp to check
     * @return True if warp is already registered
     */
    public static boolean isRegistered(Warp warp) {
        if (warpMap.containsKey(warp.getName())) {
            return true;
        }
        return false;
    }

    /**
     * Get if warp is already registered
     *
     * @param name Name to check
     * @return True if warp is already registered
     */
    public static boolean isRegistered(String name) {
        if (warpMap.containsKey(name.toLowerCase())) {
            return true;
        }
        return false;
    }

    /**
     * Get warpMap
     *
     * @return warpMap WarpMap
     */
    public static Map<String, Warp> getMap() {
        return warpMap;
    }

    /**
     * Get warp by name
     *
     * @param name Name of warp
     * @return warp Warp
     */
    public static Warp getWarp(String name) {
        if (!isRegistered(name.toLowerCase())) {
            return null;
        }

        Warp warp = warpMap.get(name.toLowerCase());
        return warp;
    }

    /**
     * Get warp option
     * @return Warp option
     */
    public static String getOption() {
        return "설명, 타이틀, 서브타이틀, 딜레이";
    }

    /**
     * Get warp option list
     * @return Warp option list
     */
    public static List<String> getOptionList() {
        return Arrays.asList("설명:", "타이틀:", "서브타이틀:", "딜레이:");
    }
}
