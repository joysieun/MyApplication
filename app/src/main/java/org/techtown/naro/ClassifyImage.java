// ClassifyImage.java
package org.techtown.naro;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Pair;

import androidx.appcompat.app.AppCompatActivity;

import org.techtown.naro.ml.ConvertedModel;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Tensor;
import org.tensorflow.lite.support.common.FileUtil;
import org.tensorflow.lite.support.common.ops.NormalizeOp;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.label.TensorLabel;
import org.tensorflow.lite.support.model.Model;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassifyImage{
    private static final String MODEL_NAME = "converted_model.tflite";
    private static final String LABEL_FILE = "labels.txt";
    private List<String> labels;
    Context context;
    Model model;
    int modelInputWidth, modelInputHeight, modelInputChannel;
    TensorImage inputImage;
    TensorBuffer outputBuffer;

    public ClassifyImage(Context context) {
        this.context = context;
    }
    // 모델로드: ClassifierWithModel 클래스에 Model 타입의 변수를 만들고 초기화 메소드인 init 메소드를 생성하고 작성
    public void init() throws IOException {
        model = Model.createModel(context, MODEL_NAME);
        initModelShape();

        labels = FileUtil.loadLabels(context, LABEL_FILE);
    }

    // 입출력 텐서를 구하는 메소드
    private void initModelShape() {
        Tensor inputTensor = model.getInputTensor(0);
        int[] shape = inputTensor.shape();
        modelInputChannel = shape[0];
        modelInputWidth = shape[1];
        modelInputHeight = shape[2];

        inputImage = new TensorImage(inputTensor.dataType());

        Tensor outputTensor = model.getOutputTensor(0);
        outputBuffer = TensorBuffer.createFixedSize(outputTensor.shape(),
                outputTensor.dataType());
    }

    // 이미지 전처리를 위한 메소드
    private TensorImage loadImage(final Bitmap bitmap) {
        if(bitmap.getConfig() != Bitmap.Config.ARGB_8888) {
            inputImage.load(convertBitmapToARGB8888(bitmap));
        } else {
            inputImage.load(bitmap);
        }

        ImageProcessor imageProcessor = new ImageProcessor.Builder()
                .add(new ResizeOp(modelInputHeight, modelInputWidth, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
                .add(new NormalizeOp(127.5f, 127.5f))
                .build();
        return imageProcessor.process(inputImage);
    }

    // 추론 결과 해석: Map<String, Float> 자료구조를 처리하는 argmax 메소드
    public Pair<String, Float> argmax(Map<String, Float> map) {
        String maxKey = "";
        float maxVal = -1;

        for (Map.Entry<String, Float> entry : map.entrySet()) {
            float f = entry.getValue();
            if (f > maxVal) {
                maxKey = entry.getKey();
                maxVal = f;
            }
        }
        return new Pair<>(maxKey, maxVal);
    }


    // 추론 결과 해석: 모델 출력과 label 매핑
//    public Pair<String, Float> classify(Bitmap image) {
//        inputImage = loadImage(image);
//        Object[] inputs = new Object[]{inputImage.getBuffer()};
//        Map<Integer, Object> outputs = new HashMap<>();
//        outputs.put(0, outputBuffer.getBuffer().rewind());
//
//        model.run(inputs, outputs);
//
//        Map<String, Float> output =
//                new TensorLabel(labels, outputBuffer).getMapWithFloatValue();
//
//        return argmax(output);
//    }

    public Map<String, Float> classify(Bitmap image) {
        inputImage = loadImage(image);
        Object[] inputs = new Object[]{inputImage.getBuffer()};
        Map<Integer, Object> outputs = new HashMap<>();
        outputs.put(0, outputBuffer.getBuffer().rewind());

        model.run(inputs, outputs);

        return new TensorLabel(labels, outputBuffer).getMapWithFloatValue();
    }

    // 자원 해제
    public void finish() {
        if (model != null) {
            model.close();
        }
    }

    private Bitmap convertBitmapToARGB8888(Bitmap bitmap) {
        return bitmap.copy(Bitmap.Config.ARGB_8888, true);
    }

}