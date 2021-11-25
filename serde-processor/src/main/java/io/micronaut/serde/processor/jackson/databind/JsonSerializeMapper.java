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
package io.micronaut.serde.processor.jackson.databind;

import io.micronaut.core.annotation.AnnotationClassValue;
import io.micronaut.core.annotation.AnnotationValue;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.inject.visitor.VisitorContext;
import io.micronaut.serde.annotation.Serdeable;
import io.micronaut.serde.config.annotation.SerdeConfig;
import io.micronaut.serde.processor.jackson.ValidatingAnnotationMapper;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Support for JsonSerialize(as=MyType).
 */
public class JsonSerializeMapper extends ValidatingAnnotationMapper {
    @Override
    protected List<AnnotationValue<?>> mapValid(AnnotationValue<Annotation> annotation, VisitorContext visitorContext) {
        AnnotationClassValue<?> acv = annotation.annotationClassValue("as").orElse(null);
        List<AnnotationValue<?>> annotations = new ArrayList<>();
        annotations.add(AnnotationValue.builder(Introspected.class).build());
        annotations.add(AnnotationValue.builder(Serdeable.Serializable.class).build());
        if (acv != null) {
            annotations.add(
                    AnnotationValue.builder(SerdeConfig.class)
                            .member(SerdeConfig.SERIALIZE_AS, acv)
                            .build()
            );
        }
        return annotations;
    }

    @Override
    protected Set<String> getSupportedMemberNames() {
        return Collections.singleton("as");
    }

    @Override
    public String getName() {
        return "com.fasterxml.jackson.databind.annotation.JsonSerialize";
    }
}
