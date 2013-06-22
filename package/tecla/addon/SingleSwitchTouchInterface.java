package com.android.tecla.addon;

import ca.idrc.tecla.R;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import ca.idrc.tecla.framework.SimpleOverlay;
import ca.idrc.tecla.framework.TeclaStatic;
import ca.idi.tecla.sdk.*;

public class SingleSwitchTouchInterface extends SimpleOverlay {

	/**
	 * Tag used for logging in the whole framework
	 */
	public static final String CLASS_TAG = "SingleSwitchTouchInterface";
	private static SingleSwitchTouchInterface sInstance;
	
	private int mTouchDownCoordX, mTouchDownCoordY;

	public SingleSwitchTouchInterface(Context context) {
		super(context);

		final WindowManager.LayoutParams params = getParams();
		params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
		setParams(params);

		View rView = getRootView();
		rView.setBackgroundResource(R.drawable.screen_switch_background_normal);
		rView.setOnTouchListener(mOverlayTouchListener);
		
		/*if(!TeclaApp.persistence.isInverseScanningEnabled()) 
			setLongClick(true);*/
		
	}

	@Override
	protected void onShow() {
		sInstance = this;
	}

	@Override
	protected void onHide() {
		sInstance = null;
	}

	/**
	 * Listener for full-screen switch actions
	 */
	private View.OnTouchListener mOverlayTouchListener = new View.OnTouchListener() {
		
		public boolean onTouch(View v, MotionEvent event) {
			
			// get display resolution
			WindowManager wm = (WindowManager) sInstance.getContext().getSystemService(Context.WINDOW_SERVICE);
			Display display = wm.getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);
			int width = size.x;
			int height = size.y;
			
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				
				// use this method to disable HUD
				sInstance.mTouchDownCoordX = (int) event.getX();
				sInstance.mTouchDownCoordY = (int) event.getY();
				
				sInstance.getRootView().setBackgroundResource(R.drawable.screen_switch_background_pressed);
				TeclaApp.a11yservice.injectSwitchEvent(
						new SwitchEvent(SwitchEvent.MASK_SWITCH_E1, 0)); //Primary switch pressed
				// if (TeclaApp.DEBUG) Log.d(TeclaApp.TAG, CLASS_TAG + "Fullscreen switch down!");
				break;
			case MotionEvent.ACTION_UP:
				
				// use this method to disable HUD
				int coordX = (int) event.getX();
				int coordY = (int) event.getY();
				if(sInstance.mTouchDownCoordX < width/2
						&& coordX > width/2) {
					sInstance.getRootView().setBackgroundResource(R.drawable.screen_switch_background_normal);
					TeclaApp.persistence.setFullscreenEnabled(false);
					TeclaApp.getInstance().turnFullscreenOff();
					TeclaStatic.logV(CLASS_TAG, "Disabled HUD");
					return false;
				}
					
				
				sInstance.getRootView().setBackgroundResource(R.drawable.screen_switch_background_normal);
				TeclaApp.a11yservice.injectSwitchEvent(
						new SwitchEvent(0,0)); //Switches released
				// if (TeclaApp.DEBUG) Log.d(TeclaApp.TAG, CLASS_TAG + "Fullscreen switch up!");
				break;
			default:
				break;
			}
			return false;
		}
	};

	public void setLongClick(boolean enabled) {
		View rView = getRootView();
		if(enabled) rView.setOnLongClickListener(mOverlayLongClickListener);
		else rView.setOnLongClickListener(null);
	}
	
	private View.OnLongClickListener mOverlayLongClickListener =  new View.OnLongClickListener() {

		@Override
		public boolean onLongClick(View v) {
			TeclaStatic.logV(CLASS_TAG, "Long clicked.  ");
			sInstance.getRootView().setBackgroundResource(R.drawable.screen_switch_background_normal);
			TeclaApp.persistence.setFullscreenEnabled(false);
			TeclaApp.getInstance().turnFullscreenOff();
			return true;
		}
	};

}
