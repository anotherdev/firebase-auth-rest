package com.anotherdev.firebase.auth.rest;

import com.anotherdev.firebase.auth.FirebaseAuthRest;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.internal.InternalAuthProvider;
import com.google.firebase.components.Component;
import com.google.firebase.components.ComponentRegistrar;
import com.google.firebase.components.Dependency;

import java.util.Collections;
import java.util.List;

public class RestAuthComponentRegistrar implements ComponentRegistrar {

    @Override
    public List<Component<?>> getComponents() {
        Component<InternalAuthProvider> component = Component.builder(InternalAuthProvider.class)
                .add(Dependency.required(FirebaseApp.class))
                .factory(container -> {
                    FirebaseApp app = container.get(FirebaseApp.class);
                    return FirebaseAuthRest.getInstance(app);
                })
                .build();
        return Collections.singletonList(component);
    }
}
