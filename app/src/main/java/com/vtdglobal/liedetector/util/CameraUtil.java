package com.vtdglobal.liedetector.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;

import androidx.annotation.NonNull;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.core.SurfaceRequest;
import androidx.camera.core.impl.PreviewConfig;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.common.util.concurrent.ListenableFuture;

public class CameraUtil {

    @SuppressLint("StaticFieldLeak")
    public static ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    @SuppressLint("StaticFieldLeak")
    public static ProcessCameraProvider cameraProvider;

    private static void bindPreview(ProcessCameraProvider cameraProvider, Context context, PreviewView view) {
        Preview preview = new Preview.Builder().build();
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                .build();
        preview.setSurfaceProvider(view.getSurfaceProvider());

        cameraProvider.unbindAll();
        cameraProvider.bindToLifecycle((LifecycleOwner) context, cameraSelector, preview);

    }



    public static void startCamera(Context context, PreviewView view) {
        cameraProviderFuture = ProcessCameraProvider.getInstance(context);
        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    cameraProvider = cameraProviderFuture.get();
                    bindPreview(cameraProvider, context,view);
                } catch (Exception e) {
                    Log.e("CameraXApp", "Error: ", e);
                }
            }
        }, ContextCompat.getMainExecutor(context));
    }
    private static void bindPreviewBack(ProcessCameraProvider cameraProvider, Context context, PreviewView view) {
        Preview preview = new Preview.Builder().build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();
        preview.setSurfaceProvider(view.getSurfaceProvider());

        cameraProvider.unbindAll();
        cameraProvider.bindToLifecycle((LifecycleOwner) context, cameraSelector, preview);

    }
    public static void startCameraBack(Context context, PreviewView view) {
        cameraProviderFuture = ProcessCameraProvider.getInstance(context);
        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    cameraProvider = cameraProviderFuture.get();
                    bindPreviewBack(cameraProvider, context,view);
                } catch (Exception e) {
                    Log.e("CameraXApp", "Error: ", e);
                }
            }
        }, ContextCompat.getMainExecutor(context));
    }


}
