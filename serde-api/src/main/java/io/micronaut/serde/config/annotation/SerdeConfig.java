/*
 * Copyright 2017-2021 original authors
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
package io.micronaut.serde.config.annotation;

import io.micronaut.context.annotation.Executable;
import io.micronaut.core.annotation.Internal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.Repeatable;

/**
 * Meta-annotation with meta annotation members that different annotation
 * models can be bind to.
 *
 * <p>This annotation shouldn't be used directly instead a concrete annotation
 * API for JSON like JSON-B or Jackson annotations should be used.</p>
 */
@Internal
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
@Inherited
public @interface SerdeConfig {
    /**
     * Used to store the type that will serialize this type.
     */
    String SERIALIZE_AS = "serAs";

    /**
     * Used to store the type that will deserialize this type.
     */
    String DESERIALIZE_AS = "deserAs";

    /**
     * Whether to validate at compilation time the config.
     */
    String VALIDATE = "validate";

    /**
     * The property to use.
     */
    String PROPERTY = "property";

    /**
     * Is it ignored.
     */
    String IGNORED = "ignored";

    /**
     * Include strategy.
     */
    String INCLUDE = "include";

    /**
     * Is this property to be used only for reading.
     */
    String READ_ONLY = "readOnly";

    /**
     * Is this property to be used only for writing.
     */
    String WRITE_ONLY = "writeOnly";

    /**
     * A type name mapping. Used for subtype binding.
     */
    String TYPE_NAME = "typeName";

    /**
     * A type name mapping. Used for subtype binding.
     */
    String TYPE_NAME_CLASS_SIMPLE_NAME_PLACEHOLDER = "$CLASS_SIMPLE_NAME";

    /**
     * The type property used for subtype binding.
     */
    String TYPE_PROPERTY = "typeProperty";

    /**
     * A property that should be used to wrap this value when serializing.
     */
    String WRAPPER_PROPERTY = "wrapperProperty";

    /**
     * A pattern to use.
     */
    String PATTERN = "pattern";

    /**
     * A locale to use.
     */
    String LOCALE = "locale";

    /**
     * A time zone to use.
     */
    String TIMEZONE = "timezone";

    /**
     * if parsing is required whether to be lenient.
     */
    String LENIENT = "lenient";

    /**
     * Custom serializer class.
     */
    String SERIALIZER_CLASS = "serializerClass";

    /**
     * Custom deserializer class.
     */
    String DESERIALIZER_CLASS = "deserializerClass";

    /**
     * The views an element is part of.
     */
    String VIEWS = "views";

    /**
     * Aliases for deserialization.
     */
    String ALIASES = "aliases";

    /**
     * Naming strategy.
     */
    String NAMING = "naming";

    /**
     * Runtime naming strategy class.
     */
    String RUNTIME_NAMING = "runtimeNaming";

    /**
     * Internal metadata type for wrapped settings.
     */
    @Internal
    @interface Unwrapped {
        String NAME = Unwrapped.class.getName();
        String PREFIX = "prefix";
        String SUFFIX = "suffix";
    }

    /**
     * Internal metadata for a JSON getter.
     */
    @Internal
    @Executable
    @interface Getter {
    }

    /**
     * Ignore handling meta annotation.
     */
    @Internal
    @interface Ignored {
        @interface Type { }
    }

    /**
     * Include property meta annotation.
     */
    @interface Included {
    }

    /**
     * Internal metadata for a JSON any getter.
     */
    @Internal
    @Executable
    @interface AnyGetter {
    }

    /**
     * Internal metadata for a setter.
     */
    @Internal
    @Executable
    @interface Setter {
    }

    /**
     * Internal metadata for any setter.
     */
    @Internal
    @Executable
    @SerdeConfig
    @interface AnySetter {
    }

    /**
     * Used to store errors.
     */
    @Internal
    @interface SerdeError {
    }

    /**
     * Used to store order information.
     */
    @Internal
    @interface PropertyOrder {
    }

    /**
     * For case where property is defined with no name.
     */
    @Internal
    @interface Property {
    }

    /**
     * Managed reference.
     */
    @Internal
    @interface ManagedRef {
    }

    /**
     * Back reference.
     */
    @Internal
    @interface BackRef {
    }

    /**
     * Meta annotations for subtyped mapping.
     */
    @Internal
    @interface Subtyped {
        /**
         * @return the subtypes
         */
        Subtype[] value() default {};
        /**
         * The discriminator to use.
         */
        String DISCRIMINATOR_TYPE = "dt";

        /**
         * The discriminator value to use.
         */
        String DISCRIMINATOR_VALUE = "dv";

        /**
         * The discriminator property to use.
         */
        String DISCRIMINATOR_PROP = "dp";

        enum DiscriminatorType {
            PROPERTY, WRAPPER_OBJECT
        }

        enum DiscriminatorValueKind {
            CLASS_NAME, CLASS_SIMPLE_NAME, NAME
        }

        /**
         * Meta annotation for a mapped subtype.
         */
        @Repeatable(Subtyped.class)
        @interface Subtype {
        }
    }

    /**
     * Meta-annotation used to model the value used during serialization.
     */
    @Internal
    @interface SerValue {
    }

    /**
     * Include strategies for serialization.
     */
    enum SerInclude {

        /**
         * Value that indicates that property is to be always included,
         * independent of value of the property.
         */
        ALWAYS,

        /**
         * Value that indicates that only properties with non-null
         * values are to be included.
         */
        NON_NULL,

        /**
         * Value that indicates that properties are included unless their value
         * is:
         *<ul>
         *  <li>null</li>
         *  <li>"absent" value of a referential type (like Java 8 `Optional`, or
         *     {link java.util.concurrent.atomic.AtomicReference}); that is, something
         *     that would not deference to a non-null value.
         * </ul>
         */
        NON_ABSENT,

        /**
         * Value that indicates that only properties with null value,
         * or what is considered empty, are not to be included.
         */
        NON_EMPTY,

        /**
         * Ignore the property.
         */
        NEVER
    }

    /**
     * Creator mode used when invoking the {@link io.micronaut.core.annotation.Creator}.
     */
    enum CreatorMode {
        /**
         * Use a single argument as the value.
         */
        DELEGATING,
        /**
         * From properties.
         */
        PROPERTIES
    }
}
