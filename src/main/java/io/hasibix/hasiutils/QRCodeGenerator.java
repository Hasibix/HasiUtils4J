package io.hasibix.hasiutils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class QRCodeGenerator {
	public static byte[] GenerateQRCodeImage(String text, int width, int height) throws WriterException, IOException {
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height, GetQRCodeHints());
		BufferedImage qrCodeImage = ToBufferedImage(bitMatrix);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(qrCodeImage, "png", baos);
		return baos.toByteArray();
	}

	private static BufferedImage ToBufferedImage(BitMatrix matrix) {
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, matrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
			}
		}
		return image;
	}

	private static Map<EncodeHintType, Object> GetQRCodeHints() {
		Map<EncodeHintType, Object> hints = new HashMap<>();
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		return hints;
	}
}
