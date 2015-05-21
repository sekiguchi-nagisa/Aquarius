/*
 * Copyright (C) 2014-2015 Nagisa Sekiguchi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package aquarius;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.AbstractElementVisitor8;
import javax.tools.Diagnostic.Kind;
import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * for verification of user defined parser interface.
 *
 * @author skgchxngsxyz-opensuse
 */
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({"aquarius.Grammar"})
public class CheckStyleProcessor extends AbstractProcessor {
    private final static boolean debugMode = false;

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        annotations.stream()
                .filter(anno -> this.matchAnnotation(anno, Grammar.class))
                .forEach(anno ->
                                roundEnv.getElementsAnnotatedWith(anno).stream()
                                        .filter(TypeElement.class::isInstance)
                                        .map(TypeElement.class::cast)
                                        .forEach(this::verifyParserClass)
                );
        return false;
    }

    /**
     * verify parser class.
     * if verify is failed, report error and exit processor.
     *
     * @param typeElement
     */
    private void verifyParserClass(TypeElement typeElement) {
        // check if is interface
        if(!typeElement.getKind().isInterface()) {
            this.reportErrorAndExit("must be interface", typeElement);
            return;
        }

        // check access level
        if(!typeElement.getModifiers().contains(Modifier.PUBLIC)) {
            this.reportErrorAndExit("must be public", typeElement);
            return;
        }

        // check if implementing one interface and it is Parser interface
        if(typeElement.getInterfaces().size() != 1
                || !this.matchClass(typeElement.getInterfaces().get(0), Parser.class)) {
            this.reportErrorAndExit("must implement interface: " + Parser.class, typeElement);
            return;
        }

        // verify
        ElementVerifier verifier = new ElementVerifier();
        typeElement.getEnclosedElements().forEach(verifier::visit);
    }

    // helper methods

    /**
     * check if TypeElement is correspond to Annotation class.
     *
     * @param annotation
     * @param annotationClass
     * @return return true if annotation is correspond to annotationClasss.
     */
    private boolean matchAnnotation(TypeElement annotation, Class<? extends Annotation> annotationClass) {
        this.debugPrint("target name-> " + annotationClass.getSimpleName());
        if(annotation.getKind() != ElementKind.ANNOTATION_TYPE) {
            return false;
        }
        String name = annotation.getQualifiedName().toString();
        this.debugPrint("annotation name-> " + name);
        return name.equals(annotationClass.getCanonicalName());
    }

    /**
     * check if TypeMirror is correspond to Class.
     *
     * @param type
     * @param targetClass
     * @return return true if type is correspond to targetClass.
     */
    private boolean matchClass(TypeMirror type, Class<?> targetClass) {
        String typeName = type.toString();
        int index = typeName.indexOf('<');
        if(index != -1) {
            typeName = typeName.substring(0, index);
        }
        return typeName.equals(targetClass.getCanonicalName());
    }

    /**
     * report error and exit processing.
     *
     * @param message
     * @param element
     */
    private void reportErrorAndExit(String message, Element element) {
        this.processingEnv.getMessager().printMessage(Kind.ERROR, message, element);
    }

    private void debugPrint(String message) {
        if(debugMode) {
            this.processingEnv.getMessager().printMessage(Kind.NOTE, "DEBUG: " + message);
        }
    }

    class ElementVerifier extends AbstractElementVisitor8<Void, Void> {

        @Override
        public Void visitPackage(PackageElement e, Void aVoid) {
            reportErrorAndExit("unsupported element", e);
            return null;
        }

        @Override
        public Void visitType(TypeElement e, Void aVoid) {
            reportErrorAndExit("unsupported element", e);
            return null;
        }

        @Override
        public Void visitVariable(VariableElement e, Void aVoid) {
            if(!e.getModifiers().contains(Modifier.STATIC)) {
                reportErrorAndExit("must be static field", e);
            }

            if(matchClass(processingEnv.getTypeUtils().erasure(e.asType()), Rule.class)) {
                reportErrorAndExit("field class must not be Rule", e);
            }
            return null;
        }

        @Override
        public Void visitExecutable(ExecutableElement e, Void aVoid) {
            // check static method
            if(e.getModifiers().contains(Modifier.STATIC)) {
                if(matchClass(processingEnv.getTypeUtils().erasure(e.getReturnType()), Rule.class)) {
                    reportErrorAndExit("static method's return type must not be Rule.", e);
                }
                return null;
            }

            // check if default method
            if(!e.isDefault()) {
                reportErrorAndExit("must be default method", e);
            }

            // check method parameter and return type
            if(e.getParameters().size() != 0
                    || !matchClass(processingEnv.getTypeUtils().erasure(e.getReturnType()), Rule.class)) {
                reportErrorAndExit("must be return type: Rule, and has no parameters", e);
            }
            return null;
        }

        @Override
        public Void visitTypeParameter(TypeParameterElement e, Void aVoid) {
            reportErrorAndExit("unsupported element", e);
            return null;
        }
    }
}
