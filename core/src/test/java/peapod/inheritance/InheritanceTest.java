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

package peapod.inheritance;

import com.tinkerpop.gremlin.process.T;
import com.tinkerpop.gremlin.structure.Graph;
import com.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import org.junit.Before;
import org.junit.Test;
import peapod.FramedGraph;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class InheritanceTest {

    private FramedGraph graph;

    @Before
    public void init() {
        Graph g = TinkerGraph.open();
        g.addVertex(T.id, 1, T.label, "person", "name", "alice");
        g.addVertex(T.id, 2, T.label, "programmer", "name", "bob", "yearsExperience", 10);

        graph = new FramedGraph(g);
    }

    @Test
    public void testFind() {
        List<Person> persons = graph.V(Person.class).toList();
        assertEquals(2, persons.size());
    }
}