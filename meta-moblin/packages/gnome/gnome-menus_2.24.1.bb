DEPENDS = "python gnome-vfs libxml2 gconf popt gtk+"
LICENSE = "GPL"

inherit gnome pkgconfig

DEPENDS += "intltool"

PACKAGES += "${PN}-python"
FILES_${PN} += "${datadir}/desktop-directories/"
FILES_${PN}-python = "${libdir}/python*"
FILES_${PN}-dbg += "${libdir}/python*/site-packages/*/.debug \
                    ${libdir}/python*/site-packages/.debug"

