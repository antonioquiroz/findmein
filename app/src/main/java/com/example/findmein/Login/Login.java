package com.example.findmein.Login;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.findmein.ConnectivityObserver;
import com.example.findmein.Inicio;
import com.example.findmein.LiveConnectivityManager;
import com.example.findmein.R;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

public class Login extends AppCompatActivity implements ConnectivityObserver {
    private static final String TAG = "";
    private final int RC_SIGN_IN = 1;
    boolean connectionEnabled;
    private GoogleSignInOptions gso;
    private CallbackManager callbackManager;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private ImageView imageView;
    private GoogleSignInClient googleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LiveConnectivityManager.singleton(this).addObserver(this);
        imageView = findViewById(R.id.imageView3);
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(v -> signIn());
        LoginButton loginButton = findViewById(R.id.login_button);

        callbackManager = CallbackManager.Factory.create();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = firebaseAuth -> {
            user = firebaseAuth.getCurrentUser();
            if(user != null){
                updateUI(user);
                goMainScreen();
            }
            else{
                updateUI(null);
            }
        };
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        googleSignInClient = com.google.android.gms.auth.api.signin.GoogleSignIn.getClient(this, gso);
        FacebookSdk.sdkInitialize(getApplicationContext());
        loginButton.setReadPermissions(Arrays.asList("email","public_profile","user_birthday"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }
            @Override
            public void onCancel() {
            }
            @Override
            public void onError(@NonNull FacebookException error) {
            }
        });
        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(@Nullable AccessToken accessToken, @Nullable AccessToken accessToken1) {
                if (accessToken1 == null) {
                    firebaseAuth.signOut();
                }
            }
        };
    }
    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,RC_SIGN_IN);
    }
    private void FirebaseGoogleAuth(GoogleSignInAccount acct) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(acct.getIdToken(),null);
        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(this, task -> {
            if(task.isSuccessful()){
                user= firebaseAuth.getCurrentUser();
                updateUI(user);
                goMainScreen();
            }else{
                updateUI(null);
            }
        });
    }
    private void updateUI(FirebaseUser user) {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if(user != null){
            String photourl = user.getPhotoUrl().toString();
            photourl = photourl + "?type=large";
            Picasso.get().load(photourl).into(imageView);
        } else if (account != null){
            String photourl = account.getPhotoUrl().toString();
            photourl = photourl + "?type=large";
            Picasso.get().load(photourl).into(imageView);
        }
        else{
            Picasso.get().load(R.drawable.user_logo).into(imageView);
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount acc = task.getResult(ApiException.class);
            FirebaseGoogleAuth(acc);
        } catch (ApiException e){
            FirebaseGoogleAuth(null);
        }
    }      
    private void handleFacebookAccessToken(AccessToken accessToken){
        AuthCredential authCredential = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(this, task -> {
            if(task.isSuccessful()){
                user = firebaseAuth.getCurrentUser();
                updateUI(user);
                goMainScreen();
            }
            else{
                updateUI(null);
            }
        });
    }
    private void goMainScreen(){
        Intent principal = new Intent(Login.this, Inicio.class);
        principal.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        if(GoogleSignIn.getLastSignedInAccount(getApplicationContext()) != null){
            principal.putExtra("gso", gso);
        }
        startActivity(principal);
    }
    protected void onStart(){
        super.onStart();
        LiveConnectivityManager.singleton(this).addObserver(this);
        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }
    protected  void  onStop(){
        super.onStop();
        if(firebaseAuthListener != null){
            firebaseAuth.removeAuthStateListener(firebaseAuthListener);
        }
    }
    protected void onPause(){
        super.onPause();
        LiveConnectivityManager.singleton(this).removeObserver(this);
        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }
    public void manageNotification(boolean connectionEnabled){
        this.connectionEnabled = connectionEnabled;
    }
    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
        {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    public int objDatosJSON(String response){
        int res =0;
        try
        {
            JSONArray json =    new JSONArray(response);
            if(json.length() > 0)
            {
                res = 1;

            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return res;
    }
    public String enviarDatosGet(String correo, String passwd){
        URL url = null;
        String linea = "";
        int respuesta = 0;
        StringBuilder resul = null;
        try
        {
            url = new URL("https://findmein.000webhostapp.com/app/login.php?correo=" + correo + "&passwd=" + passwd + "");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            respuesta = connection.getResponseCode();
            resul = new StringBuilder();
            if(respuesta == HttpURLConnection.HTTP_OK)
            {
                InputStream in = new BufferedInputStream(connection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                while((linea = reader.readLine()) != null)
                {
                    resul.append(linea);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return resul.toString();
    }
}