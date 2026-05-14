# Lost and Found Map Mobile App

This project extends the existing Task 7.1P Lost and Found app for SIT708 Pass Task 9.1. It keeps the Java/XML structure, the existing screens, and the SQLite database helper while adding map and location features.

## Google Maps API key setup

1. Create or open a Google Cloud project.
2. Enable these APIs for the Android app:
   - Maps SDK for Android
   - Places API (New)
3. Create an Android API key and restrict it to this app package if required.
4. Open `local.properties`.
5. Add your key:

```properties
googleMapsApiKey=YOUR_GOOGLE_MAPS_API_KEY_HERE
```

You can also set `GOOGLE_MAPS_API_KEY` in your environment before building.

Do not commit a real API key to a public repository.

## Implemented features

- Added latitude and longitude to the advert model and SQLite database.
- Added a safe database upgrade from the old advert table to the new location-aware table.
- Updated the Create Advert screen for Lost/Found type, name, phone, description, date, location, category, and optional image.
- Location field opens Google Places Autocomplete.
- Selected place address, latitude, and longitude are saved with the advert.
- Added Get Current Location button using `FusedLocationProviderClient`.
- Added runtime permission handling for fine and coarse location.
- Added Show On Map button on the home screen.
- Added a Google Map screen with markers for saved adverts.
- Marker title shows advert type and item name.
- Marker snippet shows short description and location.
- Added radius search in kilometres from the user's current location.
- Shows a Toast when no adverts are available for the selected map/radius result.

## Dependencies used

- Maps SDK for Android: `com.google.android.gms:play-services-maps`
- Places SDK for Android: `com.google.android.libraries.places:places`
- Fused Location Provider: `com.google.android.gms:play-services-location`

## Testing checklist

- Create a Lost advert using Places Autocomplete.
- Create a Found advert using Get Current Location.
- Open Show On Map and check that saved adverts appear as markers.
- Enter a radius in km and tap Nearby to filter markers around the current location.
- Deny location permission and confirm the app shows a safe message instead of crashing.

## Build

From the project root:

```bash
./gradlew assembleDebug
```
