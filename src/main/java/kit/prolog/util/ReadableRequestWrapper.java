package kit.prolog.util;

import org.apache.commons.io.IOUtils;
import org.springframework.lang.Nullable;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

public class ReadableRequestWrapper extends HttpServletRequestWrapper {

    private ByteArrayOutputStream cachedContent;
    @Nullable
    private BufferedReader reader;

    public ReadableRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (cachedContent == null) cacheInputStream();
        return new CachedServletInputStream(cachedContent.toByteArray());
    }

    @Override
    public BufferedReader getReader() throws IOException {
        if (this.reader == null) {
            this.reader = new BufferedReader(new InputStreamReader(getInputStream(), getCharacterEncoding()));
        }
        return this.reader;
    }
    private void cacheInputStream() throws IOException {
        cachedContent = new ByteArrayOutputStream();
        IOUtils.copy(super.getInputStream(), cachedContent);
    }

    private static class CachedServletInputStream extends ServletInputStream {
        private final ByteArrayInputStream buffer;

        public CachedServletInputStream(byte[] contents) {
            this.buffer = new ByteArrayInputStream(contents);
        }

        @Override
        public int read() {
            return buffer.read();
        }

        @Override
        public boolean isFinished() {
            return buffer.available() == 0;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener listener) {
            throw new RuntimeException("Not implemented");
        }
    }
}
