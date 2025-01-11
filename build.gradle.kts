fun properties(key: String) = providers.gradleProperty(key)

group = properties("group").get()
version = properties("version").get()
