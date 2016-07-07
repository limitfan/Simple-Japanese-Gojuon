/*
 * Copyright (c) 2011, GuoheAd Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'GuoheAd Inc.' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.guohead.sdk.adapters;

import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdRequest.ErrorCode;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.guohead.sdk.BaseAdapter;
import com.guohead.sdk.GHView;
import com.guohead.sdk.utils.Utils;

public class AdmobAdapter extends BaseAdapter implements AdListener {

	private AdView mAdView;

	public AdmobAdapter(GHView view, String params) {
		super(view, params, "Admob");
	}
	@Override
	public void loadAd() {
		super.loadAd();
		if (mAdView == null) {
			mAdView = new com.google.ads.AdView(mGHView.getActivity(),
					AdSize.BANNER, keys[0].trim());
		}
		mAdView.setId(Utils.TYPE_ADMOB);
		mAdView.setAdListener(this);
		AdRequest request = new AdRequest();
//		request.addTestDevice("你自己的设备id");
		
		mAdView.loadAd(request);
		addView(mAdView);
		mGHView.postInvalidate();
	}

	@Override
	public void invalidate() {
		super.invalidate();
	}

	@Override
	public void onDismissScreen(Ad ad) {
	}

	@Override
	public void onFailedToReceiveAd(Ad ad, ErrorCode error) {
		mAdView.setAdListener(null);
		failedReceiveAd(mAdView);
	}

	@Override
	public void onLeaveApplication(Ad ad) {
	}

	@Override
	public void onPresentScreen(Ad ad) {
	}

	@Override
	public void onReceiveAd(Ad ad) {
		mAdView.setAdListener(null);
		receiveAd(mAdView);
	}

	@Override
	public void onClick() {
		// should do nothing
	}
}
