/*
 * Copyright 2017-2020 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.core.beans;

import io.micronaut.core.annotation.AnnotationMetadata;
import io.micronaut.core.annotation.Internal;
import io.micronaut.core.annotation.UsedByGeneratedCode;
import io.micronaut.core.reflect.ReflectionUtils;
import io.micronaut.core.type.Argument;
import io.micronaut.core.util.ArgumentUtils;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;

import java.util.Objects;

/**
 * Abstract implementation of {@link BeanProperty}. Generated byte code should be used to create a subclass
 * and implement the {@link BeanProperty#get(Object)} and {@link BeanProperty#set(Object, Object)} methods. This class is subclasses at compilation time by generated byte code and should not be used directly.
 *
 * @param <B> The bean type
 * @param <P> The property type
 *
 * @author graemerocher
 * @since 1.1
 */
@UsedByGeneratedCode
@Internal
public abstract class AbstractBeanProperty<B, P> implements BeanProperty<B, P> {

    private final BeanIntrospection<B> introspection;
    private final Class<B> beanType;
    private final Class<P> type;
    private final String name;
    private final AnnotationMetadata annotationMetadata;
    private final Argument[] typeArguments;

    /**
     * Default constructor.
     * @param introspection The parent introspection
     * @param type The property type
     * @param name The property name
     * @param annotationMetadata The annotation metadata
     * @param typeArguments optional type arguments
     */
    @Internal
    @UsedByGeneratedCode
    protected AbstractBeanProperty(
            @NonNull BeanIntrospection<B> introspection,
            @NonNull Class<P> type,
            @NonNull String name,
            @Nullable AnnotationMetadata annotationMetadata,
            @Nullable Argument[] typeArguments) {
        this.introspection = introspection;
        this.type = type;
        this.beanType = introspection.getBeanType();
        this.name = name;
        this.annotationMetadata = annotationMetadata == null ? AnnotationMetadata.EMPTY_METADATA : annotationMetadata;
        this.typeArguments = typeArguments;
    }

    @Override public @NonNull String getName() {
        return name;
    }

    @NonNull
    @Override
    public Class<P> getType() {
        return type;
    }

    @Override
    public Argument<P> asArgument() {
        if (typeArguments != null) {
            return Argument.of(type, name, getAnnotationMetadata(), typeArguments);
        } else {
            return Argument.of(type, name, getAnnotationMetadata());
        }
    }

    @NonNull
    @Override
    public final BeanIntrospection<B> getDeclaringBean() {
        return introspection;
    }

    @Override
    public AnnotationMetadata getAnnotationMetadata() {
        return annotationMetadata;
    }

    @Nullable
    @Override
    public final P get(@NonNull B bean) {
        ArgumentUtils.requireNonNull("bean", bean);

        if (!beanType.isInstance(bean)) {
            throw new IllegalArgumentException("Invalid bean [" + bean + "] for type: " + introspection.getBeanType());
        }
        if (isWriteOnly()) {
            throw new UnsupportedOperationException("Cannot read from a write-only property");
        }
        return readInternal(bean);
    }

    @Override
    public B withValue(@NonNull B bean, @Nullable P value) {
        ArgumentUtils.requireNonNull("bean", bean);

        if (!beanType.isInstance(bean)) {
            throw new IllegalArgumentException("Invalid bean [" + bean + "] for type: " + introspection.getBeanType());
        }
        if (value == get(bean)) {
            return bean;
        } else {
            return withValueInternal(bean, value);
        }
    }

    @Override
    public final void set(@NonNull B bean, @Nullable P value) {
        ArgumentUtils.requireNonNull("bean", bean);

        if (!beanType.isInstance(bean)) {
            throw new IllegalArgumentException("Invalid bean [" + bean + "] for type: " + introspection.getBeanType());
        }
        if (isReadOnly()) {
            throw new UnsupportedOperationException("Cannot write a read-only property: " + getName());
        }
        if (value != null && !ReflectionUtils.getWrapperType(getType()).isInstance(value)) {
            throw new IllegalArgumentException("Specified value [" + value + "] is not of the correct type: " + getType());
        }
        /*
        if (value == null && isNonNull()) {
            throw new IllegalArgumentException("Null values not supported by property: " + getName());
        }
         */
        writeInternal(bean, value);
    }


    /**
     * Mutates a property value.
     * @param bean The bean
     * @param value The value
     * @see BeanProperty#withValue(Object, Object)
     * @return Either a copy of the bean with the copy constructor invoked or the mutated instance if it mutable
     */
    @SuppressWarnings("WeakerAccess")
    @UsedByGeneratedCode
    @Internal
    protected B withValueInternal(B bean, P value) {
        return BeanProperty.super.withValue(bean, value);
    }

    /**
     * Writes a property value.
     * @param bean The bean
     * @param value The value
     */
    @SuppressWarnings("WeakerAccess")
    @UsedByGeneratedCode
    @Internal
    protected abstract void writeInternal(@NonNull B bean, @Nullable P value);

    /**
     * Reads the bean property.
     * @param bean The bean
     * @return THe value
     */
    @SuppressWarnings("WeakerAccess")
    @UsedByGeneratedCode
    @Internal
    protected abstract P readInternal(@NonNull B bean);

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AbstractBeanProperty<?, ?> that = (AbstractBeanProperty<?, ?>) o;
        return Objects.equals(beanType, that.beanType) &&
                Objects.equals(type, that.type) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(beanType, type, name);
    }

    @Override
    public String toString() {
        return "BeanProperty{" +
                "beanType=" + beanType +
                ", type=" + type +
                ", name='" + name + '\'' +
                '}';
    }
}
