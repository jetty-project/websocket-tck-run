package org.eclipse.jetty.tck;

public class Extension2 extends org.eclipse.jetty.websocket.core.AbstractExtension
{
    @Override
    public String getName()
    {
        return "secondExtName";
    }
}
