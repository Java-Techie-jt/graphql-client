package com.javatechie.client;

import com.javatechie.dto.Item;
import com.javatechie.dto.ItemRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class InventoryClient {

    @Autowired
    private HttpGraphQlClient graphQlClient;


    public List<Item> viewProducts() {
        String graphQLQuery = "query GetProducts {\n" +
                "    getProducts {\n" +
                "        name\n" +
                "        price\n" +
                "    }\n" +
                "}";

        return graphQlClient.document(graphQLQuery)
                .retrieve("getProducts")
                .toEntityList(Item.class).block();
    }


    public List<Item> viewProductsByCategory(String category) {

        String graphQLQuery = String.format("query GetProductsByCategory {\n" +
                "    getProductsByCategory(category: \"%s\") {\n" +
                "        name\n" +
                "        category\n" +
                "        price\n" +
                "        stock\n" +
                "    }\n" +
                "}\n", category);

        return graphQlClient.document(graphQLQuery)
                .retrieve("getProductsByCategory")
                .toEntityList(Item.class).block();
    }

    public Item receiveNewShipment(ItemRequestDTO itemRequest) {
        String graphQlQuery = String.format("mutation ReceiveNewShipment {\n" +
                "    receiveNewShipment(id: \"%s\", quantity: %d) {\n" +
                "        name\n" +
                "        price\n" +
                "        stock\n" +
                "    }\n" +
                "}\n", itemRequest.getId(), itemRequest.getQty());
        return graphQlClient.document(graphQlQuery)
                .retrieve("receiveNewShipment")
                .toEntity(Item.class).block();
    }
}
