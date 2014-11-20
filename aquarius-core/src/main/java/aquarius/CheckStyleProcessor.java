package aquarius;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic.Kind;

import aquarius.annotation.Grammar;
import aquarius.annotation.RuleDefinition;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({"aquarius.annotation.Grammar", "aquarius.annotation.RuleDefinition"})
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
	 * if verify is failed, report error and exit processor
	 * @param typeElement
	 * must be TypeElement
	 */
	private void verifyParserClass(TypeElement typeElement) {
		// check if is interface
		if(!typeElement.getKind().isInterface()) {
			this.reportErrorAndExit("must be interface", typeElement);
			return;
		}

		// check if implementing one interface and it is Parser interface
		if(typeElement.getInterfaces().size() != 1 
				|| !this.matchClass(typeElement.getInterfaces().get(0), Parser.class)) {
			this.reportErrorAndExit("must implement interface: " + Parser.class, typeElement);
			return;
		}

		// check rule method
		typeElement.getEnclosedElements().parallelStream()
			.filter(ExecutableElement.class::isInstance)
			.map(ExecutableElement.class::cast)
			.forEach(this::verifyRuleMethod);
	}

	private void verifyRuleMethod(ExecutableElement element) {
		// check if default method
		if(!element.isDefault()) {
			this.reportErrorAndExit("must be default method", element);
		}

		// check method parameter and return type
		if(element.getAnnotation(RuleDefinition.class) != null) {
			if(element.getParameters().size() != 0 
					|| !this.matchClass(this.processingEnv.getTypeUtils().erasure(element.getReturnType()), Rule.class)) {
				this.reportErrorAndExit("must be return type: Rule, and has no parameters", element);
			}
		}
	}

	// helper methods
	/**
	 * match annotation 
	 * @param annotation
	 * @param annotationClass
	 * @return
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

	private boolean matchClass(TypeMirror type, Class<?> targetClass) {
		return type.toString().equals(targetClass.getCanonicalName());
	}

	/**
	 * report error and exit processing.
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
}
