package br.com.androidpro.goldark;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.http.POST;

/**
 * @author Thiago Pagonha
 * @version 25/08/15.
 */
public class RestApi implements RequestInterceptor {

    private static RestApi restApi;
    AndroidPro androidPro;
    String sessionToken;

    private RestApi() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://androidpro.goldarkapi.com")
                .setRequestInterceptor(this)
                .build();
        androidPro = restAdapter.create(AndroidPro.class);
    }

    public static RestApi getApi() {
        if(restApi==null) {
            restApi = new RestApi();
        }

        return restApi;
    }

    /**
     * Adiciona os headers que são necessários para a requisição,
     * X-Api_token específico da api
     * X-Access-Token específico do usuário logado
     * @param request
     */
    public void intercept(RequestFacade request) {
        request.addHeader("X-Api-Token",
                    "hP8LCYufKnFbx5p2WdEMfsRDa1kJBAZDrFfMGm4yaJS3XNsm7lFo917iikJ6QuPk5JY8Gj/ME0T3wzJEVR/PAw==");
        if(sessionToken!=null) {
            request.addHeader("X-Access-Token",sessionToken);
        }

    }

    interface AndroidPro {
        @POST("sessions")
        void authenticate(User user, Callback<Session> callback);

        @POST("endereco")
        void createEndereco()
    }
}
