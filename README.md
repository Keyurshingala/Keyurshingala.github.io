//Api Client In Test Be Carefull

          public class ApiClient<T> {

              Activity activity;
              Dialog progress;
              DialogNoNet dialogNoNet;

              public ServiceGenerator<T> serviceGenerator;

              public interface ServiceGenerator<T> {
                  void OnSuccess(T obj);
              }

              public void callApiWithDialog(Activity activity, View root, Call<T> apiCall, ServiceGenerator<T> serviceGenerator) {
                  this.activity = activity;
                  this.serviceGenerator = serviceGenerator;

                  if (hasInternetConnect()) {
                      dismissNoNet();
                      showProgress();
                      apiCall.clone().enqueue(new Callback<T>() {
                          @Override
                          public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
                              new Handler(Looper.getMainLooper()).postDelayed(() -> dismissProgress(), 200);

                              if (response.isSuccessful() && response.body() != null) {
                                  serviceGenerator.OnSuccess(response.body());

                              } else {
                                  Snackbar.make(root, somethingWentWrong, Snackbar.LENGTH_INDEFINITE)
                                          .setAction("Try Again", view -> callApiWithDialog(activity, root, apiCall, serviceGenerator)).show();
                              }
                          }

                          @Override
                          public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
                              dismissProgress();
                              t.printStackTrace();

                              Snackbar.make(root, somethingWentWrong, Snackbar.LENGTH_INDEFINITE)
                                      .setAction("Try Again", view -> callApiWithDialog(activity, root, apiCall, serviceGenerator)).show();

                          }
                      });

                  } else {
                      showNoNet();
                      dialogNoNet.setOnRetryClickListener(new DialogNoNet.RootClickEvent() {
                          @Override
                          public void onRetryClick(@NonNull Dialog dialog) {
                              callApiWithDialog(activity, root, apiCall, serviceGenerator);
                          }
                      });
                  }
              }

              public void callApiWithBar(Activity activity, View root, ProgressBar pb, Call<T> apiCall, ServiceGenerator<T> serviceGenerator) {
                  this.activity = activity;
                  this.serviceGenerator = serviceGenerator;

                  if (hasInternetConnect()) {
                      dismissNoNet();
                      pb.setVisibility(View.VISIBLE);
                      apiCall.clone().enqueue(new Callback<T>() {
                          @Override
                          public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
                              pb.setVisibility(View.GONE);

                              if (response.isSuccessful() && response.body() != null) {
                                  serviceGenerator.OnSuccess(response.body());

                              } else {
                                  Snackbar.make(root, somethingWentWrong, Snackbar.LENGTH_INDEFINITE)
                                          .setAction("Try Again", view -> callApiWithBar(activity, root, pb, apiCall, serviceGenerator)).show();
                              }
                          }

                          @Override
                          public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
                              pb.setVisibility(View.GONE);
                              t.printStackTrace();

                              Snackbar.make(root, somethingWentWrong, Snackbar.LENGTH_INDEFINITE)
                                      .setAction("Try Again", view -> callApiWithBar(activity, root, pb, apiCall, serviceGenerator)).show();

                          }
                      });

                  } else {
                      showNoNet();
                      dialogNoNet.setOnRetryClickListener(new DialogNoNet.RootClickEvent() {
                          @Override
                          public void onRetryClick(@NonNull Dialog dialog) {
                              callApiWithBar(activity, root, pb, apiCall, serviceGenerator);
                          }
                      });
                  }
              }


              public static Api getService() {
                  HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();     //todo remove this line in production
                  interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);               //todo remove this line in production

                  OkHttpClient okClient = new OkHttpClient.Builder()
                          .addInterceptor(chain -> {
                              Request originalRequest = chain.request();
                              Request.Builder requestBuilder = originalRequest.newBuilder()
                                      .addHeader("Cache-Control", "no-cache")
                                      .method(originalRequest.method(), originalRequest.body());
                              Request request = requestBuilder.build();
                              return chain.proceed(request);
                          })
                          .addInterceptor(interceptor)                                    //todo remove this line in production
                          .connectTimeout(10, TimeUnit.SECONDS)
                          .readTimeout(10, TimeUnit.SECONDS)
                          .build();


                  return new Retrofit.Builder()
                          .baseUrl(cons.BASE_URL)
                          .addConverterFactory(GsonConverterFactory.create())
                          .client(okClient)
                          .build()
                          .create(Api.class);
              }

              private void initNoNetDialog() {
                  dialogNoNet = new DialogNoNet(activity);
              }

              private void showNoNet() {
                  if (dialogNoNet == null) initNoNetDialog();
                  dialogNoNet.show();
              }

              private void dismissNoNet() {
                  if (dialogNoNet != null) dialogNoNet.dismiss();
              }

              private void initProgressDialog() {
                  progress = new Dialog(activity);
                  progress.setContentView(DialogProgressBinding.inflate(activity.getLayoutInflater()).getRoot());
                  int widthAndHeight = WindowManager.LayoutParams.MATCH_PARENT;
                  progress.getWindow().setLayout(widthAndHeight, widthAndHeight);
                  progress.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                  progress.setCancelable(false);
              }

              public void showProgress() {
                  if (progress == null) initProgressDialog();
                  if (!progress.isShowing()) progress.show();
              }

              public void dismissProgress() {
                  if (progress != null && progress.isShowing()) progress.dismiss();
              }

              public boolean hasInternetConnect() {
                  boolean isWifiConnected = false;
                  boolean isMobileConnected = false;

                  NetworkInfo[] netInfo = ((ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE)).getAllNetworkInfo();

                  for (NetworkInfo ni : netInfo) {
                      if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                          if (ni.isConnected()) isWifiConnected = true;
                      if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                          if (ni.isConnected()) isMobileConnected = true;
                  }
                  return isWifiConnected || isMobileConnected;
              }
          }



