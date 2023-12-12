package com.infosupport.ldoc.support;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;

public interface ProjectFactory {

  Project project(File file) throws IOException;

  Project project(String string) throws IOException;

  Project project(Reader reader) throws IOException;

  Project project(URL url) throws IOException;
}
