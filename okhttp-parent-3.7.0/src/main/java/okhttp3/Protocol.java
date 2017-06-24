/*
 * Copyright (C) 2014 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package okhttp3;

import java.io.IOException;
import java.net.URI;
import java.net.URL;

/**
 * Protocols that OkHttp implements for <a
 * href="http://tools.ietf.org/html/draft-ietf-tls-applayerprotoneg">ALPN</a> selection.
 * <p>
 * <h3>Protocol vs Scheme</h3> Despite its name, {@link java.net.URL#getProtocol()} returns the
 * {@linkplain java.net.URI#getScheme() scheme} (http, https, etc.) of the URL, not the protocol
 * (http/1.1, spdy/3.1, etc.). OkHttp uses the word <i>protocol</i> to identify how HTTP messages
 * are framed.
 * <p>
 * 用{@link URL#getProtocol()}和{@link URI#getScheme()}都只能获取到一个笼统的协议名称（如http、https），
 * 而在OkHttp中，{@link Protocol}就包含了HTTP根据体的信息（如http/1.1，spdy/3.1等）
 */
public enum Protocol {
    /**
     * An obsolete plaintext framing that does not use persistent sockets by default.
     */
    HTTP_1_0("http/1.0"),

    /**
     * A plaintext framing that includes persistent connections.
     * <p>
     * <p>This version of OkHttp implements <a href="http://www.ietf.org/rfc/rfc2616.txt">RFC
     * 2616</a>, and tracks revisions to that spec.
     */
    HTTP_1_1("http/1.1"),

    /**
     * Chromium's binary-framed protocol that includes header compression, multiplexing multiple
     * requests on the same socket, and server-push. HTTP/1.1 semantics are layered on SPDY/3.
     * <p>
     * <p>Current versions of OkHttp do not support this protocol.
     *
     * @deprecated OkHttp has dropped support for SPDY. Prefer {@link #HTTP_2}.
     */
    SPDY_3("spdy/3.1"),

    /**
     * The IETF's binary-framed protocol that includes header compression, multiplexing multiple
     * requests on the same socket, and server-push. HTTP/1.1 semantics are layered on HTTP/2.
     * <p>
     * <p>HTTP/2 requires deployments of HTTP/2 that use TLS 1.2 support {@linkplain
     * CipherSuite#TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256} , present in Java 8+ and Android 5+. Servers
     * that enforce this may send an exception message including the string {@code
     * INADEQUATE_SECURITY}.
     */
    HTTP_2("h2");

    private final String protocol;

    Protocol(String protocol) {
        this.protocol = protocol;
    }

    /**
     * Returns the protocol identified by {@code protocol}.
     *
     * @throws IOException if {@code protocol} is unknown.
     */
    public static Protocol get(String protocol) throws IOException {
        // Unroll the loop over values() to save an allocation.
        if (protocol.equals(HTTP_1_0.protocol)) return HTTP_1_0;
        if (protocol.equals(HTTP_1_1.protocol)) return HTTP_1_1;
        if (protocol.equals(HTTP_2.protocol)) return HTTP_2;
        if (protocol.equals(SPDY_3.protocol)) return SPDY_3;
        throw new IOException("Unexpected protocol: " + protocol);
    }

    /**
     * Returns the string used to identify this protocol for ALPN, like "http/1.1", "spdy/3.1" or
     * "h2".
     */
    @Override
    public String toString() {
        return protocol;
    }
}
