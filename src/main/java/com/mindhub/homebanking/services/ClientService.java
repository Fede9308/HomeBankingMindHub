package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;

import java.util.List;

public interface ClientService {

    List<ClientDTO> getClientsDTO();
    Client findById(Long id);
    ClientDTO getCLientDTO(Long id);
    Client findByEmail(String email);
    void save(Client client);
    ClientDTO getCurrentClient(String email);

}
