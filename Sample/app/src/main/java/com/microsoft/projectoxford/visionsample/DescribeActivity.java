//
// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license.
//
// Microsoft Cognitive Services (formerly Project Oxford): https://www.microsoft.com/cognitive-services
//
// Microsoft Cognitive Services (formerly Project Oxford) GitHub:
// https://github.com/Microsoft/Cognitive-Vision-Android
//
// Copyright (c) Microsoft Corporation
// All rights reserved.
//
// MIT License:
// Permission is hereby granted, free of charge, to any person obtaining
// a copy of this software and associated documentation files (the
// "Software"), to deal in the Software without restriction, including
// without limitation the rights to use, copy, modify, merge, publish,
// distribute, sublicense, and/or sell copies of the Software, and to
// permit persons to whom the Software is furnished to do so, subject to
// the following conditions:
//
// The above copyright notice and this permission notice shall be
// included in all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED ""AS IS"", WITHOUT WARRANTY OF ANY KIND,
// EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
// MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
// NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
// LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
// OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
// WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
//
package com.microsoft.projectoxford.visionsample;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.microsoft.projectoxford.vision.VisionServiceClient;
import com.microsoft.projectoxford.vision.VisionServiceRestClient;
import com.microsoft.projectoxford.vision.contract.AnalysisResult;
import com.microsoft.projectoxford.vision.contract.Category;
import com.microsoft.projectoxford.vision.contract.Face;
import com.microsoft.projectoxford.vision.contract.Tag;
import com.microsoft.projectoxford.vision.contract.Caption;
import com.microsoft.projectoxford.vision.rest.VisionServiceException;
import com.microsoft.projectoxford.visionsample.Nutrients.Cholesterol;
import com.microsoft.projectoxford.visionsample.Nutrients.DietaryFiber;
import com.microsoft.projectoxford.visionsample.Nutrients.Kcal;
import com.microsoft.projectoxford.visionsample.Nutrients.MyNutrients;
import com.microsoft.projectoxford.visionsample.Nutrients.Nutrients;
import com.microsoft.projectoxford.visionsample.Nutrients.Protein;
import com.microsoft.projectoxford.visionsample.Nutrients.Sodium;
import com.microsoft.projectoxford.visionsample.Nutrients.Sugars;
import com.microsoft.projectoxford.visionsample.Nutrients.TotalCarbohydrate;
import com.microsoft.projectoxford.visionsample.Nutrients.TotalFat;
import com.microsoft.projectoxford.visionsample.example.Food;
import com.microsoft.projectoxford.visionsample.example.MyList;
import com.microsoft.projectoxford.visionsample.helper.ImageHelper;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class DescribeActivity extends ActionBarActivity {

    // Flag to indicate which task is to be performed.
    private static final int REQUEST_SELECT_IMAGE = 0;

    // The button to select an image
    private Button mButtonSelectImage;

    // The URI of the image selected to detect.
    private Uri mImageUri;

    // The image selected to detect.
    private Bitmap mBitmap;

    // The edit to show status and result.
    private EditText mEditText;

    private VisionServiceClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_describe);

        if (client==null){
            client = new VisionServiceRestClient(getString(R.string.subscription_key));
        }

        mButtonSelectImage = (Button)findViewById(R.id.buttonSelectImage);
//        mEditText = (EditText)findViewById(R.id.editTextResult);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_describe, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void doDescribe() {
        mButtonSelectImage.setEnabled(false);
