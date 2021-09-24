package com.fneves.devsuperiortfcap1.services;

import com.fneves.devsuperiortfcap1.dto.ClientDTO;
import com.fneves.devsuperiortfcap1.entities.Client;
import com.fneves.devsuperiortfcap1.repositories.ClientRepository;
import com.fneves.devsuperiortfcap1.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Transactional(readOnly = true)
    public Page<ClientDTO> findAllPaged(PageRequest pageRequest){
        Page<Client> listClient = clientRepository.findAll(pageRequest);
        Page<ClientDTO> clientDTOS = listClient.map(x -> new ClientDTO(x));
        return clientDTOS;
    }

    public ClientDTO findById(Long id){
        Optional<Client> obj = clientRepository.findById(id);
        Client entity = obj.orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado."));
        return new ClientDTO(entity);
    }

}
