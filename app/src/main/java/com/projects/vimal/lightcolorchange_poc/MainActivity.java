package com.projects.vimal.lightcolorchange_poc;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private Timer timer;
    private ToggleButton toggleButton;
    private final String LIFX_URL = "https://api.lifx.com/v1/";
    private final String AUTH_KEY = "TODO: Add LIFX Auth Key";
    private final String CONTENT_TYPE = "application/json";
    private final String LIVING_ROOM = "group:Living Room";
    private final String LIVING_ROOM_1 = "TODO: Add LIFX Light ID";
    private final String LIVING_ROOM_2 = "TODO: Add LIFX Light ID";
    private final String LIVING_ROOM_3 = "TODO: Add LIFX Light ID";
    private final String LIVING_ROOM_4 = "TODO: Add LIFX Light ID";
    private final String DEFAULT_COLOR = "hue:0 saturation:0 brightness:0.5";

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(LIFX_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    LifxService service = retrofit.create(LifxService.class);

    Gson gson = new Gson();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toggle Button to turn on random display of images
        toggleButton = (ToggleButton) findViewById(R.id.toggleButton);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    startTimer();

                } else {
                    timer.cancel();
                    //timer.purge();
                    clearText();
                    stopDisplayingImages();
                    callServiceForDefaultLights();
                }
            }
        });
    }

    public Bitmap displayNewImage() {
        int[] images = {R.drawable.img1, R.drawable.img2, R.drawable.img3, R.drawable.img4,
                R.drawable.img5, R.drawable.img6, R.drawable.img7, R.drawable.img8, R.drawable.img9,
                R.drawable.img10, R.drawable.img11, R.drawable.img12, R.drawable.img13,
                R.drawable.img14, R.drawable.img15};

        Random rand = new Random();
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageResource(images[rand.nextInt(images.length)]);

        Bitmap decodedImage = decodeSampledBitmapFromResource(getResources(),
                images[rand.nextInt(images.length)],
                200,
                300);

        imageView.setImageBitmap(decodedImage);

        return decodedImage;
    }

    public void stopDisplayingImages() {
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.img0, 200, 300));
    }

    public void displayColors(Map<String, Palette.Swatch> colors) {
        TextView textView = (TextView) findViewById(R.id.textView);

        String displayString = "";

        for (Map.Entry<String, Palette.Swatch> color : colors.entrySet()) {

            if (color.getValue() != null) {
                displayString += color.getKey() + ": " + color.getValue().getTitleTextColor() + "\n";
            }
        }

        textView.setText(displayString);
    }

    public void clearText() {
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText("");
    }

    public Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                  int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public void startTimer() {
        timer = new Timer();
        TimerTask timerTask = new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Bitmap newImage = displayNewImage();

                        Map<String, Palette.Swatch> colors = getProminentColors(newImage);

                        displayColors(colors);

                        changeLights(colors);
                    }
                });
            }
        };
        timer.schedule(timerTask, 0, 7000);
    }

    public Map<String, Palette.Swatch> getProminentColors(Bitmap image) {
        Map<String, Palette.Swatch> colors = new HashMap<String, Palette.Swatch>();
        Palette palette = Palette.from(image).generate();

        Palette.Swatch lightVibrant = palette.getLightVibrantSwatch();
        colors.put("Light Vibrant", lightVibrant);

        Palette.Swatch vibrant = palette.getVibrantSwatch();
        colors.put("Vibrant", vibrant);

        Palette.Swatch darkVibrant = palette.getDarkVibrantSwatch();
        colors.put("Dark Vibrant", darkVibrant);

        Palette.Swatch lightMuted = palette.getLightMutedSwatch();
        colors.put("Light Muted", lightMuted);

        Palette.Swatch muted = palette.getMutedSwatch();
        colors.put("Muted", muted);

        Palette.Swatch darkMuted = palette.getDarkMutedSwatch();
        colors.put("Dark Muted", darkMuted);

        return colors;
    }

    public void changeLights(Map<String, Palette.Swatch> colors) {
        GroupLight groupLight = null;
        List<Light> lights = new ArrayList<Light>();
        Light light1 = new Light(LIVING_ROOM_1);
        Light light2 = new Light(LIVING_ROOM_2);
        Light light3 = new Light(LIVING_ROOM_3);
        Light light4 = new Light(LIVING_ROOM_4);


        if (colors.get("Dark Vibrant") != null) {
            light1.setColor(getLightColors(colors.get("Dark Vibrant")));
        }
        else {
            light1.setColor(getLightColors(colors.get("Vibrant")));
        }

        if (colors.get("Dark Muted") != null) {
            light4.setColor(getLightColors(colors.get("Dark Muted")));
        }
        else {
            light4.setColor(getLightColors(colors.get("Muted")));
        }

        if (colors.get("Light Vibrant") != null) {
            light2.setColor(getLightColors(colors.get("Light Vibrant")));
        } else {
            light2.setColor(getLightColors(colors.get("Vibrant")));
        }

        if (colors.get("Light Muted") != null) {
            light3.setColor(getLightColors(colors.get("Light Muted")));
        } else {
            light3.setColor(getLightColors(colors.get("Muted")));
        }

        lights.add(light1);
        lights.add(light2);
        lights.add(light3);
        lights.add(light4);

        groupLight = new GroupLight(lights);

        callServiceToChangeLights(groupLight);
    }

    public String getLightColors(Palette.Swatch color) {
        //Format of string to create - hue:120 saturation:1.0 brightness:0.5
        String lightColor = "";
        float[] colorSettings = color.getHsl();

        lightColor += "hue:" + colorSettings[0] + " ";
        lightColor += "saturation:" + colorSettings[1] + " ";
        lightColor += "brightness:" + colorSettings[2];

        return lightColor;
    }

    public void callServiceToChangeLights(GroupLight groupLight) {
        String groupLightInJson = gson.toJson(groupLight);

        Call <Object> result = service.updateLights(AUTH_KEY, CONTENT_TYPE, groupLight);

        result.enqueue(new Callback<Object>() {

            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    System.out.println("Successfully changed lights");
                } else {
                    System.out.println("Received bad response with attempting to change lights");
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void callServiceForDefaultLights() {
        Light light = new Light(LIVING_ROOM, DEFAULT_COLOR);
        System.out.println(gson.toJson(light));
        Call<Object> result = service.defaultLights(AUTH_KEY, CONTENT_TYPE, LIVING_ROOM, light);

        result.enqueue(new Callback<Object>() {

            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    System.out.println("Successfully defaulted lights");
                } else {
                    System.out.println("Received bad response when attempting to default lights");
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
