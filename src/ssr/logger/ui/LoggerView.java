package ssr.logger.ui;


import java.awt.BorderLayout;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class LoggerView extends JPanel {
	JScrollPane scrollPane;
	OutputStream os;
	JTextArea jTextArea;
	Logger logger;
	
	public LoggerView(String loggerName) {
		super();
		logger = Logger.getLogger(loggerName);
		jTextArea = new JTextArea();
		scrollPane = new JScrollPane(jTextArea);
		os = new JTextAreaOutputStream(jTextArea, "UTF-8");
		this.setLayout(new BorderLayout());
		this.add(BorderLayout.CENTER, scrollPane);
		
		
		logger.addHandler(new AutoFlushStreamHandler(new PrintStream(os, true), new SimpleFormatter()));
	}

	
	public class AutoFlushStreamHandler extends StreamHandler {

		public AutoFlushStreamHandler() {
			super();
		}

		public AutoFlushStreamHandler(OutputStream arg0, Formatter arg1) {
			super(arg0, arg1);
		}

		/* (non-Javadoc)
		 * @see java.util.logging.StreamHandler#publish(java.util.logging.LogRecord)
		 */
		@Override
		public synchronized void publish(LogRecord arg0) {
			super.publish(arg0);
			super.flush();
		}
	}
	
	
	public class JTextAreaOutputStream extends OutputStream {
		private ByteArrayOutputStream os;
		private JTextArea textArea;
		private String encode;

		public JTextAreaOutputStream(JTextArea textArea, String encode) {
			super();
			this.os = new ByteArrayOutputStream();
			this.textArea = textArea;
			this.encode = encode;
		}
		
		public void write(int arg) throws IOException {
			this.os.write(arg);
		}
	
		
		@Override
		public void flush() throws IOException {
			// ?½?½?½?½?½?½ÌƒG?½?½?½R?½[?½h
			final String str = new String(this.os.toByteArray(), this.encode);
			
			SwingUtilities.invokeLater(new Runnable(){
				public void run() {
					JTextAreaOutputStream.this.textArea.append(str);
				}
			});
			// ?½?½?½?½?½o?½?½?½?½?½?½?½e?½ÍƒN?½?½?½A?½?½?½?½
			this.os.reset();
		}
	}

}
