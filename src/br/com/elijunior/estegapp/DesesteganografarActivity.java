package br.com.elijunior.estegapp;

import java.net.URLEncoder;
import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DesesteganografarActivity extends Activity {

	private static final int ESCOLHER_IMAGEM = 1;
	Button btSelecionarImgDesest, btDesesteganografar, btVoltarDesesteg;
	TextView tvEnderecoImgDesest;
	EditText etSenhaDesest, etResultTextDesest;
	Auxiliar aux;
	Esteganografia desesteg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_desesteganografar);
		instanciarObjetos();
		iniciarListeners();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.desesteganografar, menu);
		return true;
	}

	void instanciarObjetos() {
		btDesesteganografar = (Button) findViewById(R.id.btDesesteganografar);
		btSelecionarImgDesest = (Button) findViewById(R.id.btSelecionarImgDesest);
		btVoltarDesesteg = (Button) findViewById(R.id.btVoltarDesesteg);
		tvEnderecoImgDesest = (TextView) findViewById(R.id.tvEnderecoImgDesest);
		etResultTextDesest = (EditText) findViewById(R.id.etResultTextDesest);
		etSenhaDesest = (EditText) findViewById(R.id.etSenhaDesest);
		aux = Auxiliar.getInstance();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (resultCode != RESULT_OK) {
			aux.torradinha("Erro, tente novamente.",
					this.getApplicationContext(), false);
		} else {
			if (requestCode == ESCOLHER_IMAGEM) {
				String fileSrc = intent.getData().getPath();
				tvEnderecoImgDesest.setText(fileSrc);
				aux.setArquivofotoEmbitmapDesest(BitmapFactory
						.decodeFile(fileSrc));
				aux.torradinha("Imagem escolhida com sucesso.",
						this.getApplicationContext(), false);
			}
		}
	}

	void iniciarListeners() {
		btDesesteganografar.setOnClickListener(new View.OnClickListener() {
			String textoRecuperado = null;

			@Override
			public void onClick(View v) {
				if (!etSenhaDesest.getText().toString().isEmpty()) {
					desesteg = new Esteganografia(aux.md5(etSenhaDesest
							.getText().toString()));
					try {
						textoRecuperado = desesteg.decode(aux
								.getArquivofotoEmbitmapDesest());
						URLEncoder.encode(textoRecuperado, "UTF-8");
						if (!textoRecuperado.isEmpty()) {
							etResultTextDesest.setText(textoRecuperado);
							aux.torradinha(
									"Mensagem desesteganografada com sucesso.",
									getApplicationContext(), false);
						} else {
							aux.torradinha("Não contém mensagem escondida.",
									getApplicationContext(), false);
						}
					} catch (Exception e) {
						aux.torradinha("Erro. Imagem ou senha errada.",
								getApplicationContext(), false);
						etResultTextDesest.setText(null);
						etResultTextDesest.setFocusable(true);
					}
				} else {
					aux.torradinha("Digite a senha.", getApplicationContext(),
							false);
				}
			}
		});

		btSelecionarImgDesest.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivityForResult(
						new Intent()
								.setAction(
										"com.sec.android.app.myfiles.PICK_DATA")
								.putExtra("CONTENT_TYPE", "image/png")
								.addCategory(Intent.CATEGORY_DEFAULT),
						ESCOLHER_IMAGEM);
			}
		});
		btVoltarDesesteg.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(),
						EstegAppMainActivity.class));
				finish();
			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_sobre) {
			startActivity(new Intent(DesesteganografarActivity.this,
					SobreActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
