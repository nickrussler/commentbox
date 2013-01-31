package utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLConnection;
import java.net.URLDecoder;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The Image servlet for serving from absolute path.
 * @author BalusC
 * @link http://balusc.blogspot.com/2007/04/imageservlet.html
 */
public class ImageServlet extends HttpServlet {
    
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) {

        // Define base path somehow. You can define it as init-param of the servlet.
        String imageFilePath = Constants.IMAGE_BASE_DIR;
        
        // In a Windows environment with the Applicationserver running on the
        // c: volume, the above path is exactly the same as "c:\images".
        // In UNIX, it is just straightforward "/images".
        // If you have stored images in the WebContent of a WAR, for example in the
        // "/WEB-INF/images" folder, then you can retrieve the absolute path by:
        // String imageFilePath = getServletContext().getRealPath("/WEB-INF/images");
        
        // Get file name from request.
        //String imageFileName = request.getParameter("file");
        
        // Get requested image by path info.
        String imageFileName = request.getPathInfo();
        
        // Check if file name is supplied to the request.
        if (imageFileName != null) {
            // Strip "../" and "..\" (avoid directory sniffing by hackers!).
            imageFileName = imageFileName.replaceAll("\\.+(\\\\|/)", "");
        } else {
            // Do your thing if the file name is not supplied to the request.
            // Throw an exception, or show default/warning image, or just ignore it.
            return;
        }
        
        // Decode the file name and prepare file object.
        try
		{
			imageFileName = URLDecoder.decode(imageFileName, "UTF-8");
		} catch (UnsupportedEncodingException e1)
		{
			
			e1.printStackTrace();
		}
        File imageFile = new File(imageFilePath, imageFileName);

        // Check if file actually exists in filesystem.
        if (!imageFile.exists()) {
            // Do your thing if the file appears to be non-existing.
            // Throw an exception, or show default/warning image, or just ignore it.
            return;
        }
        
        // Get content type by filename.
        String contentType = URLConnection.guessContentTypeFromName(imageFileName);

        // Check if file is actually an image (avoid download of other files by hackers!).
        // For all content types, see: http://www.w3schools.com/media/media_mimeref.asp
        if (contentType == null || !contentType.startsWith("image")) {
            // Do your thing if the file appears not being a real image.
            // Throw an exception, or show default/warning image, or just ignore it.
            return;
        }
        
        // Prepare streams.
        BufferedInputStream input = null;
        BufferedOutputStream output = null;
        
        try {
            // Open image file.
            input = new BufferedInputStream(new FileInputStream(imageFile));
            int contentLength = input.available();
            
            long expiry = DateUtils.getNow() + Constants.IMAGE_CACHE_EXPIRATION_TIME;
            
            // Init servlet response.
            response.reset();
            response.setContentLength(contentLength);
            response.setContentType(contentType);
            response.setHeader("Content-disposition", "inline; filename=\"" + imageFileName + "\"");
            response.setHeader("Cache-Control", "max-age=" + Constants.IMAGE_CACHE_EXPIRATION_TIME);
            
            response.setDateHeader("Expires", expiry);
            
            output = new BufferedOutputStream(response.getOutputStream());

            // Write file contents to response.
            while (contentLength-- > 0) {
                output.write(input.read());
            }
            
            // Finalize task.
            output.flush();
        } catch (IOException e) {
            // Something went wrong?
            e.printStackTrace();
        } finally {
            // Gently close streams.
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    // Do your thing with the exception. Print it, log it or mail it.
                    e.printStackTrace();
                }
            }
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    // Do your thing with the exception. Print it, log it or mail it.
                    e.printStackTrace();
                }
            }
        }
    }

}