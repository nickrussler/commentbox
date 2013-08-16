package utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.faces.application.FacesMessage;

import org.apache.commons.io.IOUtils;
import org.primefaces.model.UploadedFile;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;

import db.enums.ImageURLValidateResult;

public class ImageUploadUtils {
	public static String getMD5(String filepath) {
		
		try {
			HashCode hashCode = Files.hash(new File(filepath), Hashing.md5());
			
			return hashCode.toString();
		} catch (IOException e) {
			Utils.log("ImageUpload", e.getMessage());
		}
		
		return null;
	}
	
	private static String processFile(File file,int IMG_SIZE, String prefix) {
		String md5 = getMD5(file.getAbsolutePath());
		
		if (md5 == null) {
			Utils.log("ImageUpload processFile", "MD5 is null (wtf?): " + file.getName());
			return null;
		}
		
		String filename = prefix + "_" + md5 + "." + Files.getFileExtension(file.getName());

		File newFile = null;
		try {
			newFile = new File(Constants.IMAGE_BASE_DIR + filename);
			if (!newFile.exists()) {				
				ImageResizeUtils.resizeImage(file, newFile,IMG_SIZE);
				Utils.log("ImageUpload processFile", "Uploaded Image as: " + newFile.getAbsolutePath());
			} else {				
				Utils.log("ImageUpload processFile", "File was already Uploaded before (" + newFile.getAbsolutePath() + ")");
			}
			
			File parent_dir = file.getParentFile();
			
			file.delete();			
			parent_dir.delete();
			
			
			String uploadedImageFilename = newFile.getName();
			Utils.log("ImageUpload processFile", "Image download was successfull: " + newFile.getAbsolutePath());
			
			return uploadedImageFilename;
		} catch (Exception e) {
			Utils.log("ImageUpload processFile", e.getMessage());
			return null;
		}
	}

	
	public static String fromClient(UploadedFile uploadedFile,int IMG_SIZE, String prefix) {			
		File tmp_file = null;		
		try {
			FileOutputStream out = null;
			try {
				InputStream data = uploadedFile.getInputstream();
				tmp_file = new File(Files.createTempDir() + "/tmp_" + uploadedFile.getFileName());
				out = new FileOutputStream(tmp_file);
				IOUtils.copy(data, out);
			} finally {
				if (out != null) {
					out.close();
				}
			}
			
			Utils.log("ImageUpload", "File was saved temporarely as: " + tmp_file.getAbsolutePath());
		} catch (Exception e1) {
			Utils.log("ImageUpload", e1.getMessage());
			return null;
		}
		
		return processFile(tmp_file,IMG_SIZE, prefix);
	}
	
	public static ImageURLValidateResult validateImageUrl(URL url) {
		ImageURLValidateResult result = ImageURLValidateResult.UnkownError;
		
		try {
			HttpURLConnection.setFollowRedirects(false);
			// note : you may also need
			// HttpURLConnection.setInstanceFollowRedirects(false)
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("HEAD");
			
			if (con.getResponseCode() != HttpURLConnection.HTTP_OK) {
				return ImageURLValidateResult.NoHTTPConnectionError;
			}	
			
			String length = con.getHeaderField("Content-Length");
			
			if (length == null) {
				return ImageURLValidateResult.NoLengthError;
			}
			
			long contentLength = Long.parseLong(length);

			if (contentLength > Constants.MAX_FILE_SIZE_IMAGE_UPLOAD) {
				return ImageURLValidateResult.MaxImageSizeExceededError;
			}
			
			if (con.getContentType().equals("image/jpeg")) {
				return ImageURLValidateResult.ValidJPEG;
			}
			
			if (con.getContentType().equals("image/png")) {
				return ImageURLValidateResult.ValidPNG;
			}
			
			if (con.getContentType().equals("image/gif")) {
				return ImageURLValidateResult.ValidGIF;
			}
			
			return ImageURLValidateResult.NoImageContentTypeError;
		} catch (Exception e) {
			Utils.log("isImage()", e.getMessage());
		}
		
		return result;
	}
	
	public static String fromWeb(String imageUrl, String idForExceptions, MessageHandler messageHandler,int IMG_SIZE, String prefix) {	
		String extension = "";
		
		URL url;
		try {
			url = new URL(imageUrl);
			
			ImageURLValidateResult validation_result = validateImageUrl(url);			

			switch (validation_result) {
			case ValidGIF:
				extension = ".gif";
				break;
			case ValidJPEG:
				extension = ".jpg";
				break;
			case ValidPNG:
				extension = ".png";
				break;	
			case UnkownError:
				Utils.log("ImageUpload", "URL is invalid: " + imageUrl);
				messageHandler.addJSFMessage(new JSFMessage(idForExceptions,"Invalid URL","URL is invalid !",FacesMessage.SEVERITY_ERROR));
				return null;
			case NoLengthError:
				Utils.log("ImageUpload", "Can't Get the length: " + imageUrl);
				messageHandler.addJSFMessage(new JSFMessage(idForExceptions,"Invalid URL","Please Download this Image file and upload it yourself !",FacesMessage.SEVERITY_ERROR));
				return null;
			case NoHTTPConnectionError:
				Utils.log("ImageUpload", "URL is not reachable: " + imageUrl);
				messageHandler.addJSFMessage(new JSFMessage(idForExceptions,"Invalid URL","URL is not reachable !",FacesMessage.SEVERITY_ERROR));
				return null;
			case NoImageContentTypeError:
				Utils.log("ImageUpload", "URL doesn't seem to link to an image: " + imageUrl);				
				messageHandler.addJSFMessage(new JSFMessage(idForExceptions,"Invalid URL","URL doesn't seem to link to an image !",FacesMessage.SEVERITY_ERROR));
				return null;
			case MaxImageSizeExceededError:
				Utils.log("ImageUpload", "Image size is exceeded: " + imageUrl);
				messageHandler.addJSFMessage(new JSFMessage(idForExceptions,"Invalid URL","Image size is exceeded !",FacesMessage.SEVERITY_ERROR));				
				return null;
			}		
		} catch (MalformedURLException e) {
			Utils.addMessageForException(idForExceptions, "Invalid UR", "URL is invalid !");
			Utils.log("ImageUpload", e.getMessage());
			return null;
		}
			
		File tmp_file = new File(Files.createTempDir() + "/tmp" + extension);
		try {
			org.apache.commons.io.FileUtils.copyURLToFile(url, tmp_file);
			Utils.log("ImageUpload", "File was saved temporarely as: " + tmp_file.getAbsolutePath());
		} catch (IOException e) {
			Utils.log("ImageUpload", e.getMessage());
			return null;
		}
		
		return processFile(tmp_file,IMG_SIZE, prefix);
	}
}
