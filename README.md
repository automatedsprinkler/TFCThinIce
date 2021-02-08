# TFC+ Thin Ice

A simple addon for TerraFirmaCraft+ 0.86.2 that adds dangerous thin ice. Main features:

* The new ice has 3 variants: thin, moderate and thick.
* When stepped on, thin ice has a chance of breaking and moderate ice has a chance of downgrading to thin ice. Thick ice is perfectly safe.
* Sprinting and jumping increases the chance of breaking ice, sneaking decreases it. Falling onto ice from more than 4 blocks guarantees the ice will break/downgrade (8 if sneaking)
* When water freezes, it starts as thin ice and has a chance of freezing further into the thicker variants. The further below freezing temperature it is the faster it will progress
* When the ambient temperature is above freezing, ice will gradually thaw through each stage before becoming water. The further above freezing temperature it is the faster it will progress.

Some things to note:
* When ice is generated in new chunks, it will always start out as thin ice. AFAIK there is nothing that can really be done about this with an addon.
* This addon completely replaces TFC+'s ice with its own block which may cause compatibility issues with other addons that mess with ice.
