package org.eclipse.jetty.tck;

import org.jboss.arquillian.container.jetty.embedded_12_ee10.WebAppContextProcessor;
import org.jboss.arquillian.container.test.spi.client.deployment.ApplicationArchiveProcessor;
import org.jboss.arquillian.core.spi.LoadableExtension;

public class JettyLoadableExtension implements LoadableExtension {
    @Override
    public void register(ExtensionBuilder extensionBuilder) {
        extensionBuilder.service(ApplicationArchiveProcessor.class, JettyApplicationArchiveProcessor.class);
        extensionBuilder.service(WebAppContextProcessor.class, JettyErrorHandlerWebAppContextProcessor.class);
    }
}
