

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.devil.videoeditor.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;



public class ListActivity extends AppCompatActivity implements SavedFilesAdapter.OnFileClickedListener {

    Activity activity=ListActivity.this;
    SwipeMenuListView rvVideo;
    SavedFilesAdapter mSavedFilesAdapter;
    RelativeLayout rlNoFiles;
    RelativeLayout rlNoPermission;
    private List<File> mEditedFilesOfApp;

    private static final String FILEPATH = "filepath";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        init();
        getPermission();
    }

    private void init() {
        rvVideo=findViewById(R.id.rvVideo);
        rlNoFiles = findViewById(R.id.rlNoFiles);
        rlNoPermission = findViewById(R.id.rlNoPermission);

    }

    private void loadFiles() {

        rlNoPermission.setVisibility(View.GONE);
        rvVideo.setVisibility(View.VISIBLE);

        File moviesDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_MOVIES
        );
        File musicDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_MUSIC
        );

        mEditedFilesOfApp = new ArrayList<>();

        File[] fileListMovie= moviesDir.listFiles();
        File[] fileListMusic= musicDir.listFiles();

        if (fileListMovie !=null) {

            List<File> filesArrayList = new ArrayList<>(Arrays.asList(fileListMovie));

            for (File file : filesArrayList) {
                if (!file.isDirectory()) {
                    String fileName = file.getName();
                    if ((fileName.startsWith("merge") && fileName.endsWith("mp4")) ||
                            (fileName.startsWith("concat") && fileName.endsWith("mp4")) ||
                            (fileName.startsWith("cut_video") && fileName.endsWith("mp4"))) {
                        mEditedFilesOfApp.add(file);
                    }
                }
            }

        }

        if (fileListMusic != null) {
            List<File> filesArrayList = new ArrayList<>(Arrays.asList(fileListMusic));
            for (File file : filesArrayList) {
                if (!file.isDirectory()) {
                    String fileName = file.getName();
                    if ((fileName.startsWith("cut_audio") && fileName.endsWith("mp3")) ||
                            (fileName.startsWith("extract_audio") && fileName.endsWith("aac"))) {
                        mEditedFilesOfApp.add(file);
                    }
                }
            }
        }



        if (mEditedFilesOfApp.size() > 0) {

            Collections.sort(mEditedFilesOfApp, new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    return Long.valueOf(o2.lastModified()).compareTo(o1.lastModified());
                }
            });

            mSavedFilesAdapter = new SavedFilesAdapter(this, mEditedFilesOfApp);
            mSavedFilesAdapter.setmOnFileClickedListener(this);
            rvVideo.setAdapter(mSavedFilesAdapter);
            rvVideo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    onFileClick(mEditedFilesOfApp.get(position));
                }
            });
            mSavedFilesAdapter.notifyDataSetChanged();
            setMenu();

        } else {
            showNoFiles();
        }
    }

    private void setMenu() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {

                SwipeMenuItem shareItem = new SwipeMenuItem(ListActivity.this);
                shareItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0xC9, 0x25)));
                shareItem.setWidth(dp2px(80));
                shareItem.setIcon(R.drawable.share_pdf);
                menu.addMenuItem(shareItem);

                SwipeMenuItem deleteItem = new SwipeMenuItem(ListActivity.this);
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                deleteItem.setWidth(dp2px(80));
                deleteItem.setIcon(R.drawable.ic_delete);
                menu.addMenuItem(deleteItem);
            }
        };
        // set creator
        rvVideo.setMenuCreator(creator);
        rvVideo.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                File file = mEditedFilesOfApp.get(position);
                switch (index) {
                    case 0:
                        shareFile(file);
                        break;
                    case 1:
                        mEditedFilesOfApp.remove(position);
                        file.delete();
                        mSavedFilesAdapter.notifyDataSetChanged();
                        break;
                }
                return false;
            }
        });

        // set SwipeListener
        rvVideo.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

            @Override
            public void onSwipeStart(int position) {
            }

            @Override
            public void onSwipeEnd(int position) {
            }
        });

        // set MenuStateChangeListener
        rvVideo.setOnMenuStateChangeListener(new SwipeMenuListView.OnMenuStateChangeListener() {
            @Override
            public void onMenuOpen(int position) {
            }

            @Override
            public void onMenuClose(int position) {
            }
        });
    }

    private void shareFile(File file) {
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setDataAndType(uri, "*/*");
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, "");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        try {
            startActivityForResult(Intent.createChooser(intent, "Share File"), 369);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No App Available", Toast.LENGTH_SHORT).show();
        }
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    private void showNoFiles() {
        rvVideo.setVisibility(View.GONE);
        rlNoPermission.setVisibility(View.GONE);
        rlNoFiles.setVisibility(View.VISIBLE);
    }

    private void showNoPermission() {
        rvVideo.setVisibility(View.GONE);
        rlNoPermission.setVisibility(View.VISIBLE);
        rlNoFiles.setVisibility(View.GONE);
    }

    @Override
    public void onFileClick(File file) {
        String fileName = file.getName();
        Intent intent = null;

        if (fileName.endsWith("mp3") || fileName.endsWith("aac")) {
            intent = new Intent(ListActivity.this, AudioPreviewActivity.class);
        } else if (fileName.endsWith("mp4")) {
            intent = new Intent(ListActivity.this, VideoPreviewActivity.class);
        }

        if (intent != null) {
            intent.putExtra(FILEPATH, file.getAbsolutePath());
            startActivity(intent);
        }
    }

    private void getPermission() {
        String[] params = null;
        String writeExternalStorage = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        String readExternalStorage = Manifest.permission.READ_EXTERNAL_STORAGE;

        int hasWriteExternalStoragePermission = ActivityCompat.checkSelfPermission(this, writeExternalStorage);
        int hasReadExternalStoragePermission = ActivityCompat.checkSelfPermission(this, readExternalStorage);
        List<String> permissions = new ArrayList<>();

        if (hasWriteExternalStoragePermission != PackageManager.PERMISSION_GRANTED)
            permissions.add(writeExternalStorage);
        if (hasReadExternalStoragePermission != PackageManager.PERMISSION_GRANTED)
            permissions.add(readExternalStorage);

        if (!permissions.isEmpty()) {
            params = permissions.toArray(new String[permissions.size()]);
        }
        if (params != null && params.length > 0) {
            ActivityCompat.requestPermissions(ListActivity.this,
                    params,
                    100);
            showNoPermission();
        } else {
            loadFiles();
        }
    }

    /**
     * Handling response for permission request
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode==100 && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadFiles();
        } else {
            showNoPermission();
        }
    }
}
