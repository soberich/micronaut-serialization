package io.micronaut.serde.jackson.annotation

import io.micronaut.core.type.Argument
import io.micronaut.serde.jackson.JsonCompileSpec
import spock.lang.Requires

class JsonUnwrappedSpec extends JsonCompileSpec {

    void "test @JsonUnwrapped conflict"() {
        when:
        def context = buildContext("""
package unwrapped;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
@Introspected(accessKind = Introspected.AccessKind.FIELD)
class Parent {
  public int age;
  public String first;
  @JsonUnwrapped
  public Name name;
}

@Serdeable
@Introspected(accessKind = Introspected.AccessKind.FIELD)
class Name {
  public String first, last;
}
""")

        then:
        def e = thrown(RuntimeException)
        e.message.contains("Unwrapped property contains a property [first] that conflicts with an existing property of the outer type: unwrapped.Parent")
    }

    void "test @JsonUnwrapped conflict methods"() {
        when:
        def context = buildContext("""
package unwrapped;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
class Parent {
  private String first;
  private Name name;
  public void setFirst(String first) {
    this.first = first;
  }
  public String getFirst() {
    return first;
  }
  @JsonUnwrapped
  public void setName(unwrapped.Name name) {
        this.name = name;
  }
  @JsonUnwrapped
  public unwrapped.Name getName() {
    return name;
  }
}

@Serdeable
class Name {
  private String first;
  public void setFirst(String first) {
    this.first = first;
  }
  public String getFirst() {
    return first;   
  }
}
""")

        then:
        def e = thrown(RuntimeException)
        e.message.contains("Unwrapped property contains a property [first] that conflicts with an existing property of the outer type: unwrapped.Parent")
    }

    @Requires({ jvm.isJava17Compatible() })
    void "test @JsonUnwrapped conflict records"() {
        when:
        def context = buildContext("""
package unwrapped;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
record Parent (
  String first,
  @JsonUnwrapped
  Name name
) {}

@Serdeable
record Name (String first) {}
""")

        then:
        def e = thrown(RuntimeException)
        e.message.contains("Unwrapped property contains a property [first] that conflicts with an existing property of the outer type: unwrapped.Parent")
    }

    void "test @JsonUnwrapped"() {
        given:
        def context = buildContext("""
package unwrapped;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
@Introspected(accessKind = Introspected.AccessKind.FIELD)
class Parent {
  public int age;
  @JsonUnwrapped
  public Name name;
}

@Serdeable
@Introspected(accessKind = Introspected.AccessKind.FIELD)
class Name {
  public String first, last;
}
""")

        when:
        def name = newInstance(context, 'unwrapped.Name', [first:"Fred", last:"Flinstone"])
        def parent = newInstance(context, 'unwrapped.Parent', [age:10, name:name])

        def result = writeJson(jsonMapper, parent)

        then:
        result == '{"age":10,"first":"Fred","last":"Flinstone"}'

        when:
        def read = jsonMapper.readValue(result, Argument.of(context.classLoader.loadClass('unwrapped.Parent')))

        then:
        read.age == 10
        read.name.first == 'Fred'
        read.name.last == "Flinstone"

        cleanup:
        context.close()
    }

    @Requires({ jvm.isJava17Compatible() })
    void "test @JsonUnwrapped records"() {
        given:
        def context = buildContext("""
package unwrapped;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
record Parent(
  int age,
  @JsonUnwrapped
  Name name) {
}

@Serdeable
record Name(
  String first, String last
) {}
""")

        when:
        def name = newInstance(context, 'unwrapped.Name', "Fred", "Flinstone")
        def parent = newInstance(context, 'unwrapped.Parent', 10, name)

        def result = writeJson(jsonMapper, parent)

        then:
        result == '{"age":10,"first":"Fred","last":"Flinstone"}'

        when:
        def read = jsonMapper.readValue(result, Argument.of(context.classLoader.loadClass('unwrapped.Parent')))

        then:
        read.age == 10
        read.name.first == 'Fred'
        read.name.last == "Flinstone"

        cleanup:
        context.close()
    }

