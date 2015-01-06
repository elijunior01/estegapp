package br.com.elijunior.estegapp;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;

public class Esteganografia {

	LSB2bit lsb2bit = new LSB2bit();
	Auxiliar aux = Auxiliar.getInstance();

	public Esteganografia(String hashSenha) {
		if (!hashSenha.isEmpty()) {
			lsb2bit.setSTART_MESSAGE_COSTANT(hashSenha);
			lsb2bit.setEND_MESSAGE_COSTANT(new StringBuffer(hashSenha)
					.reverse().toString());
		}
	}

	public Bitmap encode(Bitmap bmp, String secret) {
		int height = bmp.getHeight();
		int width = bmp.getWidth();
		Bitmap newImage = null;
		int[] imgPixels = new int[width * height];
		bmp.getPixels(imgPixels, 0, width, 0, 0, width, height);
		int density = bmp.getDensity();
		bmp.recycle();
		try {
			byte[] byteImage = lsb2bit.encodeMessage(imgPixels, width, height,
					secret);
			newImage = Bitmap.createBitmap(width, height, Config.ARGB_8888);
			newImage.setDensity(density);
			int imgMod[] = lsb2bit.byteArrayToIntArray(byteImage);
			int masterIndex = 0;
			for (int j = 0; j < height; j++)
				for (int i = 0; i < width; i++) {
					newImage.setPixel(i, j, Color.argb(0xFF,
							imgMod[masterIndex] >> 16 & 0xFF,
							imgMod[masterIndex] >> 8 & 0xFF,
							imgMod[masterIndex++] & 0xFF));
				}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return newImage;
	}

	public String decode(Bitmap bmp) {
		byte[] b = null;
		try {
			int[] pixels = new int[bmp.getWidth() * bmp.getHeight()];
			bmp.getPixels(pixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(),
					bmp.getHeight());
			b = lsb2bit.convertArray(pixels);
		} catch (OutOfMemoryError er) {
			System.out
					.println("Imagem muito grande, limite de memória atingido.");
		}
		final String vvv = lsb2bit.decodeMessage(b, bmp.getWidth(),
				bmp.getHeight());
		return vvv;
	}
}