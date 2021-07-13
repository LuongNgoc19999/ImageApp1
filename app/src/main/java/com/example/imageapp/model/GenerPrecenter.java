package com.example.imageapp.model;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class GenerPrecenter {

    public static final String request = "{\"data\":[{\"basePrice\":\"39.8万円\",\"date\":\"車検残：無（購入時に新規取得）車検の取得にあたって法定費用（自動車重量税、自賠責保険料など）が本体価格とは別に必要となります。車検整備無車検整備（法定24ヶ月定期点検整備／商用車は12ヶ月）を実施しません。購入後（車検取得後）に別途、車検整備を実施してください。\",\"img_url\":\"images/VU5596112459.jpg\",\"km\":\"6.8万km\",\"repair\":\"なし\",\"title\":\"タント 660 X 4WD 4WD/純正CD/電格ミラー/スマキー(山形)\",\"totalPrice\":\"---万円\",\"year\":\"2008(H20)\"},{\"basePrice\":\"58.0万円\",\"date\":\"車検残：無（購入時に新規取得）車検の取得にあたって必要な費用（自動車重量税、自賠責保険料など）が支払総額に含まれています。車検整備付車検整備（法定24ヶ月点検整備／商用車は12ヶ月）を実施致します。その費用は本体価格に含まれています。\",\"img_url\":\"images/VU5650183849.jpg\",\"km\":\"5.7万km\",\"repair\":\"なし\",\"title\":\"タント 660 X 4WD エコアイドルパワスラスマートキー(山形)\",\"totalPrice\":\"65.7万円\",\"year\":\"2012(H24)\"},{\"basePrice\":\"19.8万円\",\"date\":\"車検残：無（購入時に新規取得）車検の取得にあたって必要な費用（自動車重量税、自賠責保険料など）が支払総額に含まれています。車検整備無車検整備（法定24ヶ月定期点検整備／商用車は12ヶ月）を実施しません。購入後（車検取得後）に別途、車検整備を実施してください。\",\"img_url\":\"images/VU1565763182.jpg\",\"km\":\"6.2万km\",\"repair\":\"あり\",\"title\":\"タント 660 L 純正ナビDVD再生車検2年/外AW/キーレス(福島)\",\"totalPrice\":\"28.8万円\",\"year\":\"2009(H21)\"},{\"basePrice\":\"25.7万円\",\"date\":\"車検残：無（購入時に新規取得）車検の取得にあたって必要な費用（自動車重量税、自賠責保険料など）が支払総額に含まれています。車検整備付車検整備（法定24ヶ月点検整備／商用車は12ヶ月）を実施致します。その費用は本体価格に含まれています。\",\"img_url\":\"images/VU6026279912.jpg\",\"km\":\"6.9万km\",\"repair\":\"なし\",\"title\":\"タント 660 X リミテッド 片側電動スライドドアCD車検整備付(宮城)\",\"totalPrice\":\"38万円\",\"year\":\"2008(H20)\"},{\"basePrice\":\"34.8万円\",\"date\":\"車検残：無（購入時に新規取得）車検の取得にあたって必要な費用（自動車重量税、自賠責保険料など）が支払総額に含まれています。車検整備付車検整備（法定24ヶ月点検整備／商用車は12ヶ月）を実施致します。その費用は本体価格に含まれています。\",\"img_url\":\"images/VU5542302755.jpg\",\"km\":\"10.6万km\",\"repair\":\"なし\",\"title\":\"タント 660 X セレクション 4WD 1年走行無制限保証片側パワースライド(青森)\",\"totalPrice\":\"45万円\",\"year\":\"2009(H21)\"},{\"basePrice\":\"43.0万円\",\"date\":\"車検残：無（購入時に新規取得）車検の取得にあたって法定費用（自動車重量税、自賠責保険料など）が本体価格とは別に必要となります。車検整備別車検整備（法定24ヶ月定期点検整備／商用車は12ヶ月）を実施致しますが、それにかかる費用が本体価格とは別に必要となります。\",\"img_url\":\"images/CU8136319485.jpg\",\"km\":\"8.7万km\",\"repair\":\"なし\",\"title\":\"タント 660 X リミテッド 左側オートスライド(福島)\",\"totalPrice\":\"---万円\",\"year\":\"2008(H20)\"},{\"basePrice\":\"12.8万円\",\"date\":\"車検残：無（購入時に新規取得）車検の取得にあたって必要な費用（自動車重量税、自賠責保険料など）が支払総額に含まれています。車検整備無車検整備（法定24ヶ月定期点検整備／商用車は12ヶ月）を実施しません。購入後（車検取得後）に別途、車検整備を実施してください。\",\"img_url\":\"images/VU5650758500.jpg\",\"km\":\"10.0万km\",\"repair\":\"なし\",\"title\":\"タント 660 X リミテッド 左オートスライドスマートキー(宮城)\",\"totalPrice\":\"24.8万円\",\"year\":\"2008(H20)\"},{\"basePrice\":\"48.0万円\",\"date\":\"車検残：無（購入時に新規取得）車検の取得にあたって必要な費用（自動車重量税、自賠責保険料など）が支払総額に含まれています。車検整備付車検整備（法定24ヶ月点検整備／商用車は12ヶ月）を実施致します。その費用は本体価格に含まれています。\",\"img_url\":\"images/VU2654389375.jpg\",\"km\":\"7.2万km\",\"repair\":\"なし\",\"title\":\"タント 660 L 4WD CD キーレス(青森)\",\"totalPrice\":\"55万円\",\"year\":\"2008(H20)\"},{\"basePrice\":\"62.8万円\",\"date\":\"車検残：無（購入時に新規取得）車検の取得にあたって法定費用（自動車重量税、自賠責保険料など）が本体価格とは別に必要となります。車検整備付車検整備（法定24ヶ月点検整備／商用車は12ヶ月）を実施致します。その費用は本体価格に含まれています。\",\"img_url\":\"images/VU5842002262.jpg\",\"km\":\"10.1万km\",\"repair\":\"なし\",\"title\":\"タント 660 X 4WD 修復歴なし(福島)\",\"totalPrice\":\"---万円\",\"year\":\"2008(H20)\"},{\"basePrice\":\"49.0万円\",\"date\":\"2021(R03)年10月\",\"img_url\":\"images/VU5921061003.jpg\",\"km\":\"2.0万km\",\"repair\":\"なし\",\"title\":\"タント 660 X リミテッド 車いすスローパー車・4名乗車(岩手)\",\"totalPrice\":\"53万円\",\"year\":\"2010(H22)\"}],\"status\":0}";
    public static final String URL = "http://192.168.1.222:5000/";
    public static String createNameImage(){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMddhhmmss");

        return format1.format(cal.getTime());
    }

    public static Bitmap CropImage(Bitmap originImage, int widthScreen, int heighScreen, int widthCamera, int heightCamera){
        int w1 = originImage.getWidth();
        int h1 = originImage.getHeight();
//        int heightYCrop = (int) (originImage.getHeight()*2.5);
        int h = w1*heightCamera/widthCamera;
        int heightYCrop = (int) (h1-(originImage.getHeight()/13*2.5)- h);
//        widthCamera = originImage.getWidth();
        Bitmap result = Bitmap.createBitmap(originImage, 0, heightYCrop, w1, h);

        return result;
    }

    public static List<Data> ConVertJson(String request) {
        List<Data> resuilt = new ArrayList<>();
        Data data = new Data();
        try {
            JSONObject jsonObject = new JSONObject(request);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            Gson gson = new Gson();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject js = jsonArray.getJSONObject(i);
                String basePrice = js.getString("basePrice");
                String date = js.getString("date");
                String img_url = js.getString("img_url");
                String km = js.getString("km");
                String repair = js.getString("repair");
                String title = js.getString("title");
                String totalPrice = js.getString("totalPrice");
                String year = js.getString("year");
                data = new Data(0, basePrice, date, img_url, km, repair, title, totalPrice, year);
//                Log.d(TAG, "http://192.168.1.222:5000/" + data.getImg_url());
                resuilt.add(data);
            }
        } catch (JSONException e) {
            e.printStackTrace();
//            Log.d(TAG, e.getMessage());
        }
        return resuilt;
    }

}
