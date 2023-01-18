package nl.tudelft.sem.template.activity.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.net.http.HttpResponse;
import nl.tudelft.sem.template.common.models.activity.TypesOfPositions;
import nl.tudelft.sem.template.common.models.activity.TypesOfPositionsDeserializer;
import nl.tudelft.sem.template.common.models.user.Tuple;
import nl.tudelft.sem.template.common.models.user.TupleDeserializer;
import nl.tudelft.sem.template.common.models.user.UserDetailsModel;
import org.springframework.stereotype.Service;

@Service
public class UserModelParser {

    /**
     * Getting the UserDetailsModel from the response.
     *
     * @param response response
     * @return userDetailsModel
     * @throws JsonProcessingException
     * */
    public UserDetailsModel getModel(HttpResponse<String> response) throws JsonProcessingException {
        // Parse the response body
        ObjectMapper mapper = new ObjectMapper();

        SimpleModule module = new SimpleModule();
        module.addDeserializer(TypesOfPositions.class, new TypesOfPositionsDeserializer());
        module.addDeserializer(Tuple.class, new TupleDeserializer());
        mapper.registerModule(module);

        return mapper.readValue(response.body(), UserDetailsModel.class);
    }
}
