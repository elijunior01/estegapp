package br.com.elijunior.estegapp;

import java.io.File;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class EsteganografarActivity extends Activity {

	protected static final int TIRAR_FOTO = 0;
	private static final int ESCOLHER_IMAGEM = 1;
	Button btEscolherImg, btAbrirCamera, btAnterior, btAvancar;
	EditText etCaminhodaImagem;
	ImageView ivImagem;
	Uri imageUri;
	Auxiliar aux;
	//boolean retorno = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_esteganografar);
		iniciarInstanciasDosobjetos();
		iniciarListners();
		definirCamera();
	}

//	void exibirAlerta() {
//		AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
//		builder1.setTitle("Atenção");
//		builder1.setMessage("Devido a limitada capaciade dos dispositivos móveis.\nÉ altamente recomendado que diminua a resolução da camera para no máximo HD.");
//		builder1.setCancelable(true);
//		builder1.setNeutralButton(android.R.string.ok,
//				new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog, int id) {
//						retorno = true;
//						if(retorno){
//							iniciarIntentCamera();
//						}
//						dialog.cancel();
//					}
//				});
//		builder1.show();
//		///return retorno;
//	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.esteganografar, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_sobre) {
			startActivity(new Intent(EsteganografarActivity.this,
					SobreActivity.class));
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (resultCode != RESULT_OK) {
			aux.torradinha("Erro, tente novamente.",
					this.getApplicationContext(), false);
		} else {
			if (requestCode == TIRAR_FOTO) {
				aux.setArquivofotoEmbitmap(getBitmapFromUri());
				etCaminhodaImagem.setText(aux.getImageUristr());
				ivImagem.setImageBitmap(aux.getArquivofotoEmbitmap());
				try {
					if (!aux.getImageUristr().isEmpty()) {
						File fototmp = new File(aux.getImageUristr());
						if (fototmp.exists()) {
							fototmp.delete();
						}
					}
				} catch (NullPointerException e) {
				}
			} else {
				if (requestCode == ESCOLHER_IMAGEM) {
					String fileSrc = intent.getData().getPath();
					etCaminhodaImagem.setText(fileSrc);
					Bitmap arquivofotoEmbitmap = BitmapFactory
							.decodeFile(fileSrc);
					aux.setArquivofotoEmbitmap(arquivofotoEmbitmap);
					ivImagem.setImageBitmap(arquivofotoEmbitmap);
				}
			}
		}
	}

	private void definirCamera() {
		if (checkCameraHardware(getApplicationContext())) {
			btAbrirCamera.setVisibility(View.VISIBLE);
		} else {
			btAbrirCamera.setVisibility(View.GONE);
		}
	}

	private boolean checkCameraHardware(Context context) {
		if (context.getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA)) {
			return true;
		} else {
			return false;
		}
	}

	void iniciarIntentCamera(){
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File filePhoto = new File(Environment
				.getExternalStorageDirectory() + "/EstegApp/",
				"EstegApp_" + aux.getDataHoraAtual() + "_.png");
		imageUri = Uri.fromFile(filePhoto);
		aux.setImageUristr(imageUri.getPath());
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		startActivityForResult(intent, TIRAR_FOTO);
	}
	
	void iniciarListners() {
		btAbrirCamera.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//boolean abrirCamera = exibirAlerta();
//				if (abrirCamera) {
					iniciarIntentCamera();
//				}
				//exibirAlerta();
			}
		});

		btAnterior.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Auxiliar.clear();
				startActivity(new Intent(EsteganografarActivity.this,
						EstegAppMainActivity.class));
				finish();
			}
		});

		btAvancar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (aux.getArquivofotoEmbitmap() != null
						&& !etCaminhodaImagem.getText().toString().isEmpty()) {
					startActivity(new Intent(EsteganografarActivity.this,
							Esteganografar2Activity.class));
				} else {
					aux.torradinha("Escolha corretamente a imagem.",
							getApplicationContext(), false);
				}
			}
		});

		btEscolherImg.setOnClickListener(new View.OnClickListener() {

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
	}

	void iniciarInstanciasDosobjetos() {
		btAbrirCamera = (Button) findViewById(R.id.btAbrirCamera);
		btAnterior = (Button) findViewById(R.id.btAnterior);
		btAvancar = (Button) findViewById(R.id.btAvancar);
		btEscolherImg = (Button) findViewById(R.id.btEscolherImg);
		etCaminhodaImagem = (EditText) findViewById(R.id.etTextoAserEsteg);
		ivImagem = (ImageView) findViewById(R.id.ivImagem);
		aux = Auxiliar.getInstance();
	}

	public Bitmap getBitmapFromUri() {
		getContentResolver().notifyChange(imageUri, null);
		ContentResolver cr = getContentResolver();
		Bitmap bitmap;
		try {
			bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr,
					imageUri);
			return aux.rotateBitmap(bitmap,
					aux.getExifOrientation(imageUri.getPath()));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}