package com.familybiz.greg.artviewer;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.UUID;


public class ArtActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

	    // Add test info
	    Art mona = new Art();
	    mona.name = "Mona Lisa";
	    mona.image = R.drawable.mona;
	    ArtCollection.getInstance().addArt(mona);

	    Art awesome = new Art();
	    awesome.name = "Stiff Arm";
	    awesome.image = R.drawable.awesome;
	    ArtCollection.getInstance().addArt(awesome);

	    Art clocks = new Art();
	    clocks.name = "Clocks";
	    clocks.image = R.drawable.clocks;
	    ArtCollection.getInstance().addArt(clocks);

	    Art farm = new Art();
	    farm.name = "Farmland";
	    farm.image = R.drawable.farm;
	    ArtCollection.getInstance().addArt(farm);

	    LinearLayout rootLayout = new LinearLayout(this);
	    rootLayout.setOrientation(LinearLayout.HORIZONTAL);
        setContentView(rootLayout);

	    FrameLayout artListLayout = new FrameLayout(this);
	    artListLayout.setId(10);
	    rootLayout.addView(artListLayout, new LinearLayout.LayoutParams(
			    0, ViewGroup.LayoutParams.MATCH_PARENT, 30));

	    FrameLayout artDetailLayout = new FrameLayout(this);
	    artDetailLayout.setId(11);
	    rootLayout.addView(artDetailLayout, new LinearLayout.LayoutParams(
			    0, ViewGroup.LayoutParams.MATCH_PARENT, 70));

	    ArtListFragment artListFragment = new ArtListFragment();
	    final ArtDetailFragment artDetailFragment = new ArtDetailFragment();

	    FragmentTransaction addTransaction = getFragmentManager().beginTransaction();
	    addTransaction.add(10, artListFragment);
	    addTransaction.add(11, artDetailFragment);
	    addTransaction.commit();

	    artListFragment.setOnArtSelectedListener(new ArtListFragment.OnArtSelectedListener() {
		    @Override
		    public void onArtSelected(ArtListFragment artListFragment, UUID identifier) {
			    // Update detail fragment
				artDetailFragment.setArtDetailIdentifier(identifier);
		    }
	    });
    }
}
