import org.example.Services.HelloService

def call(String name = 'user') {
    HelloService.sayHello(this, name)
}
