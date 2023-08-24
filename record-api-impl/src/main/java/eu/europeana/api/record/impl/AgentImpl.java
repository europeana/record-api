package eu.europeana.api.record.impl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import dev.morphia.annotations.Entity;
import eu.europeana.api.record.model.Agent;
import eu.europeana.api.record.vocabulary.EntityTypes;

@Entity(useDiscriminator = false)
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AgentImpl extends EntityImpl implements Agent {

    private String type = EntityTypes.Agent.getEntityType();

    public AgentImpl() {
    }

    @Override
    public String getType() {
        return type;
    }
}