    void "test @JsonUnwrapped - prefix/suffix"() {
        given:
        def context = buildContext("""
package unwrapped;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
@Introspected(accessKind = Introspected.AccessKind.FIELD)
class Parent {
  public int age;
  @JsonUnwrapped(prefix = "n_", suffix = "_x")
  public Name name;
}

@Serdeable
@Introspected(accessKind = Introspected.AccessKind.FIELD)
class Name {
  public String first, last;
}
""")

        when:
        def name = newInstance(context, 'unwrapped.Name', [first:"Fred", last:"Flinstone"])
        def parent = newInstance(context, 'unwrapped.Parent', [age:10, name:name])

        def result = writeJson(jsonMapper, parent)

        then:
        result == '{"age":10,"n_first_x":"Fred","n_last_x":"Flinstone"}'

        when:
        def read = jsonMapper.readValue(result, Argument.of(context.classLoader.loadClass('unwrapped.Parent')))

        then:
        read.age == 10
        read.name.first == 'Fred'
        read.name.last == "Flinstone"

        cleanup:
        context.close()
    }

    void "test @JsonUnwrapped - constructor args"() {
        given:
        def context = buildContext("""
package unwrapped;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
@Introspected(accessKind = Introspected.AccessKind.FIELD)
class Parent {
  public int age;
  @JsonUnwrapped
  public Name name;
}

@Serdeable
@Introspected(accessKind = Introspected.AccessKind.FIELD)
class Name {
  public final String first, last;
  Name(String first, String last) {
      this.first = first;
      this.last = last;
  }
}
""")

        when:
        def name = newInstance(context, 'unwrapped.Name', "Fred", "Flinstone")
        def parent = newInstance(context, 'unwrapped.Parent', [age:10, name:name])

        def result = writeJson(jsonMapper, parent)

        then:
        result == '{"age":10,"first":"Fred","last":"Flinstone"}'

        when:
        def read = jsonMapper.readValue(result, Argument.of(context.classLoader.loadClass('unwrapped.Parent')))

        then:
        read.age == 10
        read.name.first == 'Fred'
        read.name.last == "Flinstone"

        cleanup:
        context.close()
    }

    void "test @JsonUnwrapped - constructor args - prefix/suffix"() {
        given:
        def context = buildContext("""
package unwrapped;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
@Introspected(accessKind = Introspected.AccessKind.FIELD)
class Parent {
  public int age;
  @JsonUnwrapped(prefix = "n_", suffix = "_x")
  public Name name;
}

@Serdeable
@Introspected(accessKind = Introspected.AccessKind.FIELD)
class Name {
  public final String first, last;
  Name(String first, String last) {
      this.first = first;
      this.last = last;
  }
}
""")

        when:
        def name = newInstance(context, 'unwrapped.Name', "Fred", "Flinstone")
        def parent = newInstance(context, 'unwrapped.Parent', [age:10, name:name])

        def result = writeJson(jsonMapper, parent)

        then:
        result == '{"age":10,"n_first_x":"Fred","n_last_x":"Flinstone"}'

        when:
        def read = jsonMapper.readValue(result, Argument.of(context.classLoader.loadClass('unwrapped.Parent')))

        then:
        read.age == 10
        read.name.first == 'Fred'
        read.name.last == "Flinstone"

        cleanup:
        context.close()
    }

    void "test @JsonUnwrapped - parent constructor args"() {
        given:
        def context = buildContext("""
package unwrapped;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
@Introspected(accessKind = Introspected.AccessKind.FIELD)
class Parent {
  public final int age;
  @JsonUnwrapped
  public final Name name;
  
  Parent(int age, @JsonUnwrapped Name name) {
      this.age = age;
      this.name = name;
  }
}

@Serdeable
@Introspected(accessKind = Introspected.AccessKind.FIELD)
class Name {
  public final String first, last;
  Name(String first, String last) {
      this.first = first;
      this.last = last;
  }
}
""")

        when:
        def name = newInstance(context, 'unwrapped.Name', "Fred", "Flinstone")
        def parent = newInstance(context, 'unwrapped.Parent', 10, name)

        def result = writeJson(jsonMapper, parent)

        then:
        result == '{"age":10,"first":"Fred","last":"Flinstone"}'

        when:
        def read = jsonMapper.readValue(result, Argument.of(context.classLoader.loadClass('unwrapped.Parent')))

        then:
        read.age == 10
        read.name.first == 'Fred'
        read.name.last == "Flinstone"

        cleanup:
        context.close()
    }
}
