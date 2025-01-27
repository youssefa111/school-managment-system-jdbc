package utils;


import com.google.gson.Gson;

public interface JsonConverterUtil {

    static String convertToJson(Object obj) {
     Gson gson = new Gson();
     return gson.toJson(obj);
    }
    static <T> T convertFromJson(String json, Class<T> clazz) {
        Gson gson = new Gson();
        return gson.fromJson(json, clazz);
    }
}
