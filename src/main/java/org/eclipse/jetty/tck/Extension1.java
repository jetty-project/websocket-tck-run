package org.eclipse.jetty.tck;

public class Extension1 extends org.eclipse.jetty.websocket.core.AbstractExtension
{
    @Override
    public String getName()
    {
        return "firstExtName";
    }
}
