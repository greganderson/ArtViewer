package com.familybiz.greg.artviewer;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.UUID;

public class ArtDetailFragment extends Fragment {

	ImageView mArtDetailView = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mArtDetailView = new ImageView(getActivity());
		return mArtDetailView;
	}

	public void setArtDetailIdentifier(UUID identifier) {
		if (mArtDetailView == null)
			return;

		Bitmap artResourceIdentifier = ArtCollection.getInstance().getArt(identifier).image;
		mArtDetailView.setImageBitmap(artResourceIdentifier);
	}
}
