package footlogger.footlog.service;

import footlogger.footlog.converter.AreaConverter;
import footlogger.footlog.domain.Sigungu;
import footlogger.footlog.repository.SigunguRepository;
import footlogger.footlog.web.dto.response.AreaCodeDTO;
import footlogger.footlog.web.dto.response.SigunguCodeDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AreaService {
    private final AreaConverter areaConverter;
    private final SigunguRepository sigunguRepository;

    public List<AreaCodeDTO> getAreaCodes() {
        return areaConverter.getAreaCodeDTOByCodes();
    }

    public List<SigunguCodeDTO> getSigunguInfo() {
        List<Sigungu> sigungus = sigunguRepository.findAll();

        return sigungus.stream()
                .map(areaConverter::getSigunguCodeDTOByCodes)
                .toList();
    }
}
