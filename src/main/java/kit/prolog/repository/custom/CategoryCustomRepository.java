package kit.prolog.repository.custom;

import kit.prolog.dto.CategoryInfoDto;

import java.util.List;

public interface CategoryCustomRepository {
    List<CategoryInfoDto> findInfoByUserAccount(String userAccount);
}
