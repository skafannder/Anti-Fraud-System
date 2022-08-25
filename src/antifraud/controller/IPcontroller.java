package antifraud.controller;

import antifraud.DTO.IP_DTO;
import antifraud.DTO.IP_RemovedDTO;
import antifraud.request.IPrequest;
import antifraud.service.IPservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
public class IPcontroller {
    @Autowired
    private IPservice ipService;

    @PostMapping(value = "/api/antifraud/suspicious-ip")
    public ResponseEntity<IP_DTO> registerIp(@Valid @RequestBody IPrequest ip) {
        return ipService.register(ip);
    }

    @DeleteMapping(value = "/api/antifraud/suspicious-ip/{ip}")
    public ResponseEntity<IP_RemovedDTO> removeIp(@Valid @PathVariable String ip) {
        return ipService.remove(ip);
    }

    @GetMapping(value = "/api/antifraud/suspicious-ip")
    public ResponseEntity<List<IP_DTO>> getIps() {
        return ipService.findAll();
    }

}