//view to bitmap 

     /**
     *if dose not work call this method inside View.post() method
     **/
    public Bitmap bitmapFromView(View v) {
        Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        v.draw(new Canvas(b));
        return b;
    }



//simple animation

            public void slideUpFad(View v) {
                    v.setVisibility(View.VISIBLE);
                    v.setTranslationY(v.getHeight());
                    v.setAlpha(0f);
                    v.animate().setDuration(600)
                            .translationY(0)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                }
                            }).alpha(1f).start();
                }

//Unsplash Api Calling

            private const val BASE_URL = "https://api.unsplash.com/"

            const val AccessKey = "akYdbrQ_RcwiLMcEkHuZND-2FUTsHJ25k42aaaO67N4"

            object UnsplashClient {
                val service: SearchApi = Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build().create(SearchApi::class.java)
            }

            interface SearchApi {
                @GET("search/photos")
                fun getSearchPhotos(@Query("client_id") clientId: String,
                                    @Query("orientation") orientation: String,
                                    @Query("query") searchItem: String?,
                                    @Query("per_page") itemPerPage: Int): Call<SearchPhoto>
            }



//read asset file from asset file path

            public String readAssetsFile(String assetFilePath) {
                    String jsonString = "";
                    try {
                        InputStream is = getAssets().open(assetFilePath);
                        byte[] buffer = new byte[is.available()];
                        is.read(buffer);
                        is.close();
                        jsonString = new String(buffer, StandardCharsets.UTF_8);
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                    }
                    return jsonString;
                }



//save pdf from bitmap

            public File savePdf(Bitmap bitmap) {
                    PdfDocument document = new PdfDocument();
                    PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), 1).create();
                    PdfDocument.Page page = document.startPage(pageInfo);

                    Canvas canvas = page.getCanvas();
                    Paint paint = new Paint();
                    paint.setColor(Color.parseColor("#ffffff"));
                    canvas.drawPaint(paint);

                    bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);

                    paint.setColor(Color.BLUE);
                    canvas.drawBitmap(bitmap, 0, 0, null);
                    document.finishPage(page);

                    File myDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), getString(R.string.app_name));

                    if (!myDir.exists()) myDir.mkdirs();

                    String targetPdf = System.currentTimeMillis() + ".pdf";

                    File filePath = new File(myDir, targetPdf);

                    if (filePath.exists()) filePath.delete();

                    try {
                        document.writeTo(new FileOutputStream(filePath));
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                    }

                    document.close();

                    return filePath;
                }






//To Get Pic From Camera Full Size

            manifest
            <application...
             <provider
                        android:name="androidx.core.content.FileProvider"
                        android:authorities="${applicationId}.provider"
                        android:exported="false"
                        android:grantUriPermissions="true">
                        <meta-data
                            android:name="android.support.FILE_PROVIDER_PATHS"
                            android:resource="@xml/file_paths" />
                    </provider>
             </application>

           MainActivity
           private static final int REQUEST_IMAGE_CAPTURE = 111;
           String currentPhotoPath;

           private void dispatchTakePictureIntent() {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File photoFile = null;
                    try {
                        photoFile = File.createTempFile("Img"/*File Name prefix */, ".jpg"/*Extension suffix */);
                        currentPhotoPath = photoFile.getAbsolutePath();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(this, getPackageName() + ".provider", photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
            }

            @Override
            protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                super.onActivityResult(requestCode, resultCode, data);
                if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                    Log.i(TAG, currentPhotoPath); //  /data/user/0/com.example.xyz/cache/cam_img**some_auto_genreted_numers**.jpg
                    ((ImageView) findViewById(R.id.iv)).setImageURI(Uri.fromFile(new File(currentPhotoPath)));
                }
            }

        res>xml>file_paths.xml
        <?xml version="1.0" encoding="utf-8"?>
        <paths xmlns:android="http://schemas.android.com/apk/res/android">
            <external-files-path name="my_images" path="Pictures" />
        </paths>


