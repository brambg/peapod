/*
 * Copyright 2015-Bay of Many
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
 * This project is derived from code in the TinkerPop project under the following licenses:
 *
 * TinkerPop3
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the TinkerPop nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL TINKERPOP BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package peapod.manytooneedge;

import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.junit.Before;
import org.junit.Test;
import peapod.FramedGraph;
import peapod.GraphTest;

import java.util.List;

import static org.junit.Assert.*;
import static peapod.TinkerPopHelper.out;

public class ManyToOneEdgeTest extends GraphTest {

    private Person alice;
    private Person bob;
    private City london;
    private City madrid;

    @Before
    public void init() {
        Vertex alice = g.addVertex(T.label, "Person", "name", "alice");
        Vertex bob = g.addVertex(T.label, "Person", "name", "bob");
        Vertex london = g.addVertex(T.label, "City", "name", "london");
        Vertex madrid = g.addVertex(T.label, "City", "name", "madrid");

        alice.addEdge("hometown", london);

        FramedGraph graph = new FramedGraph(g, Person.class.getPackage());
        this.alice = graph.v(alice.id());
        this.bob = graph.v(bob.id());
        this.london = graph.v(london.id());
        this.madrid = graph.v(madrid.id());
    }

    @Test
    public void testGetExisting() {
        assertEquals(london, alice.getHometown().getCity());
        assertEquals(alice, alice.getHometown().getPerson());
    }

    @Test
    public void testGetNonExisting() {
        assertNull(bob.getHometown());
    }

    @Test
    public void testSet() {
        Hometown hometown = bob.setHometown(madrid);
        assertNotNull(hometown);

        hometown.setFromYear(2012);

        List<Vertex> vertices = out(bob.vertex(), "hometown");
        assertEquals("madrid", vertices.get(0).value("name"));

        bob.vertex().edges(Direction.OUT, "hometown").forEachRemaining(h ->
                        assertEquals((Integer) 2012, h.value("fromYear"))
        );
    }

    @Test
    public void testSetDifferent() {
        alice.setHometown(madrid);
        alice.vertex().vertices(Direction.OUT, "hometown").forEachRemaining(h ->
                        assertEquals("madrid", h.value("name"))
        );
        assertFalse(london.vertex().edges(Direction.IN, "hometown").hasNext());
    }

    @Test
    public void testSetNull() {
        alice.setHometown(null);
        assertFalse(alice.vertex().vertices(Direction.OUT, "hometown").hasNext());
        assertFalse(london.vertex().vertices(Direction.IN, "hometown").hasNext());
    }
}
