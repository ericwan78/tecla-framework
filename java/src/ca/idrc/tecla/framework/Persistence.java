package ca.idrc.tecla.framework;

import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.Settings;

public class Persistence {
	public static final String PREF_FULLSCREEN_MODE = "fullscreen_mode";
	//	public static final String PREF_CONNECT_TO_SHIELD = "shield_connect";
	public static final String PREF_SPEAKERPHONE_SWITCH = "speakerphone_switch";
	public static final String PREF_SELF_SCANNING = "self_scanning";
	public static final String PREF_INVERSE_SCANNING = "inverse_scanning";
	public static final String PREF_SCAN_DELAY_INT = "scan_delay_int";
	public static final String PREF_HUD = "hud_visibility";
	public static final String PREF_SINGLESWITCH_OVERLAY = "single_switch_overlay";
	public static final String PREF_HUD_SELF_SCANNING = "hud_self_scanning";

	private static final String IME_ID = "com.android.inputmethod.latin/.LatinIME";

	public static final float MAX_SCAN_DELAY = 3000;

	private static Persistence sInstance;

	private boolean is_ime_running;
	private boolean is_ime_showing;
//	private boolean is_hud_cancelled;
	//	private boolean is_framework_ready;

	private int mScanDelay;

	public static final String PREF_SWITCH_J1 = "switch_j1";
	public static final String PREF_SWITCH_J2 = "switch_j2";
	public static final String PREF_SWITCH_J3 = "switch_j3";
	public static final String PREF_SWITCH_J4 = "switch_j4";
	public static final String PREF_SWITCH_E1 = "switch_e1";
	public static final String PREF_SWITCH_E2 = "switch_e2";

	public static final String PREF_SWITCH_J1_TECLA = "switch_j1_tecla";
	public static final String PREF_SWITCH_J2_TECLA = "switch_j2_tecla";
	public static final String PREF_SWITCH_J3_TECLA = "switch_j3_tecla";
	public static final String PREF_SWITCH_J4_TECLA = "switch_j4_tecla";
	public static final String PREF_SWITCH_E1_TECLA = "switch_e1_tecla";
	public static final String PREF_SWITCH_E2_TECLA = "switch_e2_tecla";

	public static final String PREF_SWITCH_J1_MORSE = "switch_j1_morse";
	public static final String PREF_SWITCH_J2_MORSE = "switch_j2_morse";
	public static final String PREF_SWITCH_J3_MORSE = "switch_j3_morse";
	public static final String PREF_SWITCH_J4_MORSE = "switch_j4_morse";
	public static final String PREF_SWITCH_E1_MORSE = "switch_e1_morse";
	public static final String PREF_SWITCH_E2_MORSE = "switch_e2_morse";

	private static HashMap<String,String[]> mSwitchMap;

	public SharedPreferences shared_prefs;
	public SharedPreferences.Editor prefs_editor;

	public Persistence(Context context) {
		is_ime_running = false;
		is_ime_showing = false;
		//		is_framework_ready = false;
		mScanDelay = 1000;

		sInstance = this;

		shared_prefs = PreferenceManager.getDefaultSharedPreferences(context);
		prefs_editor = shared_prefs.edit();

		mSwitchMap = new HashMap<String,String[]>();
	}

	public static Persistence getInstance() {
		return sInstance;
	}

	//	public void setFrameworkReady(Boolean is_ready) {
	//		is_framework_ready = is_ready;
	//	}

	//	public Boolean isFrameworkReady() {
	//		return is_framework_ready;
	//	}

	public int getScanDelay() {
		return mScanDelay;
	}

	public void setScanDelay(int delay) {
		mScanDelay = delay;
	}

	public void setIMERunning(boolean is_showing) {
		is_ime_running = is_showing;
	}

	public void setIMEShowing(boolean is_showing) {
		is_ime_showing = is_showing;
	}

	public boolean isIMEShowing() {
		return is_ime_showing;
	}

//	public void setHUDCancelled(boolean bool) {
//		is_hud_cancelled = bool;
//	}

//	public boolean isHUDCancelled() {
//		return is_hud_cancelled;
//	}

	public boolean isFullscreenEnabled() {
		return shared_prefs.getBoolean(PREF_FULLSCREEN_MODE, false);
	}

