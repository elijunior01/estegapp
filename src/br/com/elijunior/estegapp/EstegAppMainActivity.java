package br.com.elijunior.estegapp;

import java.io.File;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class EstegAppMainActivity extends Activity {

	Button btEsteg, btDesesteg, btSobre;
	Auxiliar aux;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_esteg_app_main);
		iniciarInstanciasDosobjetos();
		iniciarListners();
		criarPasta();
	}

	void criarPasta() {
		File sdcard = Environment.getExternalStorageDirectory();
		File f = new File(sdcard + "/EstegApp/");
		if (!f.exists()) {
			f.mkdirs();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.esteg_app_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_sobre) {
			startActivity(new Intent(EstegAppMainActivity.this,
					SobreActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void iniciarInstanciasDosobjetos() {
		btEsteg = (Button) findViewById(R.id.btEsteg);
		btDesesteg = (Button) findViewById(R.id.btDesesteg);
		btSobre = (Button) findViewById(R.id.btSobre);
		aux = Auxiliar.getInstance();
	}

	void iniciarListners() {
		btDesesteg.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(EstegAppMainActivity.this,
						DesesteganografarActivity.class));
			}
		});

		btEsteg.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(EstegAppMainActivity.this,
						EsteganografarActivity.class));
			}
		});

		btSobre.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(EstegAppMainActivity.this,
						SobreActivity.class));
			}
		});

	}

}
