package org.tetawex.ecf;

import android.os.Bundle;

import android.util.Log;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import org.tetawex.ecf.core.ActionResolver;
import org.tetawex.ecf.core.ECFGame;

public class AndroidLauncher extends AndroidApplication implements ActionResolver {
    private InterstitialAd mInterstitialAd;
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        MobileAds.initialize(this, "pub-2665687280252523");
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useImmersiveMode=true;
		initialize(new ECFGame(this), config);
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-2665687280252523/4628518690");
	}

	@Override
	public void loadAd() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AdRequest request = new AdRequest.Builder()
                        .addTestDevice("34EE2F5C299894B9A8E5B6DDC867AE74")
                        .build();
                mInterstitialAd.loadAd(request);
            }
        });
	}

	@Override
	public void showAd() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("ECF", "The interstitial wasn't loaded yet.");
                }
            }
        });
	}
}
