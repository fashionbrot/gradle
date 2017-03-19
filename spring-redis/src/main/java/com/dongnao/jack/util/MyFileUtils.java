package com.dongnao.jack.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author kevin
 */
public class MyFileUtils {

	private static Properties p = new Properties();

	private static Properties messageProp = new Properties();
	
	public static String getProperty(String key) {
		return p.getProperty(key);
	}

	public static String getMessage(String key) {
		return messageProp.getProperty(key);
	}

	static {
		reload();
		// reloadNotice( );
	}

	public static void reload() {
		InputStream inputStream = MyFileUtils.class.getResourceAsStream("/config.properties");

		try {
			p.load(inputStream);
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			IOUtils.closeQuietly(inputStream);
		}
	}


	public static final String HOST_ROOT_STATIC = getProperty("host_prefix");

	public static final String OS_NAME = System.getProperty("os.name").toLowerCase();

	public static final String FILE_ROOT_STATIC = (OS_NAME.startsWith("win") ? getProperty("win_root")
			: getProperty("unix_root"));

	public static final String DB_KEY_FILE = (OS_NAME.startsWith("win") ? getProperty("win_db_key")
			: getProperty("unix_db_key"));

	public static String writeUploadFile(MultipartFile file) {
		String datepath = DateFormatUtils.format(System.currentTimeMillis(), "yyyy/MM/dd") + "/"
				+ RandomUtils.nextInt(1, 100);

		String filename = file.getOriginalFilename();

		String retpath = "/" + datepath;

		String realpath = MyFileUtils.FILE_ROOT_STATIC + retpath;
		File fileDir = new File(realpath);
		if (!fileDir.exists())
			fileDir.mkdirs();

		String extname = FilenameUtils.getExtension(filename);
		filename = Math.abs(file.getOriginalFilename().hashCode()) + RandomUtils.nextBytes(4).toString() + "." + extname;
		InputStream input = null;
		FileOutputStream fos = null;
		try {
			input = file.getInputStream();

			fos = new FileOutputStream(realpath + "/" + filename);
			IOUtils.copy(input, fos);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(fos);
		}

		return retpath + "/" + filename;
	}
}
