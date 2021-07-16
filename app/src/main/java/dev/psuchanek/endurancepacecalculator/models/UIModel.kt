package dev.psuchanek.endurancepacecalculator.models

import dev.psuchanek.endurancepacecalculator.models.zones.Zones

sealed class UIModel {
    class ZonesModel(val zonesItem: Zones) : UIModel()
    class SplitsModel(val splitItem: Split) : UIModel()
}