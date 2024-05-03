package com.tekskills.er_tekskills.utils.GoogleMaps.utilities;

import com.google.android.libraries.places.api.model.AddressComponent;
import com.google.android.libraries.places.api.model.AddressComponents;
import com.google.android.libraries.places.api.model.Place;
import com.tekskills.er_tekskills.utils.UtilsConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Helper class to parse address components fetched from GooglePlacesAutocomplete API
 */
public class GooglePlaceAddressComponentsParser {
    /**
     * List of chosen fields to fetch from GooglePlacesAutocomplete aPI
     */
    static final List<String> ACCEPTED_TYPES = new ArrayList<String>() {{
        add(UtilsConstants.PlaceAddressComponentTypes.adminAreaLv1);
        add(UtilsConstants.PlaceAddressComponentTypes.adminAreaLv2);
        add(UtilsConstants.PlaceAddressComponentTypes.premise);
        add(UtilsConstants.PlaceAddressComponentTypes.streetNumber);
        add(UtilsConstants.PlaceAddressComponentTypes.route);
        add(UtilsConstants.PlaceAddressComponentTypes.country);
    }};

    /**
     * Loop through every address components type, return HashMap of address components breakdown
     * @param place
     * @return
     */
    public static HashMap<String, String> parseAddressComponents(Place place){
        AddressComponents addressComponents = place.getAddressComponents();
        HashMap<String, String> parsedAddressComponents = new HashMap<>();
        for (AddressComponent addressComponent :
                addressComponents.asList()) {
            for (String type: addressComponent.getTypes()){
                if (ACCEPTED_TYPES.contains(type)){
                    parsedAddressComponents.put(type, addressComponent.getName());
                }
            }
        }

        return parsedAddressComponents;
    }

}
