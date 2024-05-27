/**
 * HostUtil.java
 * 4 Apr 2016
 * @author Daniel McCue
 */

package com.synadek.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 */
public final class HostUtil {

	/**
	 * Acquire a reference to the application logger.
	 */
	private static Logger log = LogManager.getLogger(HostUtil.class.getPackage().getName());

	/**
	 * Buffer size for file read.
	 */
	private static final int BUFFER_SIZE = 1024;

	/**
	 * execute a Linux command using bash.
	 * 
	 * @param command         the command string to execute
	 * @param waitForResponse true means await completion of the command and return
	 *                        the response. False means don't wait and return an
	 *                        empty string as the response.
	 * @return the response from the command or empty string (if no wait is
	 *         selected)
	 */
	public static String executeCommand(final String command, final boolean waitForResponse) {

		log.debug("Executing Linux command: {}", command);

		String response = "";
		final ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
		pb.redirectErrorStream(true);
		Process proc;
		try {
			proc = pb.start();
		} catch (IOException err) {
			log.error(err);
			return "failed";
		}

		// Capture response from command
		try (final InputStream is = proc.getInputStream();
				final OutputStream os = proc.getOutputStream();
				final InputStream es = proc.getErrorStream();) {
			if (waitForResponse) {
				// Wait for the process to finish and get the return code
				final int status = proc.waitFor();
				if (status != 0) {
					log.debug("Returned status: {}", Integer.valueOf(status));
				}
				// Read the response and return it as a string
				response = convertStreamToStr(is);
			}
		} catch (IOException | InterruptedException err) {
			log.error("Error occured while executing Linux command, '{}': {}", command, err);
		}

		return response;
	}

	/**
	 * Convert an InputStream to String.
	 * 
	 * @param is the input stream
	 * @return the complete input stream as a string
	 * @throws IOException if an error occurs
	 */
	public static String convertStreamToStr(final InputStream is) throws IOException {
		if (is != null) {
			try (final Writer writer = new StringWriter();) {
				final char[] buffer = new char[BUFFER_SIZE];
				try (final Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));) {
					int count;
					while ((count = reader.read(buffer)) != -1) {
						writer.write(buffer, 0, count);
					}
				}
				return writer.toString();
			}
		}
		return "";
	}
}
