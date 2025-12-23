package org.example.namespace

class Hello {
    static void sayHello(script, String name) {
        script.echo "Hello, ${name} from a shared library"
    }
}
