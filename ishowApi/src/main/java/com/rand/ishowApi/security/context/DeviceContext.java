package com.rand.ishowApi.security.context;

public class DeviceContext {
    private DeviceContext() {
        // prevent instantiation
    }

    private static final ThreadLocal<DeviceInfo> DEVICE_HOLDER = new ThreadLocal<>();

    public static void set(DeviceInfo deviceInfo) {
        DEVICE_HOLDER.set(deviceInfo);
    }

    public static DeviceInfo get() {
        return DEVICE_HOLDER.get();
    }

    public static boolean exists() {
        return DEVICE_HOLDER.get() != null;
    }

    public static void clear() {
        DEVICE_HOLDER.remove();
    }
}
