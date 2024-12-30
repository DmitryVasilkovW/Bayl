build:
	./gradlew clean build

run:
	@java -jar build/libs/bayl-1.0.0.jar $(ARGS)