	public void setFullscreenEnabled(boolean value) {
		prefs_editor.putBoolean(PREF_FULLSCREEN_MODE, value);
		prefs_editor.commit();
	}

	// TODO: This method depends only on full-screen mode now, but will likely depend on other preferences later on
	public boolean shouldShowHUD() {
		return shared_prefs.getBoolean(PREF_FULLSCREEN_MODE, false);
	}

	//	public boolean shouldConnectToShield() {
	//		return shared_prefs.getBoolean(PREF_CONNECT_TO_SHIELD, false);
	//	}
	//	
	//	public void setConnectToShield(boolean shieldConnect) {
	//		prefs_editor.putBoolean(PREF_CONNECT_TO_SHIELD, shieldConnect);
	//		prefs_editor.commit();
	//	}
	//
	public boolean isSpeakerphoneEnabled() {
		return shared_prefs.getBoolean(PREF_SPEAKERPHONE_SWITCH, false);
	}

	public HashMap<String,String[]> getSwitchMap() {
		mSwitchMap.clear();
		mSwitchMap.put(PREF_SWITCH_J1,
				new String[]{shared_prefs.getString(PREF_SWITCH_J1_TECLA, "1"),
				shared_prefs.getString(PREF_SWITCH_J1_MORSE, "1")});
		mSwitchMap.put(PREF_SWITCH_J2,
				new String[]{shared_prefs.getString(PREF_SWITCH_J2_TECLA, "2"),
				shared_prefs.getString(PREF_SWITCH_J2_MORSE, "2")});
		mSwitchMap.put(PREF_SWITCH_J3,
				new String[]{shared_prefs.getString(PREF_SWITCH_J3_TECLA, "3"),
				shared_prefs.getString(PREF_SWITCH_J3_MORSE, "3")});
		mSwitchMap.put(PREF_SWITCH_J4,
				new String[]{shared_prefs.getString(PREF_SWITCH_J4_TECLA, "4"),
				shared_prefs.getString(PREF_SWITCH_J4_MORSE, "4")});
		mSwitchMap.put(PREF_SWITCH_E1,
				new String[]{shared_prefs.getString(PREF_SWITCH_E1_TECLA, "4"),
				shared_prefs.getString(PREF_SWITCH_E1_MORSE, "0")});
		mSwitchMap.put(PREF_SWITCH_E2,
				new String[]{shared_prefs.getString(PREF_SWITCH_E2_TECLA, "3"),
				shared_prefs.getString(PREF_SWITCH_E2_MORSE, "0")});
		return mSwitchMap;
	}

	public boolean isSelfScanningEnabled() {
		return shared_prefs.getBoolean(PREF_SELF_SCANNING, false);
	}

	public void setSelfScanningEnabled(boolean enabled) {
		prefs_editor.putBoolean(PREF_SELF_SCANNING, enabled);
		prefs_editor.commit();
	}

	public boolean isInverseScanningEnabled() {
		return shared_prefs.getBoolean(PREF_INVERSE_SCANNING, false);
	}

	public void setInverseScanningEnabled(boolean enabled) {
		prefs_editor.putBoolean(PREF_INVERSE_SCANNING, enabled);
		prefs_editor.commit();
	}

	//	public boolean isHUDRunning() {
	//		return shared_prefs.getBoolean(PREF_HUD, false);
	//	}
	//
	//	public void setHUDRunning(boolean enabled) {
	//		prefs_editor.putBoolean(PREF_HUD, enabled);
	//		prefs_editor.commit();
	//	}
	//
	//	public boolean isSingleSwitchOverlayEnabled() {
	//		return shared_prefs.getBoolean(PREF_SINGLESWITCH_OVERLAY, false);
	//	}
	//
	//	public void setSingleSwitchOverlayEnabled(boolean enabled) {
	//		prefs_editor.putBoolean(PREF_SINGLESWITCH_OVERLAY, enabled);
	//		prefs_editor.commit();
	//	}
	//
	//	public boolean isHUDSelfScanningEnabled() {
	//		return shared_prefs.getBoolean(PREF_HUD_SELF_SCANNING, false);
	//	}
	//
	//	public void setHUDSelfScanningEnabled(boolean enabled) {
	//		prefs_editor.putBoolean(PREF_HUD_SELF_SCANNING, enabled);
	//		prefs_editor.commit();
	//	}
	//
}