//        mEditText.setText("Describing...");

        try {
            new doRequest().execute();
        } catch (Exception e)
        {
//            mEditText.setText("Error encountered. Exception is: " + e.toString());
        }
    }

    // Called when the "Select Image" button is clicked.
    public void selectImage(View view) {
//        mEditText.setText("");

        Intent intent;
        intent = new Intent(DescribeActivity.this, com.microsoft.projectoxford.visionsample.helper.SelectImageActivity.class);
        startActivityForResult(intent, REQUEST_SELECT_IMAGE);
    }

    // Called when image selection is done.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("DescribeActivity", "onActivityResult");
        switch (requestCode) {
            case REQUEST_SELECT_IMAGE:
                if(resultCode == RESULT_OK) {
                    // If image is selected successfully, set the image URI and bitmap.
                    mImageUri = data.getData();

                    mBitmap = ImageHelper.loadSizeLimitedBitmapFromUri(
                            mImageUri, getContentResolver());
                    if (mBitmap != null) {
                        // Show the image on screen.
                        ImageView imageView = (ImageView) findViewById(R.id.selectedImage);
                        imageView.setImageBitmap(mBitmap);

                        // Add detection log.
                        Log.d("DescribeActivity", "Image: " + mImageUri + " resized to " + mBitmap.getWidth()
                                + "x" + mBitmap.getHeight());

                        doDescribe();
                    }
                }
                break;
            default:
                break;
        }
    }


    private String process() throws VisionServiceException, IOException {
        Gson gson = new Gson();

        // Put the image into an input stream for detection.
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(output.toByteArray());

        AnalysisResult v = this.client.describe(inputStream, 1);

        String result = gson.toJson(v);
        Log.d("result", result);

        return result;
    }

    private class doRequest extends AsyncTask<String, String, String> {
        // Store error message
        private Exception e = null;

        public doRequest() {
        }

        @Override
        protected String doInBackground(String... args) {
            try {
                return process();
            } catch (Exception e) {
                this.e = e;    // Store error
            }

            return null;
        }

        public void onClick(View v) {
            // 1) Possibly check for instance of first
            Button b = (Button)v;
            String buttonText = b.getText().toString();
            Log.i("label", buttonText);
        }

        @Override
        protected void onPostExecute(String data) {
            super.onPostExecute(data);
            // Display based on error existence

//            mEditText.setText("");
            if (e != null) {
//                mEditText.setText("Error: " + e.getMessage());
                this.e = null;
            } else {
                Gson gson = new Gson();
                AnalysisResult result = gson.fromJson(data, AnalysisResult.class);

                /*
                mEditText.append("Image format: " + result.metadata.format + "\n");
                mEditText.append("Image width: " + result.metadata.width + ", height:" + result.metadata.height + "\n");
                mEditText.append("\n");

                for (Caption caption: result.description.captions) {
                    mEditText.append("Caption: " + caption.text + ", confidence: " + caption.confidence + "\n");
                }
                mEditText.append("\n");

                */

                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.editTextResult);
                int i=0;
                for (String tag: result.description.tags) {
                    //mEditText.append(tag + "\n");
                    Log.i("Labeltag", tag);
                    final Button newButton = new Button(getApplicationContext());
                    newButton.setText(tag);
                    newButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String buttonText = newButton.getText().toString();
                            Log.i("label", buttonText);

                            //final TextView mTextView = (TextView) findViewById(R.id.text);

                            // Instantiate the RequestQueue.
                            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                            String url ="https://nutrition-slug.herokuapp.com/getFood/"+buttonText;

                            Log.i("label", url);

                            // Request a string response from the provided URL.
                            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            ScrollView firstScroll = (ScrollView) findViewById(R.id.smartScrollView);
                                            firstScroll.setVisibility(View.GONE);
                                            // Display the first 500 characters of the response string.
                                            Log.i("label", response);
                                            Gson gson   = new Gson();
                                            Nutrients nutrientsObj = gson.fromJson(response, Nutrients.class);
                                            MyNutrients nutrients = nutrientsObj.getMyNutrients();

//                                            MyNutrients nutrients = gson.fromJson(response, MyNutrients.class);

//                                            Cholesterol cholesterol = nutrients.getCholesterol();
//                                            DietaryFiber dietaryFiber = nutrients.getDietaryFiber();
//                                            Kcal kcal = nutrients.getKcal();

                                            //Log.i("label", "Protein " + nutrients.getProtein().getName());
//                                            Sodium sodium = nutrients.getSodium();
//                                            Sugars sugars = nutrients.getSugars();
//                                            TotalCarbohydrate carbohydrate = nutrients.getTotalCarbohydrate();
//                                            TotalFat fat = nutrients.getTotalFat();

                                            TextView nutriView = (TextView) findViewById(R.id.nutriView);
                                            String nutriString = "";
//
                                            try {
                                                if(nutrients.getSodium().getName() != null){
                                                    Log.i("label", "sodium " + nutrients.getSodium().getName() );
                                                    nutriString += "sodium: " + nutrients.getSodium().getValue() + nutrients.
                                                            getSodium().getUnit() + "\n";
                                                }
                                            } catch(Exception exception){}

                                            try {
                                                if(nutrients.getCholesterol().getName() != null){
                                                    Log.i("label", "sodium " + nutrients.getCholesterol().getName() );
                                                    nutriString += "Cholesterol: " + nutrients.getCholesterol().getValue() + nutrients.
                                                            getCholesterol().getUnit() + "\n";
                                                }
                                            } catch (Exception exception) {

                                            }
                                            try {
                                                if(nutrients.getDietaryFiber().getName() != null){
                                                    Log.i("label", "sodium " + nutrients.getDietaryFiber().getName() );
                                                    nutriString += "Fiber: " + nutrients.getDietaryFiber().getValue() + nutrients.
                                                            getDietaryFiber().getUnit() + "\n";

                                                }
                                            } catch (Exception exception) {

                                            }
                                            try {
                                                if(nutrients.getProtein().getName() != null){
                                                    Log.i("label", "sodium " + nutrients.getProtein().getName() );
                                                    nutriString += "Protein: " + nutrients.getProtein().getValue() + nutrients.
                                                            getProtein().getUnit() + "\n";
                                                }
                                            } catch (Exception exception) {

                                            }
                                            try {
                                                if(nutrients.getSugars().getName() != null){
                                                    Log.i("label", "getSugars" + nutrients.getSugars().getName() );
                                                    nutriString += "Sugar: " + nutrients.getSugars().getValue() + nutrients.
                                                            getSugars().getUnit() + "\n";
                                                }
                                            } catch (Exception exception) {

                                            }
                                            try {
                                                if(nutrients.getTotalFat().getName() != null){
                                                    Log.i("label", "getTotalFat" + nutrients.getTotalFat().getName() );
                                                    nutriString += "Fat: " + nutrients.getTotalFat().getValue() + nutrients.
                                                            getTotalFat().getUnit() + "\n";
                                                }
                                            } catch (Exception exception) {

                                            }
                                            try {
                                                if(nutrients.getTotalCarbohydrate().getName() != null){
                                                    Log.i("label", "getTotalCarbohydrate" + nutrients.getTotalCarbohydrate().getName() );
                                                    nutriString += "Carbs: " + nutrients.getTotalCarbohydrate().getValue() + nutrients.
                                                            getTotalCarbohydrate().getUnit() + "\n";
                                                }
                                            } catch (Exception exception) {

                                            }

                                            nutriView.setText(nutriString);



















                                            //MyList list = gson.fromJson(response, MyList.class);
//                                            Log.i("label", Arrays.toString(list.getMyList().toArray()));


                                            //for (Food food : list.getMyList()) {
                                            //    Log.i("label", food.getName() + " " + food.getNdbno());
                                            //}
                                            // list.getMyList().toString();
//                                            Log.i("label", list.getMyList().toString());
                                            // Log.i("Label: ", response);
                                            //mTextView.setText("Response is: "+ response.substring(0,500));
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    //mTextView.setText("That didn't work!");
                                }
                            });
                            // Add the request to the RequestQueue.
                            queue.add(stringRequest);
                        }
                    });
                    linearLayout.addView(newButton);

                }

                // mEditText.append("\n");

                // mEditText.append("\n--- Raw Data ---\n\n");
                // mEditText.append(data);
                // mEditText.setSelection(0);
            }

            mButtonSelectImage.setEnabled(true);
        }
    }
}
