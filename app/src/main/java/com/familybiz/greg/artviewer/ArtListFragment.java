package com.familybiz.greg.artviewer;

import android.app.Fragment;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Set;
import java.util.UUID;

public class ArtListFragment extends Fragment implements ListAdapter {

	UUID[] mArtIdentifiersByName = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final ListView artListView = new ListView(getActivity());
		artListView.setAdapter(this);

		artListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				UUID artIdentifier = mArtIdentifiersByName[i];

				if (mOnArtSelectedListener != null)
					mOnArtSelectedListener.onArtSelected(ArtListFragment.this, artIdentifier);
			}
		});

		Set<UUID> artIdentifiers = ArtCollection.getInstance().getIdentifiers();
		// TODO: Oder list
		mArtIdentifiersByName = artIdentifiers.toArray(new UUID[artIdentifiers.size()]);
		ArtCollection.getInstance().setOnArtChangedListener(new ArtCollection.OnArtChangedListener() {
			@Override
			public void onArtChanged() {
				Set<UUID> artIdentifiers = ArtCollection.getInstance().getIdentifiers();
				// TODO: Oder list
				mArtIdentifiersByName = artIdentifiers.toArray(new UUID[artIdentifiers.size()]);
				artListView.invalidateViews();
			}
		});

		return artListView;
	}

	public interface OnArtSelectedListener {
		public void onArtSelected(ArtListFragment artListFragment, UUID identifier);
	}
	OnArtSelectedListener mOnArtSelectedListener = null;

	public void setOnArtSelectedListener(OnArtSelectedListener listener) {
		mOnArtSelectedListener = listener;
	}

	public OnArtSelectedListener getOnArtSelectedListener() {
		return mOnArtSelectedListener;
	}

	@Override
	public boolean isEmpty() {
		return getCount() <= 0;
	}

	@Override
	public int getCount() {
		if (mArtIdentifiersByName == null) {
			Set<UUID> artIdentifiers = ArtCollection.getInstance().getIdentifiers();
			// TODO: Order list
			mArtIdentifiersByName = artIdentifiers.toArray(new UUID[artIdentifiers.size()]);
		}
		return ArtCollection.getInstance().getIdentifiers().size();
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public Object getItem(int i) {
		return i;
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public int getItemViewType(int i) {
		return 0;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		UUID artIdentifier = mArtIdentifiersByName[(int)getItemId(i)];
		Art art = ArtCollection.getInstance().getArt(artIdentifier);

		TextView artTitleView = new TextView(getActivity());
		artTitleView.setText(art.name);

		return artTitleView;
	}

	@Override
	public boolean areAllItemsEnabled() {
		return true;
	}

	@Override
	public boolean isEnabled(int i) {
		return true;
	}

	@Override
	public void registerDataSetObserver(DataSetObserver dataSetObserver) { }

	@Override
	public void unregisterDataSetObserver(DataSetObserver dataSetObserver) { }
}
