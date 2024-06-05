package com.tekskills.geolocator.geofencer.models

import android.content.Context
import com.tekskills.geolocator.geofencer.service.GeoFenceBootInterface
import com.tekskills.geolocator.geofencer.service.GeoFenceUpdateInterface
import com.tekskills.geolocator.geofencer.service.GeoLocatorInterface
import com.tekskills.geolocator.geofencer.service.LocationTrackerUpdateInterface

abstract class CoreWorkerModule(context: Context) : GeoLocatorInterface
abstract class GeoFenceUpdateModule(context: Context) : GeoFenceUpdateInterface
abstract class GeoFenceBootModule(context: Context) : GeoFenceBootInterface
abstract class LocationTrackerUpdateModule(context: Context) : LocationTrackerUpdateInterface
