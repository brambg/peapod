/*
 * Copyright 2015 Bay of Many
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 * This project is derived from code in the Tinkerpop project under the following license:
 *
 *    Tinkerpop3
 *    http://www.apache.org/licenses/LICENSE-2.0
 */

package peapod;

import com.tinkerpop.gremlin.process.T;
import com.tinkerpop.gremlin.process.Traverser;
import com.tinkerpop.gremlin.process.graph.GraphTraversal;
import com.tinkerpop.gremlin.process.graph.step.map.MapStep;
import com.tinkerpop.gremlin.structure.Contains;
import com.tinkerpop.gremlin.structure.Element;
import com.tinkerpop.gremlin.structure.Vertex;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Extension of {@link com.tinkerpop.gremlin.process.Traversal} supporting framed vertices and edges.
 */
@SuppressWarnings({"unchecked", "unused"})
public class FramedGraphTraversal<S, E> {

    private GraphTraversal<S, E> traversal;
    private FramedGraph graph;

    private Class<E> lastFramingClass;

    private Map<String, Class<E>> stepLabel2FrameClass = new HashMap<>();

    public FramedGraphTraversal(GraphTraversal traversal, FramedGraph graph) {
        this.traversal = traversal;
        this.graph = graph;
    }

    protected FramedGraphTraversal<S, E> label(Class<E> clazz) {
        this.lastFramingClass = clazz;
        traversal.has(T.label, Contains.within, FramerRegistry.instance.get(clazz).subLabels());
        return this;
    }

    public FramedGraphTraversal<S, E> has(final String key) {
        traversal.has(key);
        return this;
    }

    public FramedGraphTraversal<S, E> has(final String key, final Object value) {
        traversal.has(key, value);
        return this;
    }

    public FramedGraphTraversal<S, E> has(final T accessor, final Object value) {
        traversal.has(accessor, value);
        return this;
    }

    public FramedGraphTraversal<S, E> has(final String key, final BiPredicate predicate, final Object value) {
        traversal.has(key, predicate, value);
        return this;
    }

    public FramedGraphTraversal<S, E> has(final T accessor, final BiPredicate predicate, final Object value) {
        traversal.has(accessor, predicate, value);
        return this;
    }

    public FramedGraphTraversal<S, E> has(final String label, final String key, final Object value) {
        traversal.has(label, key, value);
        return this;
    }

    public FramedGraphTraversal<S, E> has(final String label, final String key, final BiPredicate predicate, final Object value) {
        traversal.has(label, key, predicate, value);
        return this;
    }

    public FramedGraphTraversal<S, E> hasNot(final String key) {
        traversal.hasNot(key);
        return this;
    }

    public FramedGraphTraversal<S, E> values(final String... propertyKeys) {
        this.lastFramingClass = null;
        traversal.values(propertyKeys);
        return this;
    }

    public FramedGraphTraversal<S, E> filter(final Predicate<Traverser<E>> predicate) {
        traversal.filter(predicate);
        return this;
    }

    public <E2> FramedGraphTraversal<S, E2> in(final String edgeLabel, Class<E2> clazz) {
        traversal.in(edgeLabel);
        this.lastFramingClass = (Class<E>) clazz;
        return (FramedGraphTraversal<S, E2>) this;
    }

    public <E2> FramedGraphTraversal<S, E2> out(final String edgeLabel, Class<E2> clazz) {
        traversal.out(edgeLabel);
        this.lastFramingClass = (Class<E>) clazz;
        return (FramedGraphTraversal<S, E2>) this;
    }

    public FramedGraphTraversal<S, E> out(String... edgeLabels) {
        traversal.out(edgeLabels);
        return this;
    }

    public FramedGraphTraversal<S, E> in(String... edgeLabels) {
        traversal.in(edgeLabels);
        return this;
    }

    public FramedGraphTraversal<S, E> as(final String label) {
        stepLabel2FrameClass.put(label, lastFramingClass);
        traversal.as(label);
        return this;
    }

    public FramedGraphTraversal<S, E> back(final String label) {
        lastFramingClass = stepLabel2FrameClass.get(label);
        traversal.back(label);
        return this;
    }

    public FramedGraphTraversal<S, E> dedup() {
        traversal.dedup();
        return this;
    }

    public FramedGraphTraversal<S, E> dedup(Function<Traverser<E>, ?> uniqueFunction) {
        traversal.dedup(uniqueFunction);
        return this;
    }

    public FramedGraphTraversal<S, E> except(String variable) {
        traversal.except(variable);
        return this;
    }

    public FramedGraphTraversal<S, E> except(E exceptionObject) {
        traversal.except(exceptionObject);
        return this;
    }

    public FramedGraphTraversal<S, E> except(Collection<E> exceptionCollection) {
        traversal.except(exceptionCollection);
        return this;
    }

    public List<E> toList() {
        addFrameStep(lastFramingClass);
        return traversal.toList();
    }

    public Set<E> toSet() {
        addFrameStep(lastFramingClass);
        return traversal.toSet();
    }

    public E next() {
        addFrameStep(lastFramingClass);
        return traversal.next();
    }

    private <F> void addFrameStep(Class<F> framingClass) {
        if (framingClass == null) {
            return;
        }

        Framer<Element, F> framer = FramerRegistry.instance.get(framingClass);

        MapStep<Vertex, F> mapStep = new MapStep<>(traversal);
        mapStep.setFunction(v -> framer.frame(v.get(), graph));
        traversal.addStep(mapStep);
    }

}