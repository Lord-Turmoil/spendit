/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package top.tony.spendit.api.common.requets;

import org.springframework.beans.factory.annotation.Autowired;
import top.tony.spendit.api.extensions.mappers.Mappers;

public class BaseController {
    @Autowired
    protected Mappers mappers;
}
