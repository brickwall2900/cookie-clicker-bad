package com.github.brickwall2900.cookie;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Objects;

public class ClassResource implements AutoCloseable {
	private final String resource;
	private final InputStream stream;
	private final URL url;
	
	public ClassResource(String resource) {
		this(resource, ClassLoader.getSystemClassLoader());
	}
	
	public ClassResource(String resource, Class<?> classToLoad) {
		this(resource, classToLoad.getClassLoader() != null ? classToLoad.getClassLoader() : ClassLoader.getSystemClassLoader());
	}
	
	public ClassResource(String resource, ClassLoader loader) {
		this.resource = resource;
		this.stream = Objects.requireNonNull(loader.getResourceAsStream(resource));
		this.url = loader.getResource(resource);
	}

	public String getResource() {
		return resource;
	}

	public InputStream getStream() {
		return stream;
	}

	public URL getURL() {
		return url;
	}

	public String read() throws IOException, NullPointerException {
		return new String(stream.readAllBytes(), Charset.defaultCharset());
	}

	@Override
	public String toString() {
		return "ClassResource[resource=" + resource + ", stream=" + stream + ", url=" + url + "]";
	}

	@Override
	public void close() {
		try {
			stream.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
