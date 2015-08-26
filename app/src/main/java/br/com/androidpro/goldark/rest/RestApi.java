package br.com.androidpro.goldark.rest;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * @author Thiago Pagonha
 * @version 25/08/15.
 */
public class RestApi implements RequestInterceptor {

    private static RestApi restApi;
    private AndroidPro androidPro;
    private String sessionToken;

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

    public interface AndroidPro {
        /**
         * Autentica o usuário
         * @param user
         * @param callback
         */
        @POST("/sessions")
        void authenticate(@Body User user, Callback<Session> callback);

        /**
         * Cria um endereço novo
         * @param endereco
         * @param callback
         */
        @POST("/endereco")
        void createEndereco(@Body Endereco endereco, Callback<Endereco> callback);

        /**
         * Busca por um endereço que o usuário já tenha criado
         * @param userId
         * @param callback
         */
        @GET("/endereco")
        void retrieveEndereco(@Query("usuario") String userId, Callback<PageableResult<Endereco>> callback);

        /**
         * Remove um endereço que o usuário tenha inserido
         * @param id
         */
        @DELETE("/endereco/{id}")
        void eraseEndereco(@Path("id") String id, Callback<Status> callback);

        /**
         * Atualiza um endereço que já estava previamente salvo
         * @param id
         * @param endereco
         * @param callback
         */
        @PUT("/endereco/{id}")
        void updateEndereco(@Path("id") String id, @Body Endereco endereco, Callback<Endereco> callback);

    }

    public AndroidPro getAndroidPro() {
        return androidPro;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }
}
