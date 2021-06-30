package com.atroot.crowd.service.api;

import com.atroot.crowd.entity.Menu;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description:
 *
 * @author atroot@126.com  @ZYD
 * @create 2021.6.17 20:00
 */
@Service
public interface MenuService {

    List<Menu> getAll();

    void saveMenu(Menu menu);

    void updateMenu(Menu menu);

    void removeMenu(Integer id);
}
