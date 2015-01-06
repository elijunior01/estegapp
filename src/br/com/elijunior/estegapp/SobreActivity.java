package br.com.elijunior.estegapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SobreActivity extends Activity {

	Button voltar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sobre);
		instanciarObjetos();
		iniciarListeners();
	}

	void instanciarObjetos() {
		voltar = (Button) findViewById(R.id.btEsteg);
	}

	void iniciarListeners() {
		voltar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(SobreActivity.this,
						EstegAppMainActivity.class));
				finish();
			}
		});
	}
}
