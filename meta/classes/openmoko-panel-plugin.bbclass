SECTION = "openmoko/panel-plugin"
DEPENDS += "matchbox-panel-2 libmokopanelui2"

inherit openmoko2

FILES_${PN} = "${libdir}/matchbox-panel/lib*.so* ${datadir}"
