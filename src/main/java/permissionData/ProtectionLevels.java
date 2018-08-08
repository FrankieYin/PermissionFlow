package permissionData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Copyright (c) 2018, Zhijun Yin. All rights reserved.
 */
public class ProtectionLevels {

    private static final List<String> PROTECTION_NORMAL = new ArrayList<>(Arrays.asList(
            "ACCESS_LOCATION_EXTRA_COMMANDS",
            "ACCESS_NETWORK_STATE",
            "ACCESS_LOCATION_EXTRA_COMMANDS",
            "ACCESS_NETWORK_STATE",
            "ACCESS_NOTIFICATION_POLICY",
            "ACCESS_WIFI_STATE",
            "BLUETOOTH",
            "BLUETOOTH_ADMIN",
            "BROADCAST_STICKY",
            "CHANGE_NETWORK_STATE",
            "CHANGE_WIFI_MULTICAST_STATE",
            "CHANGE_WIFI_STATE",
            "DISABLE_KEYGUARD",
            "EXPAND_STATUS_BAR",
            "GET_PACKAGE_SIZE",
            "INSTALL_SHORTCUT",
            "INTERNET",
            "KILL_BACKGROUND_PROCESSES",
            "MANAGE_OWN_CALLS",
            "MODIFY_AUDIO_SETTINGS",
            "NFC",
            "READ_SYNC_SETTINGS",
            "READ_SYNC_STATS",
            "RECEIVE_BOOT_COMPLETED",
            "REORDER_TASKS",
            "REQUEST_COMPANION_RUN_IN_BACKGROUND",
            "REQUEST_COMPANION_USE_DATA_IN_BACKGROUND",
            "REQUEST_DELETE_PACKAGES",
            "REQUEST_IGNORE_BATTERY_OPTIMIZATIONS",
            "SET_ALARM",
            "SET_WALLPAPER",
            "SET_WALLPAPER_HINTS",
            "TRANSMIT_IR",
            "USE_FINGERPRINT",
            "VIBRATE",
            "WAKE_LOCK",
            "WRITE_SYNC_SETTINGS"
    ));

    private static final ArrayList<String> DANGEROUSE_PERMISSIONS = new ArrayList<>(Arrays.asList(
            "READ_CALENDAR",
            "WRITE_CALENDAR",
            "CAMERA",
            "READ_CONTACTS",
            "WRITE_CONTACTS",
            "GET_ACCOUNTS",
            "ACCESS_FINE_LOCATION",
            "ACCESS_COARSE_LOCATION",
            "RECORD_AUDIO",
            "READ_PHONE_STATE",
            "READ_PHONE_NUMBERS",
            "CALL_PHONE",
            "ANSWER_PHONE_CALLS",
            "READ_CALL_LOG",
            "WRITE_CALL_LOG",
            "ADD_VOICEMAIL",
            "USE_SIP",
            "PROCESS_OUTGOING_CALLS",
            "BODY_SENSORS",
            "SEND_SMS",
            "RECEIVE_SMS",
            "READ_SMS",
            "RECEIVE_WAP_PUSH",
            "RECEIVE_MMS",
            "READ_EXTERNAL_STORAGE",
            "WRITE_EXTERNAL_STORAGE"
    ));

}
