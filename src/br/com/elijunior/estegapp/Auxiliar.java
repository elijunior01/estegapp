package br.com.elijunior.estegapp;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Build;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.Toast;

public class Auxiliar {
	private static Auxiliar _instance = null;
	private String bitsMensagem;
	private String bitsArquivo;
	private Bitmap arquivofotoEmbitmap;
	private Bitmap arquivofotoEmbitmapDesest;
	private byte[] byteArray;
	private String imageUristr;
	private String textoAserEsteg;
	private String senhaHash;

	public Bitmap getArquivofotoEmbitmapDesest() {
		return arquivofotoEmbitmapDesest;
	}

	public void setArquivofotoEmbitmapDesest(Bitmap arquivofotoEmbitmapDesest) {
		this.arquivofotoEmbitmapDesest = arquivofotoEmbitmapDesest;
	}

	public String getSenhaHash() {
		return senhaHash;
	}

	public static void clear() {
		_instance = null;
	}

	public String getTextoAserEsteg() {
		return textoAserEsteg;
	}

	public void setTextoAserEsteg(String textoAserEsteg) {
		this.textoAserEsteg = textoAserEsteg;
	}

	public String getImageUristr() {
		return imageUristr;
	}

	public void setImageUristr(String imageUristr) {
		this.imageUristr = imageUristr;
	}

	private Auxiliar() {
		bitsMensagem = "";
		bitsArquivo = "";
	}

	public static Auxiliar getInstance() {
		if (_instance == null) {
			_instance = new Auxiliar();
		}
		return _instance;
	}

	public String getBitsMensagem() {
		return bitsMensagem;
	}

	public void setBitsMensagem(String bitsMensagem) {
		this.bitsMensagem = bitsMensagem;
	}

	public String getBitsArquivo() {
		return bitsArquivo;
	}

	public void setBitsArquivo(String bitsArquivo) {
		this.bitsArquivo = bitsArquivo;
	}

	public Bitmap getArquivofotoEmbitmap() {
		return arquivofotoEmbitmap;
	}

	public void setArquivofotoEmbitmap(Bitmap arquivofotoEmbitmap) {
		this.arquivofotoEmbitmap = arquivofotoEmbitmap;
	}

	public byte[] getByteArray() {
		return byteArray;
	}

	public void setByteArray(byte[] byteArray) {
		this.byteArray = byteArray;
	}

	public void torradinha(String msg, Context context, boolean longo) {
		if (longo) {
			Toast toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		} else {
			Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
	}

	public String getDataHoraAtual() {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss",
				Locale.US);
		return sdf.format(c.getTime());
	}

	public Bitmap rotateBitmap(Bitmap bitmap, int orientation)
			throws IOException {
		Matrix matrix = new Matrix();
		switch (orientation) {
		case ExifInterface.ORIENTATION_NORMAL:
			return bitmap;
		case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
			matrix.setScale(-1, 1);
			break;
		case ExifInterface.ORIENTATION_ROTATE_180:
			matrix.setRotate(180);
			break;
		case ExifInterface.ORIENTATION_FLIP_VERTICAL:
			matrix.setRotate(180);
			matrix.postScale(-1, 1);
			break;
		case ExifInterface.ORIENTATION_TRANSPOSE:
			matrix.setRotate(90);
			matrix.postScale(-1, 1);
			break;
		case ExifInterface.ORIENTATION_ROTATE_90:
			matrix.setRotate(90);
			break;
		case ExifInterface.ORIENTATION_TRANSVERSE:
			matrix.setRotate(-90);
			matrix.postScale(-1, 1);
			break;
		case ExifInterface.ORIENTATION_ROTATE_270:
			matrix.setRotate(-90);
			break;
		default:
			return bitmap;
		}
		try {
			Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0,
					bitmap.getWidth(), bitmap.getHeight(), matrix, true);
			bitmap.recycle();
			return bmRotated;
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			return null;
		}
	}

	public int getExifOrientation(String src) throws IOException {
		int orientation = 1;
		try {
			if (Build.VERSION.SDK_INT >= 5) {
				Class<?> exifClass = Class
						.forName("android.media.ExifInterface");
				Constructor<?> exifConstructor = exifClass
						.getConstructor(new Class[] { String.class });
				Object exifInstance = exifConstructor
						.newInstance(new Object[] { src });
				Method getAttributeInt = exifClass.getMethod("getAttributeInt",
						new Class[] { String.class, int.class });
				Field tagOrientationField = exifClass
						.getField("TAG_ORIENTATION");
				String tagOrientation = (String) tagOrientationField.get(null);
				orientation = (Integer) getAttributeInt.invoke(exifInstance,
						new Object[] { tagOrientation, 1 });
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return orientation;
	}

	boolean verificarSenhaValida(EditText etSenhaHash,
			EditText etSenhaConfimadaHash, Context context) {
		if (!etSenhaHash.getText().toString().isEmpty()
				&& !etSenhaConfimadaHash.getText().toString().isEmpty()) {
			String senhaMd5Hex = md5(etSenhaHash.getText().toString());
			String senhamd5HexConfirmada = md5(etSenhaConfimadaHash.getText()
					.toString());
			if (senhaMd5Hex.equals(senhamd5HexConfirmada)) {
				this.senhaHash = senhaMd5Hex;
				return true;
			} else {
				return false;
			}
		} else {
			torradinha("Senhas não conferem", context, true);
			return false;
		}
	}

	public String md5(String s) {
		try {
			MessageDigest digest = java.security.MessageDigest
					.getInstance("MD5");
			digest.update(s.getBytes());
			byte messageDigest[] = digest.digest();
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < messageDigest.length; i++) {
				String h = Integer.toHexString(0xFF & messageDigest[i]);
				while (h.length() < 2)
					h = "0" + h;
				hexString.append(h);
			}
			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}

}