//Dialog with Snackbar


    <?xml version="1.0" encoding="utf-8"?>
    <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        xmlns:tools="http://schemas.android.com/tools"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:background="@android:color/transparent"
        android:orientation="vertical">

        <com.google.android.material.card.MaterialCardView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_margin="@dimen/_16sdp"
            app:cardCornerRadius="@dimen/_4sdp"
            app:cardElevation="@dimen/_2sdp"
            app:cardUseCompatPadding="true">

            <RelativeLayout

                android:layout_width="match_parent"
                android:layout_height="wrap_content">

                <ProgressBar
                    android:id="@+id/pb"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_centerHorizontal="true"
                    android:layout_centerVertical="true"
                    android:visibility="gone"
                    tools:visibility="visible" />

                <LinearLayout
                    android:id="@+id/ll"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:clipToPadding="false"
                    android:orientation="vertical"
                    android:paddingStart="@dimen/_12sdp"
                    android:paddingTop="@dimen/_8sdp"
                    android:paddingEnd="@dimen/_12sdp"
                    android:paddingBottom="@dimen/_8sdp"
                    android:visibility="invisible"
                    tools:visibility="visible">          

                    <ImageView
                        android:id="@+id/ivClose"
                        android:layout_width="@dimen/_40sdp"
                        android:layout_height="@dimen/_24sdp"
                        android:layout_gravity="center_horizontal"
                        android:layout_marginBottom="@dimen/_minus8sdp"
                        android:src="@drawable/ic_close_wallet"
                        app:tint="@color/gray_d" />
                </LinearLayout>

                <androidx.coordinatorlayout.widget.CoordinatorLayout
                    android:id="@+id/clVisibleRoot"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:layout_alignBottom="@id/ll" />
            </RelativeLayout>
        </com.google.android.material.card.MaterialCardView>

    </LinearLayout>

    private void showWallet() {
        dialog = new Dialog(this);
        bind = WalletDialogBinding.inflate(getLayoutInflater());
        dialog.setContentView(bind.getRoot());

        int width = WindowManager.LayoutParams.MATCH_PARENT;
        int height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setLayout(width, height);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.setCancelable(true);

        bind.ivClose.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.show();

        //network call if needed
    }
    
    //if network all failed show option to try again
    public void tryAgain(String s, String method) {
        Snackbar snackbar = Snackbar.make(bind.clVisibleRoot, s, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Try Again", view -> {
            switch (method) {
                case method:
                    method();
                    break;
            }
        });
        snackbar.show();
    }
    
    



//Bitmap to File
        
      public File saveBitmap(Bitmap bitmap, Bitmap.CompressFormat format, int quality) {
        File myDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), getString(R.string.app_name));

        if (!myDir.exists()) myDir.mkdirs();

        String fileName;
        if (format == Bitmap.CompressFormat.JPEG)
            fileName = System.currentTimeMillis() + ".jpeg";
        else
            fileName = System.currentTimeMillis() + ".png";

        File file = new File(myDir, fileName);
        if (file.exists()) file.delete();

        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(format, quality, out);
            out.flush();
            out.close();

            MediaScannerConnection.scanFile(this, new String[]{file.getPath()}, new String[]{"image/jpeg", "image/png"}, null);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return file;
    }
    

//todo AsyncTask Alternative

            progress.show();
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.execute(new Runnable() {
                public void run() {
                    //do in background
                    getContactList();
                    System.out.println("Asynchronous task");
    

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progress.dismiss();
                            Log.i(TAG, "run: " + contactList.size());
                            //post Execute
                        }
                    });
                }
            });
            // executorService.shutdown();
            
