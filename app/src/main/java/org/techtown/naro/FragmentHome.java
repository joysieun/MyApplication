package org.techtown.naro;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FragmentHome extends Fragment {
    private View view;
    private Button button;
    private Button btn_using;
    DogDB dogDB;
    SQLiteDatabase DB;
    FirebaseAuth firebaseAuth;

    public FragmentHome(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home,container,false);
        button = (Button) view.findViewById(R.id.btn_cam);
        btn_using = (Button) view.findViewById(R.id.btn_using);

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser googleuser = firebaseAuth.getCurrentUser();
        String email = googleuser.getEmail();
        dogDB = new DogDB(getActivity().getApplicationContext(),"Dog.db",null,2);
        Dog dog = new Dog();
        setDB(getActivity());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DB = dogDB.getReadableDatabase();
                Cursor d = DB.rawQuery("SELECT petname, petage, petsex, petbirth, petface FROM User_dog WHERE  userid = '" +email+ "'ORDER BY userid",null);

                if(d.getCount() == 0 ){
                    Toast.makeText(getActivity(), "강아지정보부터 등록하시오",Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(getActivity(),Camera.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }

            }
        });
        return view;

    }

    public static final String ROOT_DIR = "/data/data/org.techtown.naro/databases";

    public static void setDB(Context ctx) {
        File folder = new File(ROOT_DIR);
        if (folder.exists()) {

        } else {
            folder.mkdirs();
        }
        AssetManager assetManager = ctx.getResources().getAssets();
        //db파일 이름
        File outfile = new File(ROOT_DIR + "pethospital.db");
        InputStream is = null;
        FileOutputStream fo = null;
        long filesize = 0;
        try {
            is = assetManager.open("pethospital.db", AssetManager.ACCESS_BUFFER);
            filesize = is.available();
            if (outfile.length() <= 0) {
                byte[] tempdata = new byte[(int) filesize];
                is.read(tempdata);
                is.close();
                outfile.createNewFile();
                fo = new FileOutputStream(outfile);
                fo.write(tempdata);
                fo.close();
            }

        } catch (IOException e) {

        }

    }
}
