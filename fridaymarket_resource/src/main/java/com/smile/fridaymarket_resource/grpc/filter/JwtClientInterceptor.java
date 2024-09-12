package com.smile.fridaymarket_resource.grpc.filter;

import io.grpc.*;

public class JwtClientInterceptor implements ClientInterceptor {

    private static final Metadata.Key<String> AUTHORIZATION_HEADER = Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER);

    private final String accessToken;

    public JwtClientInterceptor(String accessToken) {

        this.accessToken = accessToken;
    }

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> methodDescriptor,
                                                               CallOptions callOptions, Channel channel) {

        return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(channel.newCall(methodDescriptor, callOptions)) {
            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {

                headers.put(AUTHORIZATION_HEADER, "Bearer " + accessToken);
                super.start(responseListener, headers);
            }
        };
    }

}
