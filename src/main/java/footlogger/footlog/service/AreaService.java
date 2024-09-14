package footlogger.footlog.service;

import footlogger.footlog.converter.AreaConverter;
import footlogger.footlog.web.dto.response.AreaCodeDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AreaService {
    private final AreaConverter areaConverter;

    public List<AreaCodeDTO> getAreaCodes() {
        return areaConverter.getAreaCodeDTOByCodes();
    }
}
