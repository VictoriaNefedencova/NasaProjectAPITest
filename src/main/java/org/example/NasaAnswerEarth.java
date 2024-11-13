package org.example;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NasaAnswerEarth {

    String date;
    String id;
    Resource resource;
    String service_version;
    String url;

    // Конструктор для инициализации всех полей
    public NasaAnswerEarth(@JsonProperty("date") String date,
                           @JsonProperty("id") String id,
                           @JsonProperty("resource") Resource resource,
                           @JsonProperty("service_version") String service_version,
                           @JsonProperty("url") String url) {
        this.date = date;
        this.id = id;
        this.resource = resource;
        this.service_version = service_version;
        this.url = url;
    }

    // Вложенный класс для поля "resource"
    public static class Resource {

        String dataset;
        String planet;

        public Resource(@JsonProperty("dataset") String dataset,
                        @JsonProperty("planet") String planet) {
            this.dataset = dataset;
            this.planet = planet;
        }
    }
}
