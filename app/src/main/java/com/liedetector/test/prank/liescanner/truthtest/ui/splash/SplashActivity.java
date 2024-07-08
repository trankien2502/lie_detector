package com.liedetector.test.prank.liescanner.truthtest.ui.splash;

import static com.ads.sapp.util.GoogleMobileAdsConsentManager.getConsentResult;

import android.app.Application;
import android.os.Handler;
import android.view.View;

import com.ads.sapp.admob.Admob;
import com.ads.sapp.admob.AppOpenManager;
import com.ads.sapp.ads.CommonAd;
import com.ads.sapp.ads.CommonAdCallback;
import com.ads.sapp.ads.wrapper.ApAdError;
import com.ads.sapp.ads.wrapper.ApInterstitialAd;
import com.ads.sapp.funtion.AdCallback;
import com.ads.sapp.util.GoogleMobileAdsConsentManager;
import com.liedetector.test.prank.liescanner.truthtest.MyApplication;
import com.liedetector.test.prank.liescanner.truthtest.ads.AdsModel;
import com.liedetector.test.prank.liescanner.truthtest.ads.ApiService;
import com.liedetector.test.prank.liescanner.truthtest.ads.ConstantIdAds;
import com.liedetector.test.prank.liescanner.truthtest.ads.ConstantRemote;
import com.liedetector.test.prank.liescanner.truthtest.ads.IsNetWork;
import com.liedetector.test.prank.liescanner.truthtest.base.BaseActivity;
import com.liedetector.test.prank.liescanner.truthtest.databinding.ActivitySplashBinding;
import com.liedetector.test.prank.liescanner.truthtest.ui.language.LanguageStartActivity;
import com.liedetector.test.prank.liescanner.truthtest.util.EventTracking;
import com.liedetector.test.prank.liescanner.truthtest.util.SPUtils;
import com.liedetector.test.prank.liescanner.truthtest.util.SharePrefUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends BaseActivity<ActivitySplashBinding> {


    @Override
    public ActivitySplashBinding getBinding() {
        return ActivitySplashBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        SharePrefUtils.increaseCountOpenApp(this);
        AppOpenManager.getInstance().disableAppResumeWithActivity(SplashActivity.class);
        clearAllAds();
        callUMP();
        callRemoteConfig();
        EventTracking.logEvent(this,"splash_open");
    }

    private void clearAllAds() {
        ConstantIdAds.listIDAdsOpenSplash = new ArrayList<>();
        ConstantIdAds.listIDAdsInterSplash = new ArrayList<>();
        ConstantIdAds.listIDAdsNativeLanguage = new ArrayList<>();
        ConstantIdAds.listIDAdsInterIntro = new ArrayList<>();
        ConstantIdAds.listIDAdsNativeIntro = new ArrayList<>();
        ConstantIdAds.listIDAdsInterPermission = new ArrayList<>();
        ConstantIdAds.listIDAdsNativePermission = new ArrayList<>();
        ConstantIdAds.listIDAdsInterScanner = new ArrayList<>();
        ConstantIdAds.listIDAdsInterSound = new ArrayList<>();
        ConstantIdAds.listIDAdsNativeHome = new ArrayList<>();
        ConstantIdAds.listIDAdsBannerCollapsible = new ArrayList<>();
        ConstantIdAds.listIDAdsBanner= new ArrayList<>();
        ConstantIdAds.listIDAdsResume= new ArrayList<>();

        ConstantIdAds.listIDAdsInterAll = new ArrayList<>();
    }

    private void callUMP() {
        if (IsNetWork.haveNetworkConnection(this)) {
            switch (SharePrefUtils.getInt(SPUtils.CONSENT_CHECK, 0)) {
                case 0:
                    callConsent();
                    break;
                case 1:
                    Application application = getApplication();
                    ((MyApplication) application).initAds();
                    new Handler().postDelayed(this::callApi, 1500);
                    break;
                case 2:
                    new Handler().postDelayed(this::startNextActivity, 3000);
                    break;
            }
        } else {
            new Handler().postDelayed(this::startNextActivity, 3000);
        }

    }

    private void callRemoteConfig() {
        ConstantRemote.initRemoteConfig(task -> {
            if (task.isSuccessful()) {
                ConstantRemote.open_splash = ConstantRemote.getRemoteConfigBoolean("open_splash");
                ConstantRemote.show_inter_all = ConstantRemote.getRemoteConfigBoolean("show_inter_all");
                ConstantRemote.inter_splash = ConstantRemote.getRemoteConfigBoolean("inter_splash");
                ConstantRemote.native_language =  ConstantRemote.getRemoteConfigBoolean("native_language");
                ConstantRemote.inter_intro = ConstantRemote.getRemoteConfigBoolean("inter_intro");
                ConstantRemote.native_intro = ConstantRemote.getRemoteConfigBoolean("native_intro");
                ConstantRemote.inter_permission = ConstantRemote.getRemoteConfigBoolean("inter_permission");
                ConstantRemote.native_permission = ConstantRemote.getRemoteConfigBoolean("native_permission");
                ConstantRemote.inter_scanner = ConstantRemote.getRemoteConfigBoolean("inter_scanner");
                ConstantRemote.inter_sound = ConstantRemote.getRemoteConfigBoolean("inter_sound");
                ConstantRemote.native_home = ConstantRemote.getRemoteConfigBoolean("native_home");
                ConstantRemote.banner_collapsible = ConstantRemote.getRemoteConfigBoolean("banner_collapsible");
                ConstantRemote.banner = ConstantRemote.getRemoteConfigBoolean("banner");
                ConstantRemote.resume = ConstantRemote.getRemoteConfigBoolean("resume");

                ConstantRemote.inter_all = ConstantRemote.getRemoteConfigString("inter_all");
                ConstantRemote.rate_aoa_inter_splash = ConstantRemote.getRemoteConfigOpenSplash("rate_aoa_inter_splash");
            }
        });

    }
    private void callConsent() {
        GoogleMobileAdsConsentManager googleMobileAdsConsentManager = GoogleMobileAdsConsentManager.getInstance(getApplicationContext());
        googleMobileAdsConsentManager.setSetTagForUnderAge(false);
        googleMobileAdsConsentManager.gatherConsent(this, complete -> {
            if (complete && googleMobileAdsConsentManager.canRequestAds()) {
                Application application = getApplication();
                ((MyApplication) application).initAds();
            }

            if (getConsentResult(this)) {
                SharePrefUtils.putInt(SPUtils.CONSENT_CHECK, 1);
            } else {
                SharePrefUtils.putInt(SPUtils.CONSENT_CHECK, 2);
            }

            if (getConsentResult(this) && googleMobileAdsConsentManager.canRequestAds()) {
                callApi();
            } else {
                new Handler().postDelayed(this::startNextActivity, 1500);
            }
        });
    }
    private void callApi() {

        if (IsNetWork.haveNetworkConnectionUMP(this)) {
            try {
                ApiService.apiService.callAdsSplash().enqueue(new Callback<List<AdsModel>>() {
                    @Override
                    public void onResponse(Call<List<AdsModel>> call, Response<List<AdsModel>> response) {
                        if (response.body() != null) {
                            List<AdsModel> adsModelList = response.body();
                            if (!adsModelList.isEmpty()) {
                                for (AdsModel ads : response.body()) {
                                    switch (ads.getName()) {
                                        case "open_splash":
                                            ConstantIdAds.listIDAdsOpenSplash.add(ads.getAds_id());
                                            break;
                                        case "inter_splash":
                                            ConstantIdAds.listIDAdsInterSplash.add(ads.getAds_id());
                                            break;
                                        case "show_inter_all":
                                            ConstantIdAds.listIDAdsInterAll.add(ads.getAds_id());
                                            break;
                                        case "native_language":
                                            ConstantIdAds.listIDAdsNativeLanguage.add(ads.getAds_id());
                                            break;
                                        case "inter_intro":
                                            ConstantIdAds.listIDAdsInterIntro.add(ads.getAds_id());
                                            break;
                                        case "native_intro":
                                            ConstantIdAds.listIDAdsNativeIntro.add(ads.getAds_id());
                                            break;
                                        case "inter_permission":
                                            ConstantIdAds.listIDAdsInterPermission.add(ads.getAds_id());
                                            break;
                                        case "native_permission":
                                            ConstantIdAds.listIDAdsNativePermission.add(ads.getAds_id());
                                            break;
                                        case "inter_scanner":
                                            ConstantIdAds.listIDAdsInterScanner.add(ads.getAds_id());
                                            break;
                                        case "inter_sound":
                                            ConstantIdAds.listIDAdsInterSound.add(ads.getAds_id());
                                            break;
                                        case "native_home":
                                            ConstantIdAds.listIDAdsNativeHome.add(ads.getAds_id());
                                            break;
                                        case "banner_collapsible":
                                            ConstantIdAds.listIDAdsBannerCollapsible.add(ads.getAds_id());
                                            break;
                                        case "banner":
                                            ConstantIdAds.listIDAdsBanner.add(ads.getAds_id());
                                            break;
                                        case "resume":
                                            ConstantIdAds.listIDAdsResume.add(ads.getAds_id());
                                            break;


                                    }
                                }
                                if (IsNetWork.haveNetworkConnectionUMP(SplashActivity.this) && ConstantIdAds.listIDAdsBanner.size() != 0 && ConstantRemote.banner) {
                                    Admob.getInstance().loadBannerFloor(SplashActivity.this, ConstantIdAds.listIDAdsBanner);
                                    binding.rlBanner.setVisibility(View.VISIBLE);
                                } else {
                                    binding.rlBanner.setVisibility(View.GONE);
                                }

                                new Handler().postDelayed(() -> {
                                    if (isShowAppOpenOrInter()) {
                                        showAppOpenSplash();
                                    } else {
                                        showAppInterSplash();
                                    }
                                }, 3000);
                            } else {
                                new Handler().postDelayed(() -> startNextActivity(), 3000);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<AdsModel>> call, Throwable t) {
                        new Handler().postDelayed(() -> startNextActivity(), 3000);
                    }
                });
            } catch (Exception e) {
                binding.rlBanner.setVisibility(View.GONE);
                new Handler().postDelayed(this::startNextActivity, 3000);
            }
        } else {
            binding.rlBanner.setVisibility(View.GONE);
            new Handler().postDelayed(this::startNextActivity, 3000);
        }
    }
    private boolean isShowAppOpenOrInter() {
        try {
            int appOpenRate = Integer.parseInt(ConstantRemote.rate_aoa_inter_splash.get(0));
            int random = new Random().nextInt(99) + 1;
            return random <= appOpenRate;
        } catch (Exception e) {
            return true;
        }
    }
    private void showAppOpenSplash() {
        if (IsNetWork.haveNetworkConnectionUMP(this) && ConstantIdAds.listIDAdsOpenSplash.size() != 0 && ConstantRemote.open_splash) {
            AdCallback adCallback = new AdCallback() {
                @Override
                public void onNextAction() {
                    super.onNextAction();
                    startNextActivity();
                }
            };
            AppOpenManager.getInstance().loadOpenAppAdSplashFloor(SplashActivity.this, ConstantIdAds.listIDAdsOpenSplash, true, adCallback);
        } else {
            startNextActivity();
        }
    }
    private void showAppInterSplash() {
        if (IsNetWork.haveNetworkConnectionUMP(this) && ConstantIdAds.listIDAdsInterSplash.size() != 0 && ConstantRemote.inter_splash) {
            CommonAd.getInstance().loadSplashInterstitialAds(SplashActivity.this, ConstantIdAds.listIDAdsInterSplash, 15000, 3500, new CommonAdCallback() {
                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                    startNextActivity();
                }

                @Override
                public void onAdFailedToLoad(ApAdError adError) {
                    super.onAdFailedToLoad(adError);
                    startNextActivity();
                }

                @Override
                public void onAdFailedToShow(ApAdError adError) {
                    super.onAdFailedToShow(adError);
                    startNextActivity();
                }
            });
        } else {
            startNextActivity();
        }
    }








    @Override
    public void bindView() {

    }

    private void startNextActivity() {
        startNextActivity(LanguageStartActivity.class, null);
        finishAffinity();
    }

    @Override
    public void onBackPressed() {
    }
}
