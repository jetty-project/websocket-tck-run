package org.eclipse.jetty.tck;


import org.eclipse.jetty.ee10.webapp.WebAppContext;
import org.jboss.arquillian.container.jetty.embedded_12_ee10.WebAppContextProcessor;
import org.jboss.shrinkwrap.api.Archive;

import java.net.URISyntaxException;
import java.net.URL;

public class JettyErrorHandlerWebAppContextProcessor implements WebAppContextProcessor {

    @Override
    public void process(WebAppContext webAppContext, Archive<?> archive) {
        // just an example if needed
        if ("servlet_spec_fragment_web.war".equals(archive.getName())) {
            URL url = Thread.currentThread().getContextClassLoader()
                    .getResource("servlet_spec_fragment_web/webdefault.xml");
            try {
                webAppContext.setDefaultsDescriptor(url.toURI().getPath());
            } catch (URISyntaxException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
    }
}
