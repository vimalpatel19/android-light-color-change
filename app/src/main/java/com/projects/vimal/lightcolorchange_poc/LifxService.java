package com.projects.vimal.lightcolorchange_poc;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by vimal on 12/8/18.
 */

public interface LifxService {

    @PUT("lights/states")
    Call<Object> updateLights(@Header("Authorization") String authorization,
                         @Header("Content-Type") String contentType,
                         @Body GroupLight groupLight);

    @PUT("lights/{selector}/state")
    Call<Object> defaultLights(@Header("Authorization") String authorization,
                               @Header("Content-Type") String contentType,
                               @Path("selector") String selector,
                               @Body Light light);
}
