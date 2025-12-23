import org.example.namespace.Hello

def call(String name = 'user') {
    pipelineLibrary().Hello.sayHello(this, name)
}
