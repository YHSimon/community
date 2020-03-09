package com.yh_simon.community.provider;


import com.alibaba.fastjson.JSON;
import com.yh_simon.community.dto.AccesstokenDTO;
import com.yh_simon.community.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

@Component
public class GithubProvider {
    public String getAccesstoken(AccesstokenDTO accesstokenDTO){
        MediaType mediaType
                = MediaType.get("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(mediaType,JSON.toJSONString(accesstokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string=response.body().string();
//            System.out.println(string);
            String token = string.split("&")[0].split("=")[1];
//            System.out.println(token);
            return token;
        }catch (Exception e){
            e.printStackTrace();
        }
        return  null;
    }

    public GithubUser getUser(String accessToken){
        OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://api.github.com/user?access_token="+accessToken)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                String string= response.body().string();
                GithubUser githubUser = JSON.parseObject(string, GithubUser.class);
//                System.out.println(githubUser);
                return githubUser;
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
    }

}
