package com.fneves.devsuperiortfcap1.services;

import com.fneves.devsuperiortfcap1.dto.ClientDTO;
import com.fneves.devsuperiortfcap1.entities.Client;
import com.fneves.devsuperiortfcap1.repositories.ClientRepository;
import com.fneves.devsuperiortfcap1.services.exceptions.DatabaseException;
import com.fneves.devsuperiortfcap1.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
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

    @Transactional(readOnly = true)
    public ClientDTO findById(Long id){
        Optional<Client> obj = clientRepository.findById(id);
        Client entity = obj.orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado."));
        return new ClientDTO(entity);
    }

    @Transactional
    public ClientDTO insert(ClientDTO dto){
        Client entity = new Client();
        copyDtoToEntity(dto, entity);
        entity = clientRepository.save(entity);
        return new ClientDTO(entity);
    }

    @Transactional
    public ClientDTO update(Long id, ClientDTO dto) {
        try {
            Client entity = clientRepository.getOne(id);
            copyDtoToEntity(dto, entity);
            entity = clientRepository.save(entity);
            return new ClientDTO(entity);
        } catch (EntityNotFoundException e){
            throw new ResourceNotFoundException("Id não encontrado: " + id);
        }
    }

    public void delete(Long id){
        try{
            clientRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Id não encontrado: " + id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Violação de integridade");
        }
    }

    private void copyDtoToEntity(ClientDTO dto, Client entity) {
        entity.setName(dto.getName());
        entity.setCpf(dto.getCpf());
        entity.setbirthDate(dto.getbirthDate());
        entity.setIncome(dto.getIncome());
        entity.setChildren(dto.getChildren());
    }

}
