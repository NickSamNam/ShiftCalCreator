package com.nicknam.shiftcalcreator;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

/**
 * Created by snick on 22-7-2017.
 */

public class AboutActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Element tutorialElement = new Element()
                .setTitle(getString(R.string.showTutorial))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setResult(Activity.RESULT_OK, new Intent().putExtra("tut", true));
                        finish();
                    }
                });
        Element versionElement = new Element()
                .setTitle(getString(R.string.version) + " " + BuildConfig.VERSION_NAME);
        Element iconElement = new Element()
                .setTitle(getString(R.string.icBy) + " " + getString(R.string.ic_website_name))
                .setIntent(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.ic_website))));
        AboutPage aboutPage = new AboutPage(this)
                .setDescription(getString(R.string.description))
                .isRTL(false)
                .addItem(tutorialElement)
                .addGroup(getString(R.string.about))
                .addItem(versionElement)
                .addItem(iconElement)
                .addGitHub(getString(R.string.github_id), getString(R.string.checkoutGithub))
                .addPlayStore(BuildConfig.APPLICATION_ID, getString(R.string.leaveRating));

        setContentView(aboutPage.create());
    }
}
