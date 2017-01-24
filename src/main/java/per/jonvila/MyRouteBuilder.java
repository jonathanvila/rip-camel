package per.jonvila;

import org.apache.camel.builder.RouteBuilder;

/**
 * A Camel Java DSL Router
 */
public class MyRouteBuilder extends RouteBuilder {

    /**
     * Let's configure the Camel routing rules using Java code...
     */
    public void configure() {

        from("activemq:queue:batchprocess.queue")
                .multicast().to("direct:imageFeed", "direct:bopFeed", "direct:taxFeed");

        from("direct:imageFeed").to("http://mywebservice").split(xpath("//Sku"))
                .setHeader("CorrelationId", xpath("/Sku/@Id"))
                .to("direct:aggregator");
        from("direct:taxFeed");

        from("direct:aggregator").aggregate(header("CorrelationId")).completionSize(10).to("direct:finish");
    }

}
