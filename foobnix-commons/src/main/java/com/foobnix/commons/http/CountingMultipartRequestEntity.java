package com.foobnix.commons.http;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.http.entity.mime.MultipartEntity;

public class CountingMultipartRequestEntity extends MultipartEntity {
	private final ProgressListener listener;

	public CountingMultipartRequestEntity(final ProgressListener listener) {
		super();
		this.listener = listener;
	}

	@Override
	public void writeTo(final OutputStream out) throws IOException {
		if (listener != null) {
			super.writeTo(new CountingOutputStream(out, this.listener));
		} else {
			super.writeTo(out);
		}
	}

	public static class CountingOutputStream extends FilterOutputStream {

		private final ProgressListener listener;

        private long transferred;

        public CountingOutputStream(final OutputStream out, final ProgressListener listener) {
			super(out);
			this.listener = listener;
			this.transferred = 0;
		}

        public void write(byte[] b, int off, int len) throws IOException {
			out.write(b, off, len);
			this.transferred += len;
			this.listener.transferred(this.transferred);
		}

        public void write(int b) throws IOException {
			out.write(b);
			this.transferred++;
			this.listener.transferred(this.transferred);
		}
	}
}