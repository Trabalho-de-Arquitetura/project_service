package com.project_service.config;

import com.apollographql.federation.graphqljava.Federation;
import com.project_service.entity.Project;
import com.project_service.repository.ProjectRepository;
import graphql.language.StringValue;
import graphql.schema.*;
import graphql.TypeResolutionEnvironment;
import org.springframework.boot.autoconfigure.graphql.GraphQlSourceBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@Configuration
public class GraphQLFederationConfig {

    private final ProjectRepository projectRepository;

    public GraphQLFederationConfig(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    // Scalar Date customizado compatível com a versão 5.4.0 da lib
    @Bean
    public GraphQLScalarType dateScalar() {
        return GraphQLScalarType.newScalar()
                .name("Date")
                .description("Data no formato ISO (yyyy-MM-dd)")
                .coercing(new Coercing<LocalDate, String>() {
                    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

                    @Override
                    public String serialize(Object dataFetcherResult) throws CoercingSerializeException {
                        if (dataFetcherResult instanceof LocalDate) {
                            return ((LocalDate) dataFetcherResult).format(formatter);
                        }
                        throw new CoercingSerializeException("Esperado um LocalDate para serialização");
                    }

                    @Override
                    public LocalDate parseValue(Object input) throws CoercingParseValueException {
                        if (input instanceof String) {
                            try {
                                return LocalDate.parse((String) input, formatter);
                            } catch (DateTimeParseException e) {
                                throw new CoercingParseValueException("Erro ao converter valor para LocalDate");
                            }
                        }
                        throw new CoercingParseValueException("Esperado uma String para parseValue");
                    }

                    @Override
                    public LocalDate parseLiteral(Object input) throws CoercingParseLiteralException {
                        if (input instanceof StringValue) {
                            try {
                                return LocalDate.parse(((StringValue) input).getValue(), formatter);
                            } catch (DateTimeParseException e) {
                                throw new CoercingParseLiteralException("Erro ao converter literal para LocalDate");
                            }
                        }
                        throw new CoercingParseLiteralException("Esperado StringValue para parseLiteral");
                    }
                })
                .build();
    }

    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer() {
        return wiringBuilder -> wiringBuilder
                .scalar(dateScalar())  // usa o scalar customizado Date aqui
                // Removi ExtendedScalars que não são necessários
                .type("Query", builder -> builder.dataFetcher("_entities", env -> {
                    List<Map<String, Object>> representations = env.getArgument("representations");
                    assert representations != null;
                    return representations.stream()
                            .map(representation -> {
                                if ("Project".equals(representation.get("__typename"))) {
                                    Object idObject = representation.get("id");
                                    UUID id = null;
                                    if (idObject instanceof String) {
                                        try {
                                            id = UUID.fromString((String) idObject);
                                        } catch (IllegalArgumentException e) {
                                            return null;
                                        }
                                    } else if (idObject instanceof UUID) {
                                        id = (UUID) idObject;
                                    }
                                    if (id != null) {
                                        return projectRepository.findById(id).orElse(null);
                                    }
                                }
                                return null;
                            })
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());
                }));
    }

    @Bean
    public GraphQlSourceBuilderCustomizer federationCustomizer(TypeResolver entityTypeResolver, DataFetcher entitiesDataFetcher) {
        return builder -> builder.schemaFactory((registry, wiring) ->
                Federation.transform(registry, wiring)
                        .fetchEntities(entitiesDataFetcher)
                        .resolveEntityType(entityTypeResolver)
                        .build());
    }

    @Bean
    public TypeResolver entityTypeResolver() {
        return new TypeResolver() {
            @Override
            public GraphQLObjectType getType(TypeResolutionEnvironment env) {
                Object obj = env.getObject();
                if (obj instanceof Project) {
                    return env.getSchema().getObjectType("Project");
                }
                return null;
            }
        };
    }

    @Bean
    public DataFetcher entitiesDataFetcher() {
        return new DataFetcher() {
            @Override
            public Object get(DataFetchingEnvironment environment) {
                List<Map<String, Object>> representations = environment.getArgument("representations");
                List<Object> entities = new ArrayList<>();

                assert representations != null;
                for (Map<String, Object> representation : representations) {
                    String typename = (String) representation.get("__typename");
                    if ("Project".equals(typename)) {
                        String id_string = (String) representation.get("id");
                        UUID id = UUID.fromString(id_string);
                        Optional<Project> project = projectRepository.findById(id);
                        if (project.isPresent()) {
                            entities.add(project.get()); // ADICIONA O OBJETO DIRETO, NÃO Optional
                        }
                    }
                }

                return entities;
            }
        };
    }
}
