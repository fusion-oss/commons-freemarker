package com.scoperetail.commons.freemarker;

/*-
 * *****
 * commons-freemarker
 * -----
 * Copyright (C) 2018 - 2022 Scope Retail Systems Inc.
 * -----
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * =====
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class FreemarkerUtils {

  private Configuration cfg;
  private boolean isInit = false;

  // hide constructor
  private FreemarkerUtils() {
    // do nothing
  }

  public static final FreemarkerUtils getInstance(final String templateDir)
      throws ExceptionInInitializerError {
    final FreemarkerUtils instance = new FreemarkerUtils();
    try {
      instance.init(templateDir);
    } catch (IOException e) {
      throw new ExceptionInInitializerError(e);
    }
    return instance;
  }

  public static final FreemarkerUtils getInstance(final Configuration config) {
    final FreemarkerUtils instance = new FreemarkerUtils();
    instance.init(config);
    return instance;
  }

  private void init(final Configuration config) {
    if (!isInit) {
      /* ------------------------------------------------------------------------ */
      /* You should do this ONLY ONCE in the whole application life-cycle:        */

      /* Create and adjust the configuration singleton */
      cfg = config;
      isInit = true;
    }
  }

  public void init(final String templateDir) throws IOException {
    if (!isInit) {
      /* ------------------------------------------------------------------------ */
      /* You should do this ONLY ONCE in the whole application life-cycle:        */

      /* Create and adjust the configuration singleton */
      cfg = new Configuration(Configuration.VERSION_2_3_31);
      // Recommended settings for new projects:
      cfg.setDefaultEncoding("UTF-8");
      cfg.setDirectoryForTemplateLoading(new File(templateDir));
      cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
      cfg.setLogTemplateExceptions(false);
      cfg.setWrapUncheckedExceptions(true);
      cfg.setFallbackOnNullLoopVariable(false);
      isInit = true;
    }
  }

  /**
   * Parse the XML using Apache Freemarker builtin api for XML processing.
   *
   * @param xml
   * @param templateFileName
   * @return String - Output after applying the FTL for given XML document
   */
  public String xmlToString(final String xml, final String templateFileName) {
    try {
      return sourceAsXml(xml, templateFileName);
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (TemplateException e) {
      throw new RuntimeException(e);
    } catch (DocumentException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Parse the XML using Apache Freemarker builtin api for XML processing.
   *
   * @param xml
   * @param templateFileName
   * @return
   * @throws IOException
   * @throws ParserConfigurationException
   * @throws SAXException
   * @throws TemplateException
   */
  private String sourceAsXml(final String xml, final String templateFileName)
      throws DocumentException, TemplateException, IOException {
    Map<String, Object> root = new HashMap<>(1, 1);
    final Document document = DomParser.parse(new StringReader(xml));
    root.put("document", document);
    // StringReader(xml))));
    return process(templateFileName, root);
  }

  /**
   * Converts a JSON to Map using jackson API. See tests for usage and FTL examples.
   *
   * @param json
   * @param templateFileName
   * @return
   */
  public String jsonToString(final String json, final String templateFileName) {
    try {
      return sourceAsJson(json, templateFileName);
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (TemplateException e) {
      throw new RuntimeException(e);
    }
  }

  private String sourceAsJson(final String json, final String templateFileName)
      throws IOException, TemplateException {
    final Map<String, Object> root = new ObjectMapper().readValue(json, Map.class);
    return process(templateFileName, root);
  }

  private String process(final String templateFileName, final Map<String, Object> root)
      throws IOException, TemplateException {
    Template temp = cfg.getTemplate(templateFileName);
    final StringWriter out = new StringWriter();
    temp.process(root, out);
    return out.toString();
  }

  public Configuration getCfg() {
    return cfg;
  }

  public boolean isInit() {
    return isInit;
  }
}
