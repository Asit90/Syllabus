package cakart.cakart.in.syllabus.syllabus;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import cakart.cakart.in.syllabus.R;

public class ICAIstudyMaterialFragment extends Fragment {
    StudmaterialsFragment sf;
    public boolean is_showing = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_icaistudy_material, container, false);
        ImageView ca_cpt = view.findViewById(R.id.ca_cpt_studymaterial);
        ImageView ca_foundation = view.findViewById(R.id.ca_foundation_studymaterial);

        ca_cpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("subhankar", "CA_CPT");
                try {
                     sf = new StudmaterialsFragment();
                    String ca_found = readTxt(R.raw.ca_cpt);
                    JSONObject j = new JSONObject(ca_found);
                    Bundle b = new Bundle();
                    b.putString("current_list", j.getJSONArray("childs").toString());
                    b.putInt("stack",0);
                    sf.setArguments(b);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.flcontent, sf).commit();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        ca_foundation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  try {
                       sf = new StudmaterialsFragment();
                    String ca_found = readTxt(R.raw.ca_foundation);
                    JSONObject j = new JSONObject(ca_found);
                    Bundle b = new Bundle();
                    b.putString("current_list", j.getJSONArray("childs").toString());
                    b.putInt("stack",0);
                    sf.setArguments(b);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.flcontent, sf).commit();
            }catch (Exception e)
            {
                e.printStackTrace();
            }

            }
        });
        is_showing = true;
        return view;
    }

    private String readTxt(int res) {
        InputStream inputStream = getResources().openRawResource(res);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            int i = inputStream.read();
            while (i != -1) {
                byteArrayOutputStream.write(i);
                i = inputStream.read();
            }
            inputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return byteArrayOutputStream.toString();
    }

    public  void goback(){
        if(sf!=null) {
            sf.goback();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        is_showing = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        is_showing = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        is_showing = true;
    }


}
