package com.familybiz.greg.artviewer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class ArtCollection {

	Map<UUID, Art> mArt = new HashMap<UUID, Art>();

	static ArtCollection mInstance = null;

	static ArtCollection getInstance() {
		if (mInstance == null)
			mInstance = new ArtCollection();
		return mInstance;
	}

	private ArtCollection() {
	}

	public Set<UUID> getIdentifiers() {
		return mArt.keySet();
	}

	public Art getArt(UUID identifier) {
		return mArt.get(identifier);
	}

	public void addArt(Art art) {
		UUID uuid = UUID.randomUUID();
		art.identifier = uuid;
		mArt.put(uuid, art);
	}

	public void removeArt(UUID identifier) {
		mArt.remove(identifier);
	}
}
