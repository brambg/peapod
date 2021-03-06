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
 * This project is derived from code in the TinkerPop project under the following license:
 *
 *    TinkerPop3
 *    http://www.apache.org/licenses/LICENSE-2.0
 */

package peapod;

import org.apache.tinkerpop.gremlin.structure.Edge;

/**
 * <p>All generated {@code @Edge} classes implement the {@code FramedEdge} interface. It can be optionally
 * defined on the abstract class to get a reference to the wrapped TinkerPop 3 {@code edge}.</p>
 * <pre>
 * &#64;Edge
 * public abstract class Develops implements FramedEdge {}</pre>
 *
 * @author Willem Salembier
 * @see org.apache.tinkerpop.gremlin.structure.Edge
 * @since 0.1
 */
public interface FramedEdge extends FramedElement {

    default Edge edge() {
        return (Edge) element();
    }
}
