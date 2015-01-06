package br.com.elijunior.estegapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Esteganografar2Activity extends Activity {

	Button btAvancar2, btVoltarEstg2;
	EditText etTextoAserEsteg;
	Auxiliar aux;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_esteganografar2);
		iniciarInstanciasDosobjetos();
		iniciarListners();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.esteganografar2, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_sobre) {
			startActivity(new Intent(Esteganografar2Activity.this,
					SobreActivity.class));
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	void iniciarListners() {
		btAvancar2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!etTextoAserEsteg.getText().toString().isEmpty()) {
					aux.setTextoAserEsteg(etTextoAserEsteg.getText().toString());
					startActivity(new Intent(Esteganografar2Activity.this,
							Esteganografar3Activity.class));
				} else {
					aux.torradinha("Digite corretamente a mensagem.",
							getApplicationContext(), false);
				}
			}
		});
		btVoltarEstg2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(),
						EsteganografarActivity.class));
			}
		});
	}

	void iniciarInstanciasDosobjetos() {
		aux = Auxiliar.getInstance();
		btAvancar2 = (Button) findViewById(R.id.btAvancar_2);
		btVoltarEstg2 = (Button) findViewById(R.id.btVoltarEstg2);
		etTextoAserEsteg = (EditText) findViewById(R.id.etTextoAserEsteg);
	}
}
