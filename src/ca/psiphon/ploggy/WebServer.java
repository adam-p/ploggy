/*
 * Copyright (c) 2013, Psiphon Inc.
 * All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package ca.psiphon.ploggy;

import java.io.IOException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.Map;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.TrustManager;

import ca.psiphon.ploggy.Events.Request;
import ca.psiphon.ploggy.Events.Response;

import com.squareup.otto.Subscribe;

import fi.iki.elonen.NanoHTTPD;

public class WebServer extends NanoHTTPD implements NanoHTTPD.AsyncRunner {

    // TODO: see https://github.com/NanoHttpd/nanohttpd/blob/master/webserver/src/main/java/fi/iki/elonen/SimpleWebServer.java
	
    public WebServer(TransportSecurity.TransportKeyPair transportKeyPair) throws IOException {
        // Specifying port 0 so OS will pick any available ephemeral port
        super(0);
		makeSecure(
		        TransportSecurity.getSSLSocketFactory(transportKeyPair),
		        TransportSecurity.getRequiredTransportProtocols(),
		        TransportSecurity.getRequiredTransportCipherSuites());
        setAsyncRunner(this);
    }

    @Override
    public void exec(Runnable webRequestTask) {
        // TODO: verify that either InterruptedException is thrown, or check Thread.isInterrupted(), in NanoHTTPD request handling Runnables        
        Engine.getInstance().submitTask(webRequestTask);
    }

    @Override
    public Response serve(String uri, Method method, Map<String, String> headers, Map<String, String> parms, Map<String, String> files) {
        return null;
    }
}
