package ca.idrc.tecla.highlighter;

import java.util.ArrayList;

import ca.idrc.tecla.R;
import ca.idrc.tecla.framework.SimpleOverlay;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityNodeInfo;

public class TeclaHighlighter extends SimpleOverlay {

    private static TeclaHighlighter sInstance;

    private final HighlightBoundsView mInnerBounds;
    private final HighlightBoundsView mOuterBounds;
    private final HighlightBoundsView mOtherBounds;
    
    private ArrayList<AccessibilityNodeInfo> mNodes;
    
	public TeclaHighlighter(Context context) {
		super(context);
		final WindowManager.LayoutParams params = getParams();
		params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
		params.flags |= WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
		params.flags |= WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
		params.flags |= WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD;
		params.flags |= WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;
		setParams(params);
		
		setContentView(R.layout.tecla_highlighter);

		mInnerBounds = (HighlightBoundsView) findViewById(R.id.announce_bounds);
//		mAnnounceBounds.setHighlightColor(Color.argb(0xff, 0x21, 0xad, 0xe3));
		mInnerBounds.setHighlightColor(Color.WHITE);
		
		
		mOuterBounds = (HighlightBoundsView) findViewById(R.id.bounds);
		mOuterBounds.setHighlightColor(Color.argb(0xdd, 0x38, 0x38, 0x38));

		
		mOtherBounds = (HighlightBoundsView) findViewById(R.id.otherBounds);
		mOtherBounds.setHighlightColor(Color.argb(0xdd, 0x38, 0x38, 0x38));
		
	}

	
	@Override
	protected void onShow() {
		sInstance = this;
	}

	@Override
	protected void onHide() {
        sInstance = null;
        mOuterBounds.clear();
        mInnerBounds.clear();
	}
	

	public void clearHighlight() {
        mOuterBounds.clear();
        mInnerBounds.clear();
        mOuterBounds.postInvalidate();
        mInnerBounds.postInvalidate();
	}
	
    public void removeInvalidNodes() {

        mOuterBounds.removeInvalidNodes();
        mOuterBounds.postInvalidate();

        mInnerBounds.removeInvalidNodes();
        mInnerBounds.postInvalidate();
    }

    public static void highlightNode(AccessibilityNodeInfo node, ArrayList<AccessibilityNodeInfo> otherNodes) {
        if (sInstance == null) {
            return;
        }

        sInstance.clearHighlight();
        if(node != null) {
            sInstance.mOuterBounds.setStrokeWidth(20);
            sInstance.mOuterBounds.add(node);
            sInstance.mOuterBounds.postInvalidate();        	
            sInstance.mInnerBounds.setStrokeWidth(6);
            sInstance.mInnerBounds.add(node);
            sInstance.mInnerBounds.postInvalidate();
        	
        }
        
        sInstance.h.removeMessages(0);
        sInstance.mNodes = otherNodes;
        
        Message msg = new Message();
        msg.what=0;
        sInstance.h.sendMessage(msg);
        
        
    }
    
    private Handler h = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			
			AccessibilityNodeInfo node = mNodes.remove(0);
			mOtherBounds.clear();
			mOtherBounds.setStrokeWidth(4);
			mOtherBounds.add(node);
            mOtherBounds.postInvalidate();			
			mNodes.add(node);
			mInnerBounds.postInvalidate();
			super.handleMessage(msg);
			Message msg1 = new Message();
			msg1.what=0;
			h.sendMessageDelayed(msg1, 500);
		}
    	
    };
    
}
