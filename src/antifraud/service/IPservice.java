package antifraud.service;


import antifraud.DTO.IP_DTO;
import antifraud.DTO.IP_RemovedDTO;
import antifraud.model.Ip;
import antifraud.repository.IpRepository;

import antifraud.request.IPrequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;


@Component
@Service
public class IPservice {

    private final IpRepository ipRepository;

    @Autowired
    public IPservice(IpRepository ipRepository) {
        this.ipRepository = ipRepository;
    }


    public ResponseEntity<IP_DTO> register(IPrequest request) {
        String ip = request.getIp();
        if (isIpInvalid(ip)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid IP");
        }
        if (ipRepository.existsByIp(ip)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "IP already in database");
        }
        Ip ipEntity = new Ip();
        ipEntity.setIp(ip);
        ipRepository.save(ipEntity);
        return new ResponseEntity<>(new IP_DTO(ipEntity.getId(), ipEntity.getIp()), HttpStatus.OK);
    }

    public ResponseEntity<IP_RemovedDTO> remove(String ip) {
        if (isIpInvalid(ip)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid IP");
        }
        if (!ipRepository.existsByIp(ip)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "IP not found");
        }
        Ip ipEntity = ipRepository.findByIp(ip).get();
        ipRepository.delete(ipEntity);
        return new ResponseEntity<>(new IP_RemovedDTO(String.format("IP %s successfully removed!", ipEntity.getIp())), HttpStatus.OK);
    }

    public ResponseEntity<List<IP_DTO>> findAll() {
        ArrayList returnList= new ArrayList();
        var sortedIpsFromRepository = ipRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        sortedIpsFromRepository.forEach(s -> returnList.add(s));
        return new ResponseEntity<>(returnList, HttpStatus.OK);
    }

    private boolean isIpInvalid(String ip) {
        String[] ipSplitByDot = ip.split("\\.");
        if (ipSplitByDot.length != 4) {
            return true;
        }
        try {
            for (String s : ipSplitByDot) {
                int i = Integer.parseInt(s);
                if (i < 0 || i > 255) {
                    return true;
                }
            }
        } catch (Exception e) {
            return true;
        }
        return false;
    }
}
