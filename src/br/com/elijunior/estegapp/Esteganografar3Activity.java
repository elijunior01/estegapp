package br.com.elijunior.estegapp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class Esteganografar3Activity extends Activity {

	TextView tvTextoFinal;
	EditText etSenhaHash, etSenhaConfimadaHash;
	ImageView imagemConfrmacao;
	Button btCancelar, btConfirmar, btVoltarEstg3;
	Auxiliar aux;
	Bitmap bmpImage;
	Esteganografia esteg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_esteganografar3);
		iniciarInstanciasDosobjetos();
		iniciarlistners();
	}

	void iniciarInstanciasDosobjetos() {
		tvTextoFinal = (TextView) findViewById(R.id.tvTextoFinal);
		imagemConfrmacao = (ImageView) findViewById(R.id.ivImagemConfirmacao);
		btCancelar = (Button) findViewById(R.id.btCancelar_final);
		btConfirmar = (Button) findViewById(R.id.btConfirmar_final);
		btVoltarEstg3 = (Button) findViewById(R.id.btVoltarEstg3);
		etSenhaHash = (EditText) findViewById(R.id.etSenha);
		etSenhaConfimadaHash = (EditText) findViewById(R.id.etSenhaConfirmada);
		tvTextoFinal.setText(Auxiliar.getInstance().getTextoAserEsteg());
		imagemConfrmacao.setImageBitmap(Auxiliar.getInstance()
				.getArquivofotoEmbitmap());
		aux = Auxiliar.getInstance();
	}

	void iniciarlistners() {
		btCancelar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Auxiliar.clear();
				startActivity(new Intent(Esteganografar3Activity.this,
						EstegAppMainActivity.class));
				finish();
			}
		});
		btConfirmar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (aux.verificarSenhaValida(etSenhaHash, etSenhaConfimadaHash,
						getApplicationContext())) {
					esteg = new Esteganografia(aux.md5(etSenhaHash.getText()
							.toString()));
					try {
						esteganografar();
						Auxiliar.clear();
						startActivity(new Intent(getApplicationContext(),
								EstegAppMainActivity.class));
						finish();
					} catch (Exception e) {
						aux.torradinha("Erro desconhecido.",
								getApplicationContext(), true);
					}
				}
			}
		});
		btVoltarEstg3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(),
						Esteganografar2Activity.class));
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.esteganografar3, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_sobre) {
			startActivity(new Intent(Esteganografar3Activity.this,
					SobreActivity.class));
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	void esteganografar() {
		bmpImage = esteg.encode(aux.getArquivofotoEmbitmap(),
				aux.getTextoAserEsteg());
		OutputStream fOut = null;
		File file = new File(Environment.getExternalStorageDirectory()
				+ "/EstegApp/", "ImgEsteganografada_" + aux.getDataHoraAtual()
				+ "_.png");
		try {
			fOut = new FileOutputStream(file);
			bmpImage.compress(Bitmap.CompressFormat.PNG, 100, fOut);
			fOut.flush();
			fOut.close();
			aux.torradinha("Esteganografado com sucesso. Imagem salva em: "
					+ file.getAbsolutePath(), getApplicationContext(), true);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}
