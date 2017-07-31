package janus.graph.data.client.transaction;

import janus.graph.data.client.service.GraphAccessService;
import janus.graph.data.client.service.GraphDataAccessException;
import janus.graph.data.model.GraphQueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GremlinTransactionManagerImpl implements GremlinTransactionManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(GremlinTransactionManagerImpl.class);

    private GraphAccessService graphAccessService;

    public GremlinTransactionManagerImpl(GraphAccessService graphAccessService) {
        this.graphAccessService = graphAccessService;
    }

    @Override
    public void setOnReadWriteBehaviour(GraphTransactionReadWriteBehaviour readWriteBehaviour) throws GraphDataAccessException {
        GraphQueryResponse response;
        switch (readWriteBehaviour) {
            case AUTO:
                //response = graphAccessService.submit("graph.tx().onReadWrite(Transaction.READ_WRITE_BEHAVIOR.AUTO)");
                //LOGGER.info("Transaction onReadWrite() behaviour setting result: {}", response.getRawData());
                break;
            case MANUAL:
                //response = graphAccessService.submit("graph.tx().onReadWrite(Transaction.READ_WRITE_BEHAVIOR.MANUAL)");
                //LOGGER.info("Transaction onReadWrite() behaviour setting result: {}", response.getRawData());
                break;
            default:
                throw new IllegalArgumentException(String.format("Specified transaction behaviour '%s' is unsupported", readWriteBehaviour.name()));

        }
    }

    @Override
    public void setOnCloseBehaviour(GraphTransactionCloseBehaviour closeBehaviour) throws GraphDataAccessException {
        GraphQueryResponse response;
        switch (closeBehaviour) {
            case COMMIT:
                //response = graphAccessService.submit("graph.tx().onClose(Transaction.CLOSE_BEHAVIOR.COMMIT)");
                //LOGGER.info("Transaction onClose() behaviour setting result: {}", response.getRawData());
                break;
            case ROLLBACK:
                //response = graphAccessService.submit("graph.tx().onClose(Transaction.CLOSE_BEHAVIOR.ROLLBACK)");
                //LOGGER.info("Transaction onClose() behaviour setting result: {}", response.getRawData());
                break;
            default:
                throw new IllegalArgumentException(String.format("Specified transaction behaviour '%s' is unsupported", closeBehaviour.name()));

        }
    }

    @Override
    public void commit() throws GraphDataAccessException {
//        gremlinGraphAccessService.submit("graph.tx().commit())");
    }

    @Override
    public void rollback() throws GraphDataAccessException {
//        gremlinGraphAccessService.submit("graph.tx().rollback())");
    }

}
