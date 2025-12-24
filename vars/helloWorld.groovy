import org.example.namespace.Hello

def call(String name = 'user') {
    HelloService.sayHello(this, name)
}