//selector for Rg

        <?xml version="1.0" encoding="utf-8"?>
        <selector xmlns:android="http://schemas.android.com/apk/res/android">

            <item android:state_checked="true">
                <shape>
                    <solid android:color="@color/blue" />
                    <corners android:radius="@dimen/_12sdp" />
                    <padding android:bottom="@dimen/_8sdp" android:left="@dimen/_18sdp" android:right="@dimen/_18sdp" android:top="@dimen/_8sdp" />
                </shape>
            </item>

            <item android:state_pressed="true">
                <shape>
                    <solid android:color="#200C1947" />
                    <corners android:radius="@dimen/_12sdp" />
                    <padding android:bottom="@dimen/_8sdp" android:left="@dimen/_18sdp" android:right="@dimen/_18sdp" android:top="@dimen/_8sdp" />
                </shape>

            </item>

            <item android:state_checked="false">
                <shape>
                    <solid android:color="@color/btn_def" />
                    <corners android:radius="@dimen/_12sdp" />
                    <padding android:bottom="@dimen/_8sdp" android:left="@dimen/_18sdp" android:right="@dimen/_18sdp" android:top="@dimen/_8sdp" />
                </shape>
            </item>
        </selector>
            
//Image Blure With Picasso

        implementation 'com.squareup.picasso:picasso:2.71828'
        
       
        import android.content.Context;
        import android.graphics.Bitmap;
        import android.renderscript.Allocation;
        import android.renderscript.Element;
        import android.renderscript.RenderScript;
        import android.renderscript.ScriptIntrinsicBlur;
        import com.squareup.picasso.Transformation;

        public class BlurTransformation implements Transformation {

            RenderScript rs;

            public BlurTransformation(Context context) {
                super();
                rs = RenderScript.create(context);
            }

            @Override
            public Bitmap transform(Bitmap bitmap) {
                // Create another bitmap that will hold the results of the filter.
                Bitmap blurredBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

                // Allocate memory for Renderscript to work with
                Allocation input = Allocation.createFromBitmap(rs, blurredBitmap, Allocation.MipmapControl.MIPMAP_FULL, Allocation.USAGE_SHARED);
                Allocation output = Allocation.createTyped(rs, input.getType());

                // Load up an instance of the specific script that we want to use.
                ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
                script.setInput(input);

                // Set the blur radius
                script.setRadius(10);

                // Start the ScriptIntrinisicBlur
                script.forEach(output);

                // Copy the output to the blurred bitmap
                output.copyTo(blurredBitmap);

                bitmap.recycle();

                return blurredBitmap;
            }

            @Override
            public String key() {
                return "blur";
            }
        }
        
        Picasso.get().load(R.drawable.black_background)
        .transform(new BlurTransformation(context))
        .into(bind.blureView);


//Backround Blur

        implementation 'com.eightbitlab:blurview:1.6.6'

         <eightbitlab.com.blurview.BlurView
                        android:id="@+id/blureView"
                        android:layout_width="match_parent"
                        android:layout_height="match_parent" />

        View decorView = activity.getWindow().getDecorView();
        //ViewGroup you want to start blur from. Choose root as close to BlurView in hierarchy as possible.
        ViewGroup rootView = (ViewGroup) decorView.findViewById(android.R.id.content);
        //Set drawable to draw in the beginning of each blurred frame (Optional).
        //Can be used in case your layout has a lot of transparent space and your content
        //gets kinda lost after after blur is applied.
        Drawable windowBackground = decorView.getBackground();

        bind.blureView.setupWith(bind.getRoot())
                 .setFrameClearDrawable(windowBackground)
                 .setBlurAlgorithm(new RenderScriptBlur(activity))
                 .setBlurRadius(10f) //radius
                 .setBlurAutoUpdate(true)
                 .setHasFixedTransformationMatrix(false); // Or false if it's in a scrolling container or might be animated
                 
                 
//Full Screen
             
     //in values/themes.xml
     <style name="Theme.App" parent="Theme.MaterialComponents.Light.NoActionBar.Bridge">
        <!--        status bar-->
        <item name="colorPrimaryDark">@color/st</item>
        <!--        button-->
        <item name="colorPrimary">@color/btn</item>
        <!--        courser  switch  checkBox-->
        <item name="colorAccent">@color/accent</item>
     </style>

    <style name="Theme.WindowFullscreen" parent="Theme.App">
        <item name="android:windowNoTitle">true</item>
        <item name="android:windowFullscreen">true</item>
    </style>
    
    //in AndroidManifest.xml
    <application
        ...
        android:theme="@style/Theme.App">
                 
        <activity
            android:name=".SplashActivity"
            android:exported="true"
            android:theme="@style/Theme.WindowFullscreen">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

        <activity
            android:name=".LogInActivity"
            android:exported="true"
            android:theme="@style/Theme.WindowFullscreen">
        </activity>
      
    </application>
