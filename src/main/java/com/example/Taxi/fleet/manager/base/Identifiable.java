package com.example.Taxi.fleet.manager.base;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
public class Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
}
