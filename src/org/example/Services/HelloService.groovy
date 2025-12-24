package org.example.Services

class HelloService {
    static void sayHello(script, String name) {
        script.echo "Hello, ${name} from a shared library"
    }
}
