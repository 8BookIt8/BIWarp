package com.bookit.BIWarp;

import java.util.*;

public class WarpManager {
    private final static Map<String, Warp> warpMap = new HashMap<>();

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
        return warpMap.containsKey(warp.getName());
    }

    /**
     * Get if warp is already registered
     *
     * @param name Name to check
     * @return True if warp is already registered
     */
    public static boolean isRegistered(String name) {
        return warpMap.containsKey(name.toLowerCase());
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
     * @return Warp
     */
    public static Warp getWarp(String name) {
        if (!isRegistered(name.toLowerCase())) {
            return null;
        }
        return warpMap.get(name.toLowerCase());
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
