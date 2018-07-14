package cakart.cakart.in.syllabus.syllabus;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import cakart.cakart.in.syllabus.R;
import cakart.cakart.in.syllabus.model.Node;

public class StudmaterialsFragment extends Fragment {


    String current_list;
    String parent;
    boolean is_root = true;
    ListView list;
    int stack = 0;
    ProgressDialog mProgressDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.studymaterialsfragment, container, false);
        current_list = getArguments().getString("current_list");
        parent = getArguments().getString("parent");
        stack = getArguments().getInt("stack");
        list = (ListView) view.findViewById(R.id.list);
        showList(current_list, true);

        return view;
    }


    public void showNext(String next_a) {


        StudmaterialsFragment sf = new StudmaterialsFragment();
        Bundle b = new Bundle();
        b.putString("current_list", next_a);
        b.putInt("stack", stack);
        sf.setArguments(b);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().addToBackStack("stc_" + stack).replace(R.id.flcontent, sf).commit();
    }


    public void showList(String json_s, boolean is_root) {
        try {
            JSONArray json = new JSONArray(json_s);
            final ArrayList<Node> a = new ArrayList<Node>();
            for (int i = 0; i < json.length(); i++) {
                JSONObject j = (JSONObject) json.get(i);
                Node n = new Node();
                n.setName(j.getString("title"));
                if (j.has("is_file")) {
                    n.setUrl(j.getString("link"));
                    if(n.getUrl().contains(".zip") || n.getUrl().contains(".ZIP")){
                        continue;
                    }
                }
                if (j.has("childs")) {
                    n.setChilds(j.getJSONArray("childs").toString());
                }
                n.setParent(json.toString());
                a.add(n);
            }
            CustomAdapter adapter = new CustomAdapter(a, getContext());
            list.setAdapter(adapter);

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> p, View view, int position, long id) {
                    Node clicked_node = a.get(position);
                    if (clicked_node.getUrl() != null) {
                        new DownloadFileFromURL().execute(clicked_node.getUrl());
                    } else {
                        stack = stack + 1;
                        DeckListActivity d = (DeckListActivity) getActivity();
                        d.stack_count = stack;
                        parent = clicked_node.getParent();
                        Log.d("Akhil", "stacj" + stack);
                        showNext(clicked_node.getChilds());
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void goback() {
        if (getActivity().getSupportFragmentManager().getBackStackEntryCount() == 0) {
            getActivity().finish();
            return;
        } else {
            getFragmentManager().popBackStack();
        }
    }


    public static StudmaterialsFragment newInstance(String current_list, String parent) {

        StudmaterialsFragment f = new StudmaterialsFragment();
        Bundle b = new Bundle();
        b.putString("current_list", current_list);
        b.putString("parent", parent);
        f.setArguments(b);

        return f;
    }


    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setTitle("Downloading");
            mProgressDialog.setMessage("Please Wait!");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(100);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.show();
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {

                String file_name = f_url[0].substring(f_url[0].lastIndexOf("/") + 1, f_url[0].length());
                String download_director = Environment.getExternalStorageDirectory().getAbsolutePath() + "/CA Foundation Downloads";
                new File(download_director).mkdirs();

                if (new File(download_director + "/" + file_name).exists()) {
                    return download_director + "/" + file_name;
                }

                Log.d("Akhil",f_url[0]);

                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                // this will be useful so that you can show a tipical 0-100% progress bar
                int lenghtOfFile = conection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(), 8192);


                // Output stream
                OutputStream output = new FileOutputStream(download_director + "/" + file_name);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

                return download_director + "/" + file_name;

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task
         * Dismiss the progress dialog
         **/
        @Override
        protected void onPostExecute(String downloaded_path) {
            // dismiss the dialog after the file was downloaded
            Log.d("Akhil", "Downloaded - " + downloaded_path);
            mProgressDialog.dismiss();
            if (downloaded_path == null) {
                Toast.makeText(getActivity(), "Failed to download!check your internet connection.", Toast.LENGTH_LONG).show();
                return;
            }
            Toast.makeText(getActivity(), "Downloaded", Toast.LENGTH_LONG).show();
            openFile(downloaded_path);
        }

    }

    public void openFile(String file_path) {
        MimeTypeMap myMime = MimeTypeMap.getSingleton();
        Intent newIntent = new Intent(Intent.ACTION_VIEW);
        String mimeType = myMime.getMimeTypeFromExtension(fileExt(file_path));
        newIntent.setDataAndType(Uri.fromFile(new File(file_path)), mimeType);
        newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            getActivity().startActivity(newIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getActivity(), "No handler for this type of file. Please install to open "+file_path.substring(file_path.lastIndexOf(".")+1,file_path.length())+" file", Toast.LENGTH_LONG).show();
        }
    }

    private String fileExt(String url) {
        if (url.indexOf("?") > -1) {
            url = url.substring(0, url.indexOf("?"));
        }
        if (url.lastIndexOf(".") == -1) {
            return null;
        } else {
            String ext = url.substring(url.lastIndexOf(".") + 1);
            if (ext.indexOf("%") > -1) {
                ext = ext.substring(0, ext.indexOf("%"));
            }
            if (ext.indexOf("/") > -1) {
                ext = ext.substring(0, ext.indexOf("/"));
            }
            return ext.toLowerCase();

        }
    }



}