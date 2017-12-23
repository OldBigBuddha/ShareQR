package kyoto.freeprojects.oldbigbuddha.shareqr;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.support.v4.database.DatabaseUtilsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewOverlay;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import kyoto.freeprojects.oldbigbuddha.shareqr.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        ViewTreeObserver treeObserver = mBinding.ivQr.getViewTreeObserver();
        treeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (getIntent().getAction().equals(Intent.ACTION_SEND)) {
                    String url = getIntent().getExtras().getCharSequence(Intent.EXTRA_TEXT).toString();
                    try {
                        mBinding.ivQr.setImageBitmap( makeQR(url) );
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        mBinding.btCreateQr.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        String url = mBinding.etUrl.getText().toString();

        try {
            mBinding.ivQr.setImageBitmap( makeQR( url ) );
        } catch (WriterException e) {
            e.printStackTrace();
        }

        Toast.makeText(MainActivity.this, url, Toast.LENGTH_SHORT).show();
        mBinding.etUrl.setText("");

    }

    public Bitmap makeQR(String url) throws WriterException {
        BarcodeEncoder encoder  = new BarcodeEncoder();
        int size = calcScreenSize();
        return encoder.encodeBitmap(url, BarcodeFormat.QR_CODE, size, size);

    }

    public int calcScreenSize() {
        int height = mBinding.ivQr.getHeight();
        int width  = mBinding.ivQr.getWidth();
        int size   = width <= height ? width : height;
        return size;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return true;
    }
}
