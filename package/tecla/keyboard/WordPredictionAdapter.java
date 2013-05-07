package com.android.tecla.keyboard;

import com.android.inputmethod.latin.suggestions.SuggestionsView;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class WordPredictionAdapter {

	private static final int[] SUGGESTIONSVIEWINDICES = {0,2,4};
	private static final int BACKGROUND_HIGHLIGHT_COLOR = Color.BLUE;
	private static final int BACKGROUND_NORMAL_COLOR = Color.DKGRAY;
	public static final String tag = "WordPredictionAdapter";
	
	private static ViewGroup sSuggestionsViewGroup = null;
	private static SuggestionsView sSuggestionsView = null;

	public static void setSuggestionsViewGroup(ViewGroup vg) {
		sSuggestionsViewGroup = vg;
	}
	
	public static void setSuggestionsView(SuggestionsView sv) {
		sSuggestionsView = sv;
	}
	
	public static boolean selectHighlighted() {
		if(sSuggestionsViewGroup == null) return false;
		if(sSuggestionsViewGroup.getVisibility() != ViewGroup.VISIBLE) return false;
		WordPredictionStates.click();
		return WordPredictionStates.sState != WordPredictionStates.WPSCAN_NONE;
	}

	public static boolean highlightNext() {
		if(sSuggestionsViewGroup == null) return false;
		if(sSuggestionsViewGroup.getVisibility() != ViewGroup.VISIBLE) return false;
		WordPredictionStates.scanNext();
		if(WordPredictionStates.sState == WordPredictionStates.WPSCAN_NONE) return false;
		else return true;
	}

	public static void highlightPrevious() {
		if(sSuggestionsViewGroup == null) return;
		if(sSuggestionsViewGroup.getVisibility() != ViewGroup.VISIBLE) return;
		WordPredictionStates.scanPrevious();
	}
	
	private static void highlightSuggestion(int index, boolean highlight) {
		if(index < 0 || index >= SUGGESTIONSVIEWINDICES.length) return;
		View view = sSuggestionsViewGroup.getChildAt(SUGGESTIONSVIEWINDICES[index]);
		if(highlight) {
			view.setBackgroundColor(BACKGROUND_HIGHLIGHT_COLOR);
		} else {
			view.setBackgroundColor(BACKGROUND_NORMAL_COLOR);
		}
	}
	
	private static void invalidateKeys() {
		sSuggestionsViewGroup.invalidate();
	}
	
	private static class WordPredictionStates {
		private static final int WPSCAN_NONE = 0x66;
		private static final int WPSCAN_HIGHLIGHTED = 0x77;
		private static final int WPSCAN_SUGGESTIONS = 0x88;
		private static final int WPSCAN_MORESUGGESTIONS = 0x99;
		private static final int WPSCAN_CLICK = 0xaa;
		private static int sState = WPSCAN_NONE;
		
		private static int sCurrentIndex = -1;
		
		private static void reset() {
			sState = WPSCAN_HIGHLIGHTED;
			sCurrentIndex = -1;
		}
		
		private static void scanNext() {
			switch(sState) {
			case(WPSCAN_NONE):		highlightSuggestion(0, true);
									highlightSuggestion(1, true);
									highlightSuggestion(2, true);
									sState = WPSCAN_HIGHLIGHTED;
									break;
			case(WPSCAN_HIGHLIGHTED):		highlightSuggestion(0, false);
											highlightSuggestion(1, false);
											highlightSuggestion(2, false);
											sState = WPSCAN_NONE;
											break;
			case(WPSCAN_SUGGESTIONS):		if(sCurrentIndex == SUGGESTIONSVIEWINDICES.length) {
												highlightSuggestion(0, false);
												highlightSuggestion(1, false);
												highlightSuggestion(2, false);
											}
											highlightSuggestion(sCurrentIndex++, false);
											sCurrentIndex %= SUGGESTIONSVIEWINDICES.length + 2;
											if(sCurrentIndex == SUGGESTIONSVIEWINDICES.length) {
												highlightSuggestion(0, true);
												highlightSuggestion(1, true);
												highlightSuggestion(2, true);
												break;
											}
											highlightSuggestion(sCurrentIndex, true);
											break;
			case(WPSCAN_MORESUGGESTIONS):	if(sCurrentIndex < 18)
												sSuggestionsView.mMoreSuggestionsView.getKeyboard().mKeys[sCurrentIndex].onReleased();
											else
												sCurrentIndex = 2;
											++sCurrentIndex;
											if(sCurrentIndex < 18)
												sSuggestionsView.mMoreSuggestionsView.getKeyboard().mKeys[sCurrentIndex].onPressed();
											sSuggestionsView.mMoreSuggestionsView.invalidateAllKeys();
											break;
			case(WPSCAN_CLICK):		highlightSuggestion(sCurrentIndex, false);
									sState = WPSCAN_NONE;
									break;
			default:				break;
			}
			invalidateKeys();
		}
		
		private static void scanPrevious() {
			switch(sState) {
			case(WPSCAN_NONE):		highlightSuggestion(0, true);
									highlightSuggestion(1, true);
									highlightSuggestion(2, true);
									sState = WPSCAN_HIGHLIGHTED;
									break;
			case(WPSCAN_HIGHLIGHTED):		highlightSuggestion(0, false);
											highlightSuggestion(1, false);
											highlightSuggestion(2, false);
											sState = WPSCAN_NONE;
											break;
			case(WPSCAN_SUGGESTIONS):		highlightSuggestion(sCurrentIndex--, false);
											if (sCurrentIndex < 0)
												sCurrentIndex = SUGGESTIONSVIEWINDICES.length;
											highlightSuggestion(sCurrentIndex, true);
			default:						break;
			}
		}
		
		private static void click() {
			switch(sState) {
			case(WPSCAN_NONE):				break;
			case(WPSCAN_HIGHLIGHTED):		highlightSuggestion(0, false);
											highlightSuggestion(1, false);
											highlightSuggestion(2, false);
											sState = WPSCAN_SUGGESTIONS;
											sCurrentIndex = 0;
											highlightSuggestion(sCurrentIndex, true);
											invalidateKeys();
											break;
			case(WPSCAN_SUGGESTIONS):		if(sCurrentIndex == SUGGESTIONSVIEWINDICES.length) {
												highlightSuggestion(0, false);
												highlightSuggestion(1, false);
												highlightSuggestion(2, false);
												sSuggestionsView.onLongClick(null);
												sSuggestionsView.mMoreSuggestionsView.getKeyboard().mKeys[sCurrentIndex].onPressed();
												sSuggestionsView.mMoreSuggestionsView.invalidateAllKeys();
												sState = WPSCAN_MORESUGGESTIONS;
											} else {
												highlightSuggestion(sCurrentIndex, false);
												View view = sSuggestionsViewGroup.getChildAt(SUGGESTIONSVIEWINDICES[sCurrentIndex]);
												view.callOnClick();
												sState = WPSCAN_NONE;												
											}
											invalidateKeys();
											break;
			case(WPSCAN_MORESUGGESTIONS):	if(sCurrentIndex < 18) {
												sSuggestionsView.mMoreSuggestionsView.getKeyboard().mKeys[sCurrentIndex].onReleased();
												sSuggestionsView.mMoreSuggestionsView.invalidateAllKeys();
												// select word here
											}
											sState = WPSCAN_NONE;
											break;
			default:						break;
			}
		}
		
		
	}
}
