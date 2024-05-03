package com.tekskills.er_tekskills.utils.GoogleMaps

import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

/**
 * ClusterItem class
 */
class MyClusterItem(lat: Double, lng: Double, iconBitMapDescriptor: BitmapDescriptor) :
    ClusterItem {
    private val position: LatLng
    private val title: String
    private val snippet: String
    val iconBitMapDescriptor: BitmapDescriptor

    init {
        position = LatLng(lat, lng)
        title = "cc"
        snippet = "cl"
        //        this.title = site.getSiteName();
//        this.snippet = site.getSiteName();;
        this.iconBitMapDescriptor = iconBitMapDescriptor
    }

    override fun getPosition(): LatLng {
        return position
    }

    override fun getTitle(): String? {
        return title
    }

    override fun getSnippet(): String? {
        return snippet
    }
}
