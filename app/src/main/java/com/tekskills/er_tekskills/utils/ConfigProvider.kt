package com.tekskills.er_tekskills.utils

import com.tekskills.er_tekskills.utils.Common.Companion.INITIALIZE_CONFIG


object ConfigProvider {

    private var configuration: CTConfig? = null

    fun setConfiguration(config: CTConfig) {
        configuration = config
    }

    fun getConfiguration() = configuration ?:
    throw IllegalStateException(INITIALIZE_CONFIG)
}