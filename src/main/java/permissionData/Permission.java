package permissionData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Copyright (c) 2018, Zhijun Yin. All rights reserved.
 */
public class Permission {

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

    private static final List<String> DANGEROUSE_PERMISSIONS = new ArrayList<>(Arrays.asList(
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

    private static final List<String> PROTECTION_SIGNATURE = new ArrayList<>(Arrays.asList(
            "BIND_ACCESSIBILITY_SERVICE",
            "BIND_AUTOFILL_SERVICE",
            "BIND_CARRIER_SERVICES",
            "BIND_CHOOSER_TARGET_SERVICE",
            "BIND_CONDITION_PROVIDER_SERVICE",
            "BIND_DEVICE_ADMIN",
            "BIND_DREAM_SERVICE",
            "BIND_INCALL_SERVICE",
            "BIND_INPUT_METHOD",
            "BIND_MIDI_DEVICE_SERVICE",
            "BIND_NFC_SERVICE",
            "BIND_NOTIFICATION_LISTENER_SERVICE",
            "BIND_PRINT_SERVICE",
            "BIND_SCREENING_SERVICE",
            "BIND_TELECOM_CONNECTION_SERVICE",
            "BIND_TEXT_SERVICE",
            "BIND_TV_INPUT",
            "BIND_VISUAL_VOICEMAIL_SERVICE",
            "BIND_VOICE_INTERACTION",
            "BIND_VPN_SERVICE",
            "BIND_VR_LISTENER_SERVICE",
            "BIND_WALLPAPER",
            "CLEAR_APP_CACHE",
            "MANAGE_DOCUMENTS",
            "READ_VOICEMAIL",
            "REQUEST_INSTALL_PACKAGES",
            "SYSTEM_ALERT_WINDOW",
            "WRITE_SETTINGS",
            "WRITE_VOICEMAIL"
    ));

    private String name;
    private ProtectionLevel level;

    public Permission(String name) {
        this.name = name;
        if (PROTECTION_NORMAL.contains(name)) {
            level = ProtectionLevel.PROTECTION_NORMAL;
        } else if (DANGEROUSE_PERMISSIONS.contains(name)) {
            level = ProtectionLevel.DANGEROUSE_PERMISSIONS;
        } else if (PROTECTION_SIGNATURE.contains(name)) {
            level = ProtectionLevel.PROTECTION_SIGNATURE;
        } else {
            level = ProtectionLevel.SPECIAL;
        }
    }

}
