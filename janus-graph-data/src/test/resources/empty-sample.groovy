import com.thinkaurelius.titan.core.Cardinality
import com.thinkaurelius.titan.core.Multiplicity

// an init script that returns a Map allows explicit setting of global bindings.
def globals = [:]

// Generates the modern graph into an empty graph via LifeCycleHook.
// Note that the name of the key in the "global" map is unimportant.
globals << [hook: [
        onStartUp: { ctx ->
            ctx.logger.info("Loading 'modern' graph data, if necessary.")
            try {
                if (!graph.vertices().hasNext()) {
                    // define the schema/indexes if necessary
                    def mgmt = graph.openManagement()
                    if (!mgmt.getVertexLabels().iterator().hasNext()) {
                        def person = mgmt.makeVertexLabel("person").make()
                        def software = mgmt.makeVertexLabel("software").make()
                        def name = mgmt.makePropertyKey("name").dataType(String.class).cardinality(Cardinality.SINGLE).make()
                        def age = mgmt.makePropertyKey("age").dataType(Integer.class).cardinality(Cardinality.SINGLE).make()
                        def lang = mgmt.makePropertyKey("lang").dataType(String.class).cardinality(Cardinality.SINGLE).make()
                        def knows = mgmt.makeEdgeLabel("knows").multiplicity(Multiplicity.MULTI).make()
                        def created = mgmt.makeEdgeLabel("created").multiplicity(Multiplicity.MULTI).make()
                        def weight = mgmt.makePropertyKey("weight").dataType(Double.class).cardinality(Cardinality.SINGLE).make()
                        def personByName = mgmt.buildIndex("personByName", Vertex.class).addKey(name).indexOnly(person).buildCompositeIndex()
                        def softwareByName = mgmt.buildIndex("softwareByName", Vertex.class).addKey(name).indexOnly(software).buildCompositeIndex()
                    }
                    mgmt.commit()
                }
            } catch (Throwable th) {
                // just in case because Groovy loves eating exceptions
                th.printStackTrace()
            }
        }
] as LifeCycleHook]

// define the default TraversalSource to bind queries to - this one will be named "g".
globals << [g: graph.traversal()]
