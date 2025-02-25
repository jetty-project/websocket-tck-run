import org.eclipse.jetty.websocket.core.Extension;

module org.eclipse.jetty.tck
{
    requires org.eclipse.jetty.websocket.core.common;

    exports org.eclipse.jetty.tck;

    uses Extension;

    provides Extension with
        org.eclipse.jetty.tck.Extension1,
        org.eclipse.jetty.tck.Extension2,
        org.eclipse.jetty.tck.Extension3;
}
