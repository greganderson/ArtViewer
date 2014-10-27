package com.familybiz.greg.artviewer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
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

	public interface OnArtChangedListener {
		public void onArtChanged();
	}
	OnArtChangedListener mOnArtChangedListener = null;

	private ArtCollection() {
	}

	public OnArtChangedListener getOnArtChangedListener() {
		return mOnArtChangedListener;
	}

	public void setOnArtChangedListener(OnArtChangedListener onArtChangedListener) {
		mOnArtChangedListener = onArtChangedListener;
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
		if (mOnArtChangedListener != null)
			mOnArtChangedListener.onArtChanged();
	}

	public void removeArt(UUID identifier) {
		mArt.remove(identifier);
		if (mOnArtChangedListener != null)
			mOnArtChangedListener.onArtChanged();
	}

	public void scrapeArt(final String uriString) {
		AsyncTask<String, Integer, URI[]> imageScrapeTask = new AsyncTask<String, Integer, URI[]>() {
			@Override
			protected URI[] doInBackground(String... uriStrings) {
				if (uriStrings.length <= 0)
					return new URI[0];
				String uriString = uriStrings[0];

				String contentString = null;
				try {
					HttpClient client = new DefaultHttpClient();
					HttpGet request = new HttpGet(uriString);
					HttpResponse response = client.execute(request);

					InputStream contentStream = response.getEntity().getContent();
					Scanner contentScanner = new Scanner(contentStream).useDelimiter("\\A");
					contentString = contentScanner.hasNext() ? contentScanner.next() : null;
					Log.i("Network", contentString);


				}
				catch (IOException e) {
					e.printStackTrace();
				}

				if (contentString == null)
					return new URI[0];

				ArrayList<URI> imageUris = new ArrayList<URI>();

				String imageTagStart =  "<img src=\"";
				String imageTagEnd = "\"";
				while (true) {
					int imageTagStartIndex = contentString.indexOf(imageTagStart);
					if (imageTagStartIndex < 0)
						break;
					contentString = contentString.substring(imageTagStartIndex + imageTagStart.length());
					int imageTagEndIndex = contentString.indexOf(imageTagEnd);
					if (imageTagEndIndex < 0)
						break;
					String imageUriString = contentString.substring(0, imageTagEndIndex);
					contentString = contentString.substring(imageTagEndIndex);

					if (imageUriString.length() <= 0)
						continue;

					try {
						URI imageUri = new URI(imageUriString);
						imageUris.add(imageUri);
						publishProgress(imageUris.size());
					}
					catch (URISyntaxException e) { }
				}

				return imageUris.toArray(new URI[imageUris.size()]);
			}

			@Override
			protected void onProgressUpdate(Integer... values) {
				super.onProgressUpdate(values);
			}

			@Override
			protected void onPostExecute(URI[] uris) {
				super.onPostExecute(uris);

				AsyncTask<URI, Integer, Bitmap[]> bitmapDownloadTask = new AsyncTask<URI, Integer, Bitmap[]>() {
					@Override
					protected Bitmap[] doInBackground(URI... bitmapUris) {
						ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();
						for (URI uri : bitmapUris) {
							try {
								HttpClient client = new DefaultHttpClient();
								HttpGet request =  new HttpGet(uri);
								HttpResponse response = client.execute(request);

								InputStream content = response.getEntity().getContent();
								Bitmap bitmap = BitmapFactory.decodeStream(content);
								if (bitmap != null) {
									bitmaps.add(bitmap);
									publishProgress(bitmaps.size());
								}
							}
							catch (IOException e) {
								e.printStackTrace();
							}
						}

						return bitmaps.toArray(new Bitmap[bitmaps.size()]);
					}

					@Override
					protected void onProgressUpdate(Integer... values) {
						super.onProgressUpdate(values);
					}

					@Override
					protected void onPostExecute(Bitmap[] bitmaps) {
						super.onPostExecute(bitmaps);

						for (Bitmap bitmap : bitmaps) {
							Art art = new Art();
							art.name = "nytimes.com bitmap";
							art.image = bitmap;
							addArt(art);
						}
					}
				};

				bitmapDownloadTask.execute(uris);
			}
		};
		imageScrapeTask.execute(uriString);
	}
}
