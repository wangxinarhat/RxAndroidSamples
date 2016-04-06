package wang.wangxinarhat.rxandroidsamples.cache;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.List;

import wang.wangxinarhat.rxandroidsamples.domain.ImageInfoBean;
import wang.wangxinarhat.rxandroidsamples.global.BaseApplication;

/**
 * Created by wang on 2016/4/6.
 */
public class DataCache {
    private static String DATA_FILE_NAME = "data_cache";

    private static DataCache INSTANCE;

    File dataFile = new File(BaseApplication.getApplication().getFilesDir(), DATA_FILE_NAME);
    Gson gson = new Gson();

    public static DataCache newInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DataCache();
        }
        return INSTANCE;
    }

    public List<ImageInfoBean> readData() {

        try {
            Reader reader = new FileReader(dataFile);

            return gson.fromJson(reader, new TypeToken<List<ImageInfoBean>>() {
            }.getType());

        } catch (FileNotFoundException e) {
            e.printStackTrace();


            return null;
        }

    }

    public void writeData(List<ImageInfoBean> list) {

        String json = gson.toJson(list);

        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            Writer writer = new FileWriter(dataFile);
            writer.write(json);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean deleteCache() {
        return dataFile.delete();
    }
}
