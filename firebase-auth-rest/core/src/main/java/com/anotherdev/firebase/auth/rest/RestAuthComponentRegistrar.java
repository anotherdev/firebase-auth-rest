package com.anotherdev.firebase.auth.rest;

import com.anotherdev.firebase.auth.FirebaseAuthRest;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.internal.InternalAuthProvider;
import com.google.firebase.components.Component;
import com.google.firebase.components.ComponentRegistrar;
import com.google.firebase.components.Dependency;

import java.util.Collections;
import java.util.List;

import timber.log.Timber;

public class RestAuthComponentRegistrar implements ComponentRegistrar {

    @Override
    public List<Component<?>> getComponents() {
        Timber.i("Called getComponents()");
        final Class<InternalAuthProvider> authProviderClass = InternalAuthProvider.class;
        final Class<FirebaseApp> firebaseAppClass = FirebaseApp.class;

        Component<InternalAuthProvider> component = Component.builder(authProviderClass)
                .add(Dependency.required(firebaseAppClass))
                .factory(container -> {
                    FirebaseApp app = container.get(firebaseAppClass);
                    Timber.i("create auth provider: %s for app: %s", authProviderClass, app);
                    return (InternalAuthProvider) FirebaseAuthRest.getInstance(app);
                })
                .build();
        return Collections.singletonList(component);
    }
}
