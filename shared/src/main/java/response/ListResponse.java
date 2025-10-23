package response;

import java.util.Collection;

public record ListResponse(Collection<model.GameDataListItem> games){ }